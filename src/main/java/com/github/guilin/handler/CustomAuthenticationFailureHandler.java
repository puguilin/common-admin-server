package com.github.guilin.handler;

import com.github.guilin.common.Constant;
import com.github.guilin.exception.AuthTokenException;
import com.github.guilin.exception.AuthTokenExpiredException;
import com.github.guilin.exception.CustomAuthenticationException;
import com.github.guilin.exception.ImageCodeException;
import com.github.guilin.utils.JsonUtils;
import com.github.guilin.domain.vo.ResultVo;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录后认证失败处理器
 *
 * @author CaoChenLei
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //清理上下文
        SecurityContextHolder.clearContext();
        //判断异常类
        int code = Constant.RESPONSE_FAIL;
        String msg = "error";
        if (e instanceof UsernameNotFoundException) {
            msg = "账户或密码错误！";
        } else if (e instanceof BadCredentialsException) {
            msg = "账户或密码错误！";
        } else if (e instanceof DisabledException) {
            msg = "用户账户已被锁！";
        } else if (e instanceof CredentialsExpiredException) {
            msg = "用户密码已失效！";
        } else if (e instanceof LockedException) {
            msg = "用户账户已被锁！";
        } else if (e instanceof AccountExpiredException) {
            msg = "用户账户已过期！";
        } else if (e instanceof InternalAuthenticationServiceException) {
            msg = "认证服务有异常！";
        } else if (e instanceof ImageCodeException) {
            code = Constant.IMAGE_CODE_EXCEPTION;
            msg = e.getMessage();
        } else if (e instanceof AuthTokenException) {
            code = Constant.AUTH_TOKEN_EXCEPTION;
            msg = e.getMessage();
        } else if (e instanceof AuthTokenExpiredException) {
            code = Constant.AUTH_TOKEN_EXPIRED_EXCEPTION;
            msg = e.getMessage();
        } else if (e instanceof CustomAuthenticationException) {
            code = Constant.CUSTOM_AUTHENTICATION_EXCEPTION;
            msg = e.getMessage();
        }
        //返回客户端
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(JsonUtils.toString(ResultVo.failWith(code, msg)).getBytes());
        out.flush();
        out.close();
    }
}
