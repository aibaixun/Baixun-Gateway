package com.aibaixun.gateway.filter;

import com.aibaixun.basic.jwt.JwtUtil;
import com.aibaixun.gateway.propertry.AuthProperties;
import com.aibaixun.gateway.service.AuthFeignClient;
import com.aibaixun.gateway.service.PermissionService;
import com.aibaixun.gateway.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * url 鉴权 调用授权服务
 * @author wang xiao
 * @date 2022/5/27
 */
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private AuthProperties authProperties;

    private PermissionService permissionService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!authProperties.getEnable()) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String requestUrl = request.getURI().getRawPath();
        if (ignore(path) || ignore(requestUrl)) {
            logger.info("AuthenticationFilter request :{} path:{} or url:{} is ignore",request.getId(),path,requestUrl);
            return chain.filter(exchange);
        }
        String userid = request.getHeaders().getFirst(JwtUtil.DEFAULT_USER_ID);
        String methodValue = request.getMethodValue();
        boolean hasPermission = permissionService.hasPermission(userid, path, methodValue);
        if (!hasPermission){
           return ResponseUtil.noAuth(exchange.getResponse());
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Autowired
    public void setAuthProperties(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    private boolean ignore(String path) {
        return authProperties.getIgnoreUrl().stream()
                .map(url -> url.replace("/**", ""))
                .anyMatch(path::startsWith);
    }
}
