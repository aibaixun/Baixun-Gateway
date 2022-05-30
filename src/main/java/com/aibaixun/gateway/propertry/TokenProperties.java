package com.aibaixun.gateway.propertry;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author wang xiao
 * @date 2022/5/27
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "gateway.token")
public class TokenProperties {

    private Set<String> ignoreUrl = new HashSet<>();

    private Boolean enable = false;

    /**
     * 监控中心和swagger需要访问的url
     */
    private static final String[] ENDPOINTS = {
            "/oauth/**",
            "/actuator/**",
            "/v2/api-docs/**",
            "/v2/api-docs-ext/**",
            "/swagger/api-docs",
            "/swagger-ui.html",
            "/doc.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/druid/**",
            "/error/**",
            "/assets/**",
            "/auth/logout",
            "/auth/code"
    };


    public Set<String> getIgnoreUrl() {
        ignoreUrl.addAll(Arrays.asList(ENDPOINTS));
        return ignoreUrl;
    }

    public void setIgnoreUrl(Set<String> ignoreUrl) {
        this.ignoreUrl = ignoreUrl;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
