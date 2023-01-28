package com.github.guilin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于展示系统用户信息
 *
 * @author CaoChenLei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserInfoVo {
    private Long id;
    private String name;
    private String avatar;
    private Object[] roles;
}
