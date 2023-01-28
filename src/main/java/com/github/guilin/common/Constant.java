package com.github.guilin.common;

/**
 * 系统常量
 *
 * @author CaoChenLei
 */
public class Constant {
    public static final String[] WHITE_LIST = new String[]{//访问路径白名单
            "/api/sysUser/login",
            "/api/sysUser/imageCode",
            "/favicon.ico",
            "/doc.html",
            "/swagger-resources",
            "/v2/api-docs",
            "/webjars/**"
    };

    public static final int RESPONSE_SUCCESS = 200;//响应成功200
    public static final int RESPONSE_FAIL = -1;//响应失败-1

    public static final int ACCESS_DENIED_EXCEPTION = 403;//权限不足403
    public static final int RUNTIME_EXCEPTION = 500;//系统异常500
    public static final int IMAGE_CODE_EXCEPTION = 1001;//校验验证码失败1001
    public static final int AUTH_TOKEN_EXCEPTION = 1002;//校验token失败1002
    public static final int AUTH_TOKEN_EXPIRED_EXCEPTION = 1003;//请求token过期1003
    public static final int CUSTOM_AUTHENTICATION_EXCEPTION = 1004;//自定义认证异常1004

    public static final String TOKEN_NAME = "Authorization";//请求的token的名称
    public static final String TOKEN_TYPE = "bearer";//响应的token的类型
    public static final String TOKEN_PREFIX = "Bearer ";//响应的token的前缀
    public static final String REFRESH_TOKEN_NAME = "refreshToken";//刷新token的名称

    public static final String IMAGE_CODE_NAME = "imageCode";//请求验证码的名称
    public static final String IMAGE_CODE_TYPE = "image";//请求验证码的类型（保留）
    public static final String IMAGE_CODE_PREFIX = "imageCode ";//请求验证码的前缀（保留）

    public static final String REDIS_CACHE_PREFIX = "common_admin_";//缓存的前缀值
    public static final String REDIS_AUTH_TOKEN_PREFIX = REDIS_CACHE_PREFIX + "token_";//保存到redis中的token前缀
    public static final String REDIS_IMAGE_CODE_PREFIX = REDIS_CACHE_PREFIX + "imageCode_";//保存到redis中的imageCode前缀

    public static final String PASSWORD_MASK = "*********";//返回前端的密码遮掩

    public static final String CLOUD_STORAGE_CONFIG = "CLOUD_STORAGE_CONFIG";//云存储配置项
}
