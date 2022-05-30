package com.aibaixun.gateway.service;

import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.gateway.service.fallback.AuthFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wang xiao
 * @date 2022/5/30
 */

@Component
@FeignClient(value = "uaa",decode404 = true,path = "/uaa",fallback = AuthFeignClientFallback.class)
public interface AuthFeignClient {

    /**
     * uid是否具备权限 访问url
     * @param userid 用户id
     * @param requestPath 请求url
     * @param method 方法
     * @return boolean
     */
    @GetMapping("/auth")
    JsonResult<Boolean> checkPath(@RequestParam("userid") String userid, @RequestParam("requestPath") String requestPath, @RequestParam("method") String method);

}
