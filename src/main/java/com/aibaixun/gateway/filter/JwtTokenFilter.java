package com.aibaixun.gateway.filter;

import com.aibaixun.basic.jwt.JwtUtil;
import com.aibaixun.basic.util.IDUtils;
import com.aibaixun.basic.util.StringUtil;
import com.aibaixun.gateway.propertry.TokenProperties;
import com.aibaixun.gateway.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * jwt token 认证处理
 * @author wang xiao
 * @date 2022/5/27
 */
@Component
public class JwtTokenFilter implements GlobalFilter, Ordered {

    private TokenProperties tokenProperties;


    private static final String TOKEN_NAME ="X-AUTH";

    private final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);



    @Autowired
    public void setAuthProperties(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!tokenProperties.getEnable()) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String requestUrl = request.getURI().getRawPath();
        if (ignore(path) || ignore(requestUrl)) {
            logger.info("JwtTokenFilter request :{} path:{} or url:{} is ignore",request.getId(),path,requestUrl);
            return chain.filter(exchange);
        }
        ServerHttpResponse response = exchange.getResponse();
        String headerToken = request.getHeaders().getFirst(TOKEN_NAME);
        if (StringUtil.isEmpty(headerToken)) {
            logger.info("JwtTokenFilter request :{} token is empty",request.getId());
            return ResponseUtil.noToken(response);
        }
        String uid = JwtUtil.getUid(null,headerToken);
        String tid = JwtUtil.getTid(null,headerToken);
        if (StringUtil.isEmpty(uid) || StringUtil.isEmpty(tid)) {
            logger.info("JwtTokenFilter request :{} uid:{} or tid:{} is empty",request.getId(),uid,tid);
            return ResponseUtil.noAuth(response);
        }
        ServerHttpRequest.Builder requestBuilder = request.mutate();
        requestBuilder.headers(k->k.remove(TOKEN_NAME));
        requestBuilder.header(JwtUtil.DEFAULT_USER_ID, uid);
        requestBuilder.header(JwtUtil.DEFAULT_TENANT_ID, tid);
        MDC.put("TraceId", IDUtils.randomUUID());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }


    private boolean ignore(String path) {
        return tokenProperties.getIgnoreUrl().stream()
                .map(url -> url.replace("/**", ""))
                .anyMatch(path::startsWith);
    }


}
