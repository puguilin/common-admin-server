package com.github.guilin.mapper;

import com.github.guilin.domain.entity.SysRole;
import com.github.guilin.domain.entity.SysUser;

import java.util.List;

public interface SysUserMapper {
    int insert(SysUser sysUser);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKey(SysUser sysUser);

    SysUser selectByPrimaryKey(Long id);

    List<SysUser> selectList(SysUser sysUser);

    SysUser selectByUsername(String username);

    List<SysUser> selectByDeptIds(List<Long> deptIds);

    int deleteSysUserRoleByUid(Long uid);

    int insertSysUserRole(Long uid, List<Long> rids);

    List<SysRole> getRoleByUid(Long uid);
}
