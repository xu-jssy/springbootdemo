package com.example.demoback.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.alibaba.fastjson.JSONObject;
import com.example.demoback.entity.RestBean;
import com.example.demoback.service.AuthorizeService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Spring security配置类
 *
 * @param
 */
@Configuration  // @Configuration注解表示这是一个配置类
@EnableWebSecurity  //@EnableWebSecurity注解表示启用Spring Security的web安全支持
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;

    @Bean   // @Bean注解表示将该方法返回的对象交给Spring容器管理
    // SecurityFilterChain是Spring Security的核心过滤器，用于对用户的请求进行过滤
    public SecurityFilterChain chain(HttpSecurity http) throws Exception {
        // HttpSecurity是Spring Security的配置类，用于配置Spring Security的相关信息
        return http
                //配置哪些请求需要被保护，哪些请求不需要被保护
                .authorizeHttpRequests()
                        .anyRequest().authenticated()// anyRequest配置所有请求都需要被保护,authenticated配置所有请求都需要被认证
                        .and()// and方法用于连接配置

                //配置登录相关信息
                .formLogin(login -> login
                        .loginProcessingUrl("/api/auth/login")// loginProcessingUrl方法用于配置登录请求的路径
                        .successHandler(this::onAuthenticationSuccess)// successHandler方法用于配置登录成功后的处理器
                        .failureHandler(this::onAuthenticationFailure))// failureHandler方法用于配置登录失败后的处理器

                //配置登出相关信息
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout"))// logoutUrl方法用于配置登出请求的路径

                //配置跨站请求伪造相关信息
                .csrf(csrf->csrf.disable())//disable方法用于禁用跨站请求伪造

                //配置异常处理相关信息
                .exceptionHandling(exceptionHandling->exceptionHandling
                        .authenticationEntryPoint(this::onAuthenticationFailure))// authenticationEntryPoint方法用于配置匿名用户访问无权限资源时的异常处理器
                
                // build方法用于构建HttpSecurity对象
                .build();
    }


    /**
     * 登录成功后的处理器
     *
     * @param 
     * @return void
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        // 设置响应编码格式为utf-8
        response.setCharacterEncoding("utf-8");
        // 将登录成功的信息以json格式写入响应
        response
            // getWriter方法用于获取响应的字符输出流
            // write方法用于将字符串写入响应
            // toJSONString方法用于将对象转换为json格式的字符串
            .getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
    }

    /**
     * 登录失败后的处理器
     * 
     * @author xu-jssy
     * @return void
     */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authentication) throws IOException{
        // 设置响应编码格式为utf-8
        response.setCharacterEncoding("utf-8");
        // 将登录失败的信息以json格式写入响应               自定义的状态类                登录失败的信息
        response.getWriter().write(JSONObject.toJSONString(RestBean.error(401, authentication.getMessage())));
    }

    /**
     * 配置自定义用户验证服务
     * 
     * @author xu-jssy
     */
    @Bean
    // AuthenticationManagerBuilder是Spring Security的身份验证构建器，用于创建AuthenticationManager
    public AuthenticationManager authenticationManagerBean(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                    .getSharedObject(AuthenticationManagerBuilder.class)// 获取AuthenticationManagerBuilder对象
                    .userDetailsService(authorizeService)// 配置自定义的用户验证服务
                    .and()
                    .build();
    }

    /**
     * 配置密码加密器
     * 
     * @author xu-jssy
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
