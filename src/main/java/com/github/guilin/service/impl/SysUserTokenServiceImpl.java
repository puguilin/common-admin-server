package com.github.guilin.service.impl;

import com.github.guilin.common.Constant;
import com.github.guilin.common.Payload;
import com.github.guilin.config.prop.RsaKeyProperties;
import com.github.guilin.domain.dto.SysUserInfoDTO;
import com.github.guilin.domain.vo.TokenVo;
import com.github.guilin.exception.AuthTokenException;
import com.github.guilin.exception.AuthTokenExpiredException;
import com.github.guilin.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 系统用户token服务实现
 *
 * @author CaoChenLei
 */
@Slf4j
@Component
public class SysUserTokenServiceImpl {
    @Resource
    private RsaKeyProperties prop;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SysUserDetailsServiceImpl userDetailsService;
    @Value("${token.expire}")
    private Long expire;
    @Value("${token.unit}")
    private String unit;

    /**
     * 检查token
     *
     * @param request  本次请求
     * @param isDelete 是否删除Redis中缓存的token
     * @return UserDetails 用户详情对象
     */
    public UserDetails checkToken(HttpServletRequest request, boolean isDelete) {
        //获取token
        String token = request.getHeader(Constant.TOKEN_NAME);
        if (StringUtils.isEmpty(token)) {
            throw new AuthTokenException("token未被携带！");
        }
        if (!token.startsWith(Constant.TOKEN_PREFIX)) {
            throw new AuthTokenException("token非法格式！");
        } else {
            token = token.replace(Constant.TOKEN_PREFIX, "");
        }
        //解析载荷
        Payload<SysUserInfoDTO> payload;
        try {
            payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), SysUserInfoDTO.class);
        } catch (ExpiredJwtException e) {
            throw new AuthTokenExpiredException("token已经过期！");
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | PrematureJwtException e) {
            throw new AuthTokenException("token非法签名！");
        }
        //判断缓存
        String id = payload.getId();
        String key = Constant.REDIS_AUTH_TOKEN_PREFIX + id;
        String value = (String) redisTemplate.opsForValue().get(key);
        log.debug("Get token [{}={}] of redis, The value passed in is {}", key, value, id);
        if (StringUtils.isEmpty(value)) {
            throw new AuthTokenException("token已经过期！");
        }
        if (!value.contains(token)) {
            throw new AuthTokenException("token非法伪造！");
        }
        //判断内容
        SysUserInfoDTO sysUserInfoDTO = payload.getUserInfo();
        if (Objects.isNull(sysUserInfoDTO)) {
            throw new AuthTokenException("token内容为空！");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(sysUserInfoDTO.getUsername());
        if (Objects.isNull(userDetails)) {
            throw new AuthTokenException("token内容无效！");
        }
        //是否删除
        if (isDelete) {
            redisTemplate.delete(key);
            log.debug("Delete token [{}={}] of redis", key, id);
        }
        //返回用户
        return userDetails;
    }

    /**
     * 刷新token
     *
     * @param accessToken token值
     * @return TokenVo token对象
     */
    public TokenVo refreshToken(String accessToken) {
        //刷新token
        String refreshToken = "";
        if ("second".equals(unit)) {
            refreshToken = JwtUtils.refreshTokenExpireInSeconds(accessToken, prop.getPrivateKey(), prop.getPublicKey(), expire * 2);
        }
        if ("minute".equals(unit)) {
            refreshToken = JwtUtils.refreshTokenExpireInMinutes(accessToken, prop.getPrivateKey(), prop.getPublicKey(), expire * 2);
        }
        log.debug("Refresh token expire: {}, unit: {}", expire * 2, unit);
        //封装返回值
        TokenVo tokenVo = new TokenVo();
        tokenVo.setAccessToken(Constant.TOKEN_PREFIX + accessToken);
        tokenVo.setTokenType(Constant.TOKEN_TYPE);
        tokenVo.setRefreshToken(Constant.TOKEN_PREFIX + refreshToken);
        tokenVo.setExpiresIn(expire);
        tokenVo.setExpiresType(unit);
        //保存token
        String id = JwtUtils.getInfoFromToken(accessToken, prop.getPublicKey()).getId();
        String key = Constant.REDIS_AUTH_TOKEN_PREFIX + id;
        String value = accessToken + "," + refreshToken;
        if ("second".equals(unit)) {
            redisTemplate.opsForValue().set(key, value, expire * 2, TimeUnit.SECONDS);
        }
        if ("minute".equals(unit)) {
            redisTemplate.opsForValue().set(key, value, expire * 2, TimeUnit.MINUTES);
        }
        log.debug("Refresh and Set token [{}={}] to redis", key, value);
        //返回token
        return tokenVo;
    }
}
