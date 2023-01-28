package com.github.guilin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于封装token信息的实体类
 *
 * @author CaoChenLei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVo {
    //@JsonProperty("access_token")
    private String accessToken;
    //@JsonProperty("token_type")
    private String tokenType;
    //@JsonProperty("refresh_token")
    private String refreshToken;
    //@JsonProperty("expires_in")
    private Long expiresIn;
    //@JsonProperty("expires_type")
    private String expiresType;
}
