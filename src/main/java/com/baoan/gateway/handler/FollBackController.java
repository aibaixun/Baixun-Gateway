package com.baoan.gateway.handler;

import com.aibaixun.basic.result.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author wang xiao
 * @date 2022/5/27
 */
@RestController
public class FollBackController {
    private final Logger logger = LoggerFactory.getLogger(FollBackController.class);

    @RequestMapping("/fallback")
    public JsonResult<String> fallback(ServerWebExchange exchange) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        logger.error("FollBackController append fallback,route is:{}",route);
        return JsonResult.failed("下游服务调用失败,正在熔断!请稍后在尝试");
    }
}
