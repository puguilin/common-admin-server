package com.github.guilin.service;

import com.github.pagehelper.PageInfo;
import com.github.guilin.domain.entity.SysRole;
import com.github.guilin.domain.vo.PermissionVo;

import java.util.List;

public interface SysRoleService {
    int add(SysRole sysRole);

    int deleteById(Long id);

    int updateById(SysRole sysRole);

    SysRole getById(Long id);

    PageInfo<SysRole> getPage(SysRole query, Integer pageNum, Integer pageSize);

    PermissionVo getPermission(Long id);

    int assignMenu(Long rid, List<Long> mids);

    List<Long> selectByMenuIds(List<Long> menuIds);
}
