package com.aibaixun.gateway.service;

import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.gateway.service.follback.AuthClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wang xiao
 * @date 2022/5/29
 */
@FeignClient(name = "uaa",path = "/uaa",fallback = AuthClientFallback.class)
public interface AuthFeignClient {
    /**
     * 检测用户时候具备访问改地址方法
     * @param userid id
     * @param requestPath 请求地址
     * @param method 方法
     * @return jsonResult
     */
    @GetMapping("/auth")
    JsonResult<Boolean> checkPath(@RequestParam String userid, @RequestParam String requestPath, @RequestParam HttpMethod method);
}
