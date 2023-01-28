package com.github.guilin.utils;

import com.github.guilin.common.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * 生成token以及校验token相关方法
 *
 * @author CaoChenLei
 */
public class JwtUtils {
    private static final String JWT_PAYLOAD_USER_KEY = "user";

    private static String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    /**
     * 私钥加密token
     *
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位分钟
     * @return JWT
     */
    public static String generateTokenExpireInMinutes(Object userInfo, PrivateKey privateKey, long expire) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtils.toString(userInfo))
                .setId(createJTI())
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(expire).atZone(ZoneId.of("Asia/Shanghai")).toInstant()))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 私钥加密token
     *
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位秒
     * @return JWT
     */
    public static String generateTokenExpireInSeconds(Object userInfo, PrivateKey privateKey, long expire) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtils.toString(userInfo))
                .setId(createJTI())
                .setExpiration(Date.from(LocalDateTime.now().plusSeconds(expire).atZone(ZoneId.of("Asia/Shanghai")).toInstant()))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 刷新token
     *
     * @param token      旧token
     * @param privateKey 私钥
     * @param publicKey  公钥
     * @param expire     过期时间
     * @return
     */
    public static String refreshTokenExpireInMinutes(String token, PrivateKey privateKey, PublicKey publicKey, long expire) {
        Jws<Claims> claims = parserToken(token, publicKey);
        return Jwts.builder()
                .setClaims(claims.getBody())
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(expire).atZone(ZoneId.of("Asia/Shanghai")).toInstant()))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 刷新token
     *
     * @param token      旧token
     * @param privateKey 私钥
     * @param publicKey  公钥
     * @param expire     过期时间
     * @return
     */
    public static String refreshTokenExpireInSeconds(String token, PrivateKey privateKey, PublicKey publicKey, long expire) {
        Jws<Claims> claims = parserToken(token, publicKey);
        return Jwts.builder()
                .setClaims(claims.getBody())
                .setExpiration(Date.from(LocalDateTime.now().plusSeconds(expire).atZone(ZoneId.of("Asia/Shanghai")).toInstant()))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return Jws<Claims>
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey, Class<T> userType) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setUserInfo(JsonUtils.toBean(body.get(JWT_PAYLOAD_USER_KEY).toString(), userType));
        claims.setExpiration(body.getExpiration());
        return claims;
    }

    /**
     * 获取token中的载荷信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setExpiration(body.getExpiration());
        return claims;
    }
}
