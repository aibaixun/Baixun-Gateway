package com.aibaixun.gateway.filter;

import com.aibaixun.gateway.propertry.IpLimitProperties;
import com.aibaixun.gateway.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * ip 限制
 * @author wang xiao
 * @date 2022/5/27
 */
@Component
public class IpFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(IpFilter.class);
    private IpLimitProperties ipLimitProperties;
    private static final String X_REAL_IP ="X-Real-IP";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        String ip = getRemoteAddress(remoteAddress);
        logger.info("IpFilter request id is:{},path is:{} from :{}",request.getId(),request.getPath(),ip);
        if (ipLimitProperties.needLimit(ip)){
            logger.warn("IpFilter request id is:{},path is:{} from :{} is limited",request.getId(),request.getPath(),ip);
            ServerHttpResponse response = exchange.getResponse();
            return ResponseUtil.ipLimited(response);
        }
        ServerHttpRequest.Builder requestBuilder = request.mutate();
        requestBuilder.header(X_REAL_IP, ip);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }


    @Autowired
    public void setIpLimitProperties(IpLimitProperties ipLimitProperties) {
        this.ipLimitProperties = ipLimitProperties;
    }

    private String getRemoteAddress(InetSocketAddress inetSocketAddress){
        if (inetSocketAddress == null){
            return "";
        }
        return inetSocketAddress.getAddress().getHostAddress();
    }

    private Mono<Void> chainFilterAndSetHeaders(GatewayFilterChain chain, ServerWebExchange exchange, LinkedHashMap<String, String> headerMap) {
        // 添加header
        Consumer<HttpHeaders> httpHeaders = httpHeader -> {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpHeader.set(entry.getKey(), entry.getValue());
            }
        };

        ServerHttpRequest newRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
        ServerWebExchange build = exchange.mutate().request(newRequest).build();
        return chain.filter(build);
    }

}
