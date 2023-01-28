package com.github.guilin.mapper;

import com.github.guilin.domain.entity.SysMenu;

import java.util.List;

public interface SysMenuMapper {
    int insert(SysMenu sysMenu);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKey(SysMenu sysMenu);

    SysMenu selectByPrimaryKey(Long id);

    List<SysMenu> selectList(SysMenu sysMenu);

    List<SysMenu> selectMenuListBySysUserId(Long sysUserId);

    List<SysMenu> selectParent(List<Long> asList);
}
