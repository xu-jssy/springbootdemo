package com.example.demoback.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demoback.entity.Account;
import com.example.demoback.mapper.UserMapper;

import jakarta.annotation.Resource;


/**
 * 登录认证服务类
 * 
 * @author xu-jssy
 * @desc 实现了UserDetailsService接口，用于登录认证
 */
@Service
public class AuthorizeService implements UserDetailsService{
    @Resource
    UserMapper userMapper;

    @Override
    // 重写loadUserByUsername方法，用于登录认证
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        if(username==null)
            throw new UsernameNotFoundException("用户名不存在");
        Account account=userMapper.findAccountByNameOrEmail(username);
        if(account==null)
            throw new UsernameNotFoundException("用户名或密码错误");
        
        return User
                //配置用户名、密码、权限
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .authorities("user")
                .build();
    }
}
