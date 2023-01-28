package com.github.guilin.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
public class SysUser implements UserDetails {
    private Long id;//用户id
    private String username;//用户名称
    private String password;//用户密码
    private String avatar;//用户头像
    private Integer sex;//用户性别
    private String phone;//用户手机
    private String email;//用户邮箱
    private Integer state;//用户状态
    private String remark;//用户备注
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;//创建时间
    private Long deptId;//部门id

    @JsonIgnore
    Collection<? extends GrantedAuthority> authorities;
    @JsonIgnore
    List<SysMenu> menus;

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return state != null && state != 3;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return state != null && state != 2;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return state != null && state != 4;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return state != null && state != 1;
    }
}
