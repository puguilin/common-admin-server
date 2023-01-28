package com.github.guilin.filter;

import com.github.guilin.common.Constant;
import com.github.guilin.exception.ImageCodeException;
import com.github.guilin.handler.CustomAuthenticationFailureHandler;
import com.github.guilin.service.impl.SysUserTokenServiceImpl;
import com.github.guilin.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 用于验证token的验证过滤器
 *
 * @author CaoChenLei
 */
@Slf4j
@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SysUserTokenServiceImpl sysUserTokenService;
    @Resource
    private CustomAuthenticationFailureHandler failureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        log.debug("RequestURI = {}", requestURI);
        try {
            if (Arrays.stream(Constant.WHITE_LIST).anyMatch(uri -> requestURI.startsWith(uri.replace("**", "")))) {
                //校验验证码，登录请求才会检查
                if (requestURI.equals(Constant.WHITE_LIST[0])) {
                    checkImageCode(request);
                }
            } else {
                //校验token，登录请求、获取验证码不会检查
                if (!requestURI.equals(Constant.WHITE_LIST[0]) && !requestURI.equals(Constant.WHITE_LIST[1])) {
                    checkAuthToken(request);
                }
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            failureHandler.onAuthenticationFailure(request, response, e);
        }
    }

    private void checkImageCode(HttpServletRequest request) {
        String imageCode = request.getParameter(Constant.IMAGE_CODE_NAME);
        if (StringUtils.isEmpty(imageCode)) {
            throw new ImageCodeException("验证码为空！");
        }
        String ip = IpUtils.getRemoteIp(request);
        String key = Constant.REDIS_IMAGE_CODE_PREFIX + ip + "_" + imageCode;
        String value = (String) redisTemplate.opsForValue().get(key);
        log.debug("Get {} [{}={}] of redis, The value passed in is {}", Constant.IMAGE_CODE_NAME, key, value, imageCode);
        if (StringUtils.isEmpty(value)) {
            throw new ImageCodeException("验证码失效！");
        }
        if (!imageCode.equals(value)) {
            throw new ImageCodeException("验证码错误！");
        }
        log.debug("Check {} success", Constant.IMAGE_CODE_NAME);
    }

    private void checkAuthToken(HttpServletRequest request) {
        UserDetails userDetails = sysUserTokenService.checkToken(request, false);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.debug("Authenticated user {}, setting security context", userDetails.getUsername());
    }
}
