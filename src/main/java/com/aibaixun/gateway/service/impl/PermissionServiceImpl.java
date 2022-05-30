package com.aibaixun.gateway.service.impl;

import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.gateway.service.AuthFeignClient;
import com.aibaixun.gateway.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        CompletableFuture<JsonResult<Boolean>> completableFuture = CompletableFuture.supplyAsync(() -> this.authFeignClient.checkPath(uid, url, method));
        try {
            JsonResult<Boolean> booleanJsonResult = completableFuture.get(6, TimeUnit.SECONDS);
            if (JsonResult.isSuccess(booleanJsonResult)){
                return booleanJsonResult.getData();
            }
        }catch (ExecutionException |InterruptedException | TimeoutException e){
            return false;
        }
        return false;
    }

    @Autowired
    public void setAuthFeignClient(AuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }
}
