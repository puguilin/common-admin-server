package com.github.guilin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.guilin.common.Constant;
import com.github.guilin.domain.entity.SysDepartment;
import com.github.guilin.domain.entity.SysRole;
import com.github.guilin.domain.entity.SysUser;
import com.github.guilin.domain.vo.RoleVo;
import com.github.guilin.mapper.SysRoleMapper;
import com.github.guilin.mapper.SysUserMapper;
import com.github.guilin.service.SysDepartmentService;
import com.github.guilin.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysDepartmentService sysDepartmentService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public int add(SysUser sysUser) {
        String password = sysUser.getPassword();
        sysUser.setPassword(passwordEncoder.encode(password));
        sysUser.setAvatar("https://img-blog.csdnimg.cn/7bb73a6a05d64a02b8c4f50722862e6e.gif");
        sysUser.setCreateTime(LocalDateTime.now());
        return sysUserMapper.insert(sysUser);
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        // 先删除用户和角色关联信息
        sysUserMapper.deleteSysUserRoleByUid(id);
        // 然后再单独的删除用户信息
        return sysUserMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    @Override
    public int updateById(SysUser sysUser) {
        Long id = sysUser.getId();
        String password = sysUser.getPassword();
        if (Constant.PASSWORD_MASK.equals(password)) {
            SysUser oldUser = sysUserMapper.selectByPrimaryKey(id);
            sysUser.setPassword(oldUser.getPassword());
        } else {
            sysUser.setPassword(passwordEncoder.encode(password));
        }
        return sysUserMapper.updateByPrimaryKey(sysUser);
    }

    @Override
    public SysUser getById(Long id) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        sysUser.setPassword(Constant.PASSWORD_MASK);
        return sysUser;
    }

    @Override
    public PageInfo<SysUser> getPage(SysUser query, Integer pageNum, Integer pageSize) {
        List<SysUser> list;
        // 进行分页处理
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        // 进行部门查询
        if (query != null && query.getDeptId() != null) {
            List<SysDepartment> offspring = sysDepartmentService.getOffspring(query.getDeptId());
            List<Long> deptIds = offspring.stream().map(item -> item.getId()).collect(Collectors.toList());
            deptIds.add(query.getDeptId());
            log.debug("Offspring SysDepartment Ids: {}", deptIds);
            list = sysUserMapper.selectByDeptIds(deptIds);
        }
        // 进行其他查询
        else {
            list = sysUserMapper.selectList(query);
        }
        // 密码进行保密
        list.stream().forEach(item -> {
            item.setPassword(Constant.PASSWORD_MASK);
        });
        // 重新设置分页
        return new PageInfo<>(list);
    }

    @Override
    public List<SysUser> selectByDeptIds(List<Long> deptIds) {
        return sysUserMapper.selectByDeptIds(deptIds);
    }

    @Override
    public RoleVo getRole(Long id, Integer pageSize) {
        List<SysRole> sysRoles = sysUserMapper.getRoleByUid(id);
        List<Long> sysRoleIds = sysRoles.stream().map(item -> item.getId()).collect(Collectors.toList());
        int count = sysRoleMapper.selectCount();
        int pageTotal = (int) (Math.ceil(1.0 * count / pageSize));
        RoleVo roleVo = new RoleVo();
        List<List<Long>> checks = roleVo.getChecks();
        for (int pageNum = 1; pageNum <= pageTotal; pageNum++) {
            PageHelper.startPage(pageNum, pageSize);
            List<SysRole> sysRoleList = sysRoleMapper.selectList(null);
            List<Long> current = new ArrayList<>();
            for (SysRole sysRole : sysRoleList) {
                if (sysRoleIds.contains(sysRole.getId())) {
                    current.add(sysRole.getId());
                }
            }
            checks.add(current);
        }
        return roleVo;
    }

    @Transactional
    @Override
    public int assignRole(Long uid, List<Long> rids) {
        // 先删除旧角色
        sysUserMapper.deleteSysUserRoleByUid(uid);
        // 再插入新角色
        if (rids != null) {
            return sysUserMapper.insertSysUserRole(uid, rids);
        } else {
            return 1;
        }
    }

    @Override
    public SysUser getByUsername(String username) {
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        sysUser.setPassword(Constant.PASSWORD_MASK);
        return sysUser;
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser == null) {
            return false;
        }
        if (passwordEncoder.matches(oldPassword, sysUser.getPassword())) {
            sysUser.setPassword(passwordEncoder.encode(newPassword));
            return sysUserMapper.updateByPrimaryKey(sysUser) > 0;
        } else {
            return false;
        }
    }
}
