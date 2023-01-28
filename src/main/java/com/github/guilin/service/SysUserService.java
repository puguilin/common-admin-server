package com.github.guilin.service;

import com.github.pagehelper.PageInfo;
import com.github.guilin.domain.entity.SysUser;
import com.github.guilin.domain.vo.RoleVo;

import java.util.List;

public interface SysUserService {
    int add(SysUser sysUser);

    int deleteById(Long id);

    int updateById(SysUser sysUser);

    SysUser getById(Long id);

    PageInfo<SysUser> getPage(SysUser query, Integer pageNum, Integer pageSize);

    List<SysUser> selectByDeptIds(List<Long> deptIds);

    RoleVo getRole(Long id, Integer pageSize);

    int assignRole(Long uid, List<Long> rids);

    SysUser getByUsername(String username);

    boolean changePassword(String username, String oldPassword, String newPassword);
}
