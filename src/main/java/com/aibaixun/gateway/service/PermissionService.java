package com.aibaixun.gateway.service;

/**
 * @author wang xiao
 * @date 2022/5/30
 */
public interface PermissionService {

    /**
     * uid是否具备权限 访问url
     * @param uid 用户id
     * @param url 请求url
     * @param method 方法
     * @return boolean
     */
    boolean hasPermission(String uid, String url, String method) ;
}
