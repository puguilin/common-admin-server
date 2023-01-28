package com.github.guilin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.guilin.domain.entity.SysMenu;
import com.github.guilin.domain.entity.SysRole;
import com.github.guilin.domain.vo.PermissionVo;
import com.github.guilin.mapper.SysMenuMapper;
import com.github.guilin.mapper.SysRoleMapper;
import com.github.guilin.service.SysRoleService;
import com.github.guilin.utils.MakeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;

    @Transactional
    @Override
    public int add(SysRole sysRole) {
        sysRole.setCreateTime(LocalDateTime.now());
        return sysRoleMapper.insert(sysRole);
    }

    @Transactional
    @Override
    public int deleteById(Long id) {
        // 先删除角色和菜单关联信息
        sysRoleMapper.deleteSysRoleMenuByRid(id);
        // 然后再单独的删除角色信息
        return sysRoleMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    @Override
    public int updateById(SysRole sysRole) {
        return sysRoleMapper.updateByPrimaryKey(sysRole);
    }

    @Override
    public SysRole getById(Long id) {
        return sysRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<SysRole> getPage(SysRole query, Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<SysRole> list = sysRoleMapper.selectList(query);
        return new PageInfo<>(list);
    }

    @Override
    public PermissionVo getPermission(Long id) {
        // 查询所有菜单
        List<SysMenu> menus = sysMenuMapper.selectList(null);
        menus = MakeUtils.makeMenuTree(menus, 0L);
        // 查询选中集合
        Long[] checks = sysRoleMapper.selectChecks(id);
        // 封装视图对象
        PermissionVo permissionVo = new PermissionVo();
        permissionVo.setMenus(menus);
        permissionVo.setChecks(checks);
        return permissionVo;
    }

    @Transactional
    @Override
    public int assignMenu(Long rid, List<Long> mids) {
        // 先删除旧数据
        sysRoleMapper.deleteSysRoleMenuByRid(rid);
        // 再插入新数据
        if (mids != null) {
            return sysRoleMapper.insertSysRoleMenu(rid, mids);
        } else {
            return 1;
        }
    }

    @Override
    public List<Long> selectByMenuIds(List<Long> menuIds) {
        return sysRoleMapper.selectByMenuIds(menuIds);
    }
}
