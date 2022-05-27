package com.aibaixun.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

/**
 * key resolver 使用ip 做限流
 * @author wang xiao
 * @date 2022/5/27
 */
@Configuration
public class GatewayResolver {

    @Bean("ipKeyResolver")
    @Primary
    public KeyResolver ipResolver() {
        return exchange -> {
            InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
            if (null == remoteAddress){
                return Mono.empty();
            }
            return Mono.just(remoteAddress.getAddress().getHostAddress());
        };
    }

    @Bean("pathKeyResolver")
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().toString());
    }




}
