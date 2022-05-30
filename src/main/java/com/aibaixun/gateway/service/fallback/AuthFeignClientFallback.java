package com.aibaixun.gateway.service.fallback;

import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.gateway.service.AuthFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author wang xiao
 * @date 2022/5/30
 */
@Component
public class AuthFeignClientFallback implements AuthFeignClient {
    @Override
    public JsonResult<Boolean> checkPath(String userid, String requestPath, String method) {
        return JsonResult.failed("request is fail");
    }
}
