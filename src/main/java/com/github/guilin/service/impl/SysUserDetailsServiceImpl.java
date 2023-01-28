package com.github.guilin.service.impl;

import com.github.guilin.domain.entity.SysMenu;
import com.github.guilin.domain.entity.SysUser;
import com.github.guilin.mapper.SysMenuMapper;
import com.github.guilin.mapper.SysUserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 完成认证与授权服务
 *
 * @author CaoChenLei
 */
@Component
public class SysUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //认证
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("账户或密码错误!");
        }
        //授权
        List<SysMenu> menus = sysMenuMapper.selectMenuListBySysUserId(sysUser.getId());
        sysUser.setMenus(menus);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (SysMenu menu : menus) {
            authorities.add(new SimpleGrantedAuthority(menu.getCode()));
        }
        sysUser.setAuthorities(authorities);
        return sysUser;
    }
}
