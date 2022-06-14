package com.aibaixun.gateway.service.impl;

import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.gateway.service.AuthFeignClient;
import com.aibaixun.gateway.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wang xiao
 * @date 2022/5/30
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private  AuthFeignClient authFeignClient;

    public PermissionServiceImpl() {
    }

    @Override
    public boolean hasPermission(String uid, String url, String method)  {
        JsonResult<Boolean> booleanJsonResult1 = authFeignClient.checkPath(uid, url, method);
        if (JsonResult.isSuccess(booleanJsonResult1)){
            return booleanJsonResult1.getData();
        }
        return false;
    }

    @Autowired
    public void setAuthFeignClient(AuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }
}
