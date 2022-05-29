package com.aibaixun.gateway.service.follback;

import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.gateway.service.AuthFeignClient;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * @author wang xiao
 * @date 2022/5/29
 */
@Component
public class AuthClientFallback implements AuthFeignClient {
    @Override
    public JsonResult<Boolean> checkPath(String userid, String requestPath, HttpMethod method) {
        return JsonResult.success(false);
    }
}
