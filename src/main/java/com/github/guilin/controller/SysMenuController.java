package com.github.guilin.controller;

import com.github.guilin.annotation.SysLogRecord;
import com.github.guilin.domain.entity.SysMenu;
import com.github.guilin.domain.vo.ResultVo;
import com.github.guilin.service.SysMenuService;
import com.github.guilin.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "系统菜单接口")
@RestController
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysRoleService sysRoleService;

    @SysLogRecord("新增系统菜单信息")
    @PreAuthorize("hasAuthority('sys:menu:add')")
    @ApiOperation("新增系统菜单信息")
    @PostMapping("/api/sysMenu/add")
    public ResultVo add(@RequestBody SysMenu sysMenu) {
        int rows = sysMenuService.add(sysMenu);
        if (rows > 0) {
            return ResultVo.success("添加成功！");
        } else {
            return ResultVo.fail("添加失败！");
        }
    }

    @SysLogRecord("根据主键删除菜单")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @ApiOperation("根据主键删除菜单")
    @GetMapping("/api/sysMenu/deleteById")
    public ResultVo deleteById(Long id) {
        List<SysMenu> child = sysMenuService.getChild(id);
        if (child.isEmpty()) {
            List<SysMenu> offspring = sysMenuService.getOffspring(id);
            List<Long> menuIds = offspring.stream().map(item -> item.getId()).collect(Collectors.toList());
            menuIds.add(id);
            List<Long> sysRoles = sysRoleService.selectByMenuIds(menuIds);
            if (sysRoles.isEmpty()) {
                int rows = sysMenuService.deleteById(id);
                if (rows > 0) {
                    return ResultVo.success("删除成功！");
                } else {
                    return ResultVo.fail("删除失败！");
                }
            } else {
                return ResultVo.fail("该菜单已被授权，不能直接删除！");
            }
        } else {
            return ResultVo.fail("该菜单存在下级，不能直接删除！");
        }
    }

    @SysLogRecord("更新系统菜单信息")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    @ApiOperation("更新系统菜单信息")
    @PostMapping("/api/sysMenu/update")
    public ResultVo updateById(@RequestBody SysMenu sysMenu) {
        Long parentId = sysMenu.getParentId();
        Long id = sysMenu.getId();
        if (!parentId.equals(id)) {
            int rows = sysMenuService.updateById(sysMenu);
            if (rows > 0) {
                return ResultVo.success("更新成功！");
            } else {
                return ResultVo.fail("更新失败！");
            }
        } else {
            return ResultVo.fail("该菜单的上级，不能指向自己！");
        }
    }

    @SysLogRecord("根据主键获取菜单")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    @ApiOperation("根据主键获取菜单")
    @GetMapping("/api/sysMenu/getById")
    public ResultVo getById(Long id) {
        SysMenu sysMenu = sysMenuService.getById(id);
        return ResultVo.successWith(sysMenu);
    }

    @SysLogRecord("获取系统菜单列表")
    @ApiOperation("获取系统菜单列表")
    @GetMapping("/api/sysMenu/getList")
    public ResultVo getList() {
        List<SysMenu> sysMenuList = sysMenuService.getList();
        return ResultVo.successWith(sysMenuList);
    }

    @SysLogRecord("条件查询系统部门")
    @PreAuthorize("hasAuthority('sys:menu:search')")
    @ApiOperation("条件查询系统部门")
    @GetMapping("/api/sysMenu/search")
    public ResultVo search(SysMenu query) {
        List<SysMenu> sysMenuList = sysMenuService.search(query);
        return ResultVo.successWith(sysMenuList);
    }

    @SysLogRecord("获取父级菜单列表")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    @ApiOperation("获取父级菜单列表")
    @GetMapping("/api/sysMenu/getParent")
    public ResultVo getParent() {
        List<SysMenu> parent = sysMenuService.getParent();
        return ResultVo.successWith(parent);
    }
}
