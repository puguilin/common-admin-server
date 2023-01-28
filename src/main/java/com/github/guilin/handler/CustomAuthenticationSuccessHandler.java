package com.github.guilin.handler;

import com.github.guilin.common.Constant;
import com.github.guilin.config.prop.RsaKeyProperties;
import com.github.guilin.domain.dto.SysUserInfoDTO;
import com.github.guilin.domain.vo.ResultVo;
import com.github.guilin.domain.vo.TokenVo;
import com.github.guilin.service.impl.SysUserTokenServiceImpl;
import com.github.guilin.utils.IpUtils;
import com.github.guilin.utils.JsonUtils;
import com.github.guilin.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录后认证成功处理器
 *
 * @author CaoChenLei
 */
@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    private RsaKeyProperties prop;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SysUserTokenServiceImpl sysUserTokenService;
    @Value("${token.expire}")
    private Long expire;
    @Value("${token.unit}")
    private String unit;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //生成token
        SysUserInfoDTO sysUserInfoDTO = new SysUserInfoDTO(authentication.getName());
        String accessToken = "";
        if ("second".equals(unit)) {
            accessToken = JwtUtils.generateTokenExpireInSeconds(sysUserInfoDTO, prop.getPrivateKey(), expire);
        }
        if ("minute".equals(unit)) {
            accessToken = JwtUtils.generateTokenExpireInMinutes(sysUserInfoDTO, prop.getPrivateKey(), expire);
        }
        log.debug("Access token expire: {}, unit: {}", expire, unit);
        //刷新token
        TokenVo tokenVo = sysUserTokenService.refreshToken(accessToken);
        //返回客户端
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(JsonUtils.toString(ResultVo.successWith(tokenVo)).getBytes());
        out.flush();
        out.close();
        //清除验证码
        String ip = IpUtils.getRemoteIp(request);
        String imageCode = request.getParameter(Constant.IMAGE_CODE_NAME);
        String key = Constant.REDIS_IMAGE_CODE_PREFIX + ip + "_" + imageCode;
        redisTemplate.delete(key);
        log.debug("Delete {} [{}={}] of redis", Constant.IMAGE_CODE_NAME, key, imageCode);
    }
}
