package com.github.guilin.utils;

import com.github.guilin.domain.entity.SysDepartment;
import com.github.guilin.domain.entity.SysMenu;
import com.github.guilin.domain.vo.RouterVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 构建菜单树、路由树工具类
 *
 * @author CaoChenLei
 */
@Slf4j
public class MakeUtils {
    /**
     * 生成部门树
     */
    public static List<SysDepartment> makeDepartmentTree(List<SysDepartment> departmentList, Long parentId) {
        List<SysDepartment> list = new ArrayList<>();
        Optional.ofNullable(departmentList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item.getParentId().equals(parentId))
                .forEach(item -> {
                    SysDepartment sysDepartment = new SysDepartment();
                    BeanUtils.copyProperties(item, sysDepartment);
                    //设置children
                    sysDepartment.setChildren(makeDepartmentTree(departmentList, item.getId()));
                    //添加sysDepartment
                    list.add(sysDepartment);
                });
        return list;
    }

    /**
     * 生成菜单树
     */
    public static List<SysMenu> makeMenuTree(List<SysMenu> menuList, Long parentId) {
        List<SysMenu> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item.getParentId().equals(parentId))
                .forEach(item -> {
                    SysMenu sysMenu = new SysMenu();
                    BeanUtils.copyProperties(item, sysMenu);
                    //设置children
                    sysMenu.setChildren(makeMenuTree(menuList, item.getId()));
                    //添加sysMenu
                    list.add(sysMenu);
                });
        return list;
    }

    /**
     * 生成路由树
     */
    public static List<RouterVo> makeRouterTree(List<SysMenu> menuList, Long parentId) {
        List<RouterVo> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item.getParentId().equals(parentId))
                .forEach(item -> {
                    RouterVo routerVo = new RouterVo();
                    //设置name
                    String component = item.getComponent();
                    if (!StringUtils.isEmpty(component)) {
                        //去除组件空格
                        component = StringUtils.deleteWhitespace(component);
                        //切分路径单词
                        String[] words = component.split("/");
                        //拼接路由名称
                        StringBuilder name = new StringBuilder();
                        for (String word : words) {
                            if (!StringUtils.isEmpty(word)) {
                                name.append(StringUtils.capitalize(word));
                            }
                        }
                        //设置路由名称
                        routerVo.setName(name.toString());
                        log.debug("Set Component(type={}) Name ==> {}", item.getType(), routerVo.getName());
                    }
                    //设置path
                    routerVo.setPath(item.getPath());
                    //设置component
                    if (item.getParentId().equals(0L)) {
                        routerVo.setComponent("Layout");
                        routerVo.setAlwaysShow(true);
                        routerVo.setRedirect("noRedirect");
                    } else {
                        routerVo.setComponent(item.getComponent());
                        routerVo.setAlwaysShow(false);
                        routerVo.setHidden(item.getHidden());
                    }
                    //设置meta
                    routerVo.setMeta(routerVo.new Meta(
                            item.getName(),
                            item.getIcon(),
                            item.getCode().split(",")
                    ));
                    //设置children
                    routerVo.setChildren(makeRouterTree(menuList, item.getId()));
                    //添加routerVo
                    list.add(routerVo);
                });
        return list;
    }
}
