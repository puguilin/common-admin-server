package com.github.guilin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于在载荷中传输用户信息
 *
 * @author CaoChenLei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserInfoDTO {
    private String username;
}
