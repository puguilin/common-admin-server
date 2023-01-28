package com.github.guilin.config;

import com.github.guilin.common.Constant;
import com.github.guilin.filter.CustomAuthenticationFilter;
import com.github.guilin.handler.CustomAccessDeniedHandler;
import com.github.guilin.handler.CustomAuthenticationEntryPointHandler;
import com.github.guilin.handler.CustomAuthenticationFailureHandler;
import com.github.guilin.handler.CustomAuthenticationSuccessHandler;
import com.github.guilin.service.impl.SysUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    @Resource
    private CustomAuthenticationFailureHandler authenticationFailureHandler;
    @Resource
    private CustomAccessDeniedHandler accessDeniedHandler;
    @Resource
    private CustomAuthenticationEntryPointHandler authenticationEntryPointHandler;
    @Resource
    private CustomAuthenticationFilter authenticationFilter;
    @Resource
    private SysUserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();//开启cors跨域
        http.csrf().disable();//禁用csrf保护
        http.headers().frameOptions().disable();//禁用frame保护
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//禁用会话管理
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);//校验token
        http.formLogin()
                .loginProcessingUrl(Constant.WHITE_LIST[0])
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .authorizeRequests()
                .antMatchers(Constant.WHITE_LIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPointHandler);
    }
}
