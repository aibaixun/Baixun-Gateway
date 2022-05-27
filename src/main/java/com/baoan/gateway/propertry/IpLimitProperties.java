package com.baoan.gateway.propertry;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * ip 限制名单配置
 * @author wang xiao
 * @date 2022/5/27
 */
@Component
@ConfigurationProperties(prefix = "gateway.ip")
public class IpLimitProperties {
    private Set<String> limits;

    public Set<String> getLimits() {
        return limits;
    }

    public void setLimits(Set<String> limits) {
        this.limits = limits;
    }

    public boolean needLimit(String targetIp){
        return limits.contains(targetIp);
    }
}
