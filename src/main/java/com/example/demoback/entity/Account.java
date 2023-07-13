package com.example.demoback.entity;

import lombok.Data;

/**
 * 账户实体类
 *
 * @author xu-jssy
 * @return 
 */
@Data
public class Account {
    int id;
    String email;
    String username;
    String password;
    
}
