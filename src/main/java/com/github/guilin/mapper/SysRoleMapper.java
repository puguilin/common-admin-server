package com.github.guilin.mapper;

import com.github.guilin.domain.entity.SysRole;

import java.util.List;

public interface SysRoleMapper {
    int insert(SysRole sysRole);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKey(SysRole sysRole);

    SysRole selectByPrimaryKey(Long id);

    List<SysRole> selectList(SysRole sysRole);

    Long[] selectChecks(Long id);

    void deleteSysRoleMenuByRid(Long rid);

    int insertSysRoleMenu(Long rid, List<Long> mids);

    List<Long> selectByMenuIds(List<Long> mids);

    int selectCount();
}
