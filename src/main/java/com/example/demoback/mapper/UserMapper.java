package com.example.demoback.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.demoback.entity.Account;

/**
 * 用户Mapper类
 * 
 * @author xu-jssy
 */
@Mapper// @Mapper注解表示这是一个Mybatis的Mapper类
public interface UserMapper {
    // @Select注解表示这是一个查询语句
    @Select("select * from db_account where username = #{text} or email = #{text}")
    // 根据用户名或邮箱查询账户信息
    public Account findAccountByNameOrEmail(String text);
}
