package com.github.guilin.handler;

import com.github.guilin.common.Constant;
import com.github.guilin.utils.JsonUtils;
import com.github.guilin.domain.vo.ResultVo;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用来解决匿名用户访问无权限资源时的异常
 *
 * @author CaoChenLei
 */
@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(JsonUtils.toString(ResultVo.failWith(Constant.ACCESS_DENIED_EXCEPTION, "没有权限访问！")).getBytes());
        out.flush();
        out.close();
    }
}
