package com.example.demoback.entity;

import lombok.Data;

@Data
public class RestBean<T> {
    private int status;
    private boolean success;
    private T message;

    // 私有全参构造器
    private RestBean(int status, boolean success, T message){
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public static <T> RestBean<T> success(){
        return new RestBean<T>(200, true, null);
    }

    public static <T> RestBean<T> success(T data){
        return new RestBean<T>(200, true, data);
    }

    public static <T> RestBean<T> error(int status){
        return new RestBean<T>(status, false, null);
    }

    public static <T> RestBean<T> error(int status, T data){
        return new RestBean<T>(status, false, data);
    }
}
