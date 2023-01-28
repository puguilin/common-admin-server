package com.github.guilin.service.impl;

import com.github.guilin.domain.entity.SysMenu;
import com.github.guilin.mapper.SysMenuMapper;
import com.github.guilin.service.SysMenuService;
import com.github.guilin.utils.MakeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Resource
    private SysMenuMapper sysMenuMapper;

    @Transactional
    @Override
    public int add(SysMenu sysMenu) {
        sysMenu.setCreateTime(LocalDateTime.now());
        return sysMenuMapper.insert(sysMenu);
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        return sysMenuMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    @Override
    public int updateById(SysMenu sysMenu) {
        return sysMenuMapper.updateByPrimaryKey(sysMenu);
    }

    @Override
    public SysMenu getById(Long id) {
        return sysMenuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysMenu> getList() {
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(null);
        return MakeUtils.makeMenuTree(sysMenuList, 0L);
    }

    @Override
    public List<SysMenu> search(SysMenu query) {
        return sysMenuMapper.selectList(query);
    }

    @Override
    public List<SysMenu> getParent() {
        // 查询父亲菜单
        List<SysMenu> parent = sysMenuMapper.selectParent(Arrays.asList(0L, 1L));
        // 构建菜单结构
        return MakeUtils.makeMenuTree(parent, 0L);
    }

    @Override
    public List<SysMenu> getChild(Long id) {
        SysMenu query = new SysMenu();
        query.setParentId(id);
        return sysMenuMapper.selectList(query);
    }

    @Override
    public List<SysMenu> getOffspring(Long id) {
        List<SysMenu> childList = new ArrayList<>();
        recursionParse(id, childList);
        return childList;
    }

    private void recursionParse(Long parentId, List<SysMenu> childList) {
        List<SysMenu> childs = getChild(parentId);
        if (childs.isEmpty()) return;
        childList.addAll(childs);
        childs.forEach(item -> recursionParse(item.getId(), childList));
    }
}
