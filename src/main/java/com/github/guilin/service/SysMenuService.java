package com.github.guilin.service;

import com.github.guilin.domain.entity.SysMenu;

import java.util.List;

public interface SysMenuService {
    int add(SysMenu sysMenu);

    int deleteById(Long id);

    int updateById(SysMenu sysMenu);

    SysMenu getById(Long id);

    List<SysMenu> getList();

    List<SysMenu> search(SysMenu query);

    List<SysMenu> getParent();

    List<SysMenu> getChild(Long id);

    List<SysMenu> getOffspring(Long id);
}
