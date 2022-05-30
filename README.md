# gateway
基于SpringCloud gateway 实现网关功能
# 功能点
- 限流（基于Redis 令牌桶算法实现）
- 鉴权 (Token 校验与业务权限校验)
- 熔断 (Hystrix 实现)


# 配置说明
```yaml
spring:
  application:
    name: gateway
  redis:
    # redis 配置，开发使用集群
    cluster:
      nodes: 172.16.9.2:6379,172.16.9.3:6379,172.16.9.3:6379
  cloud:
    gateway:
      globalcors:
        # 网关跨域配置
        cors-configurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
      routes:
        #  路由配置
        - id: good
          uri: lb://good
          predicates:
            - Path= /good/**
          filters:
            - name: GatewayRequestRateLimiter
              args:
                # 令牌桶每秒填充平均速率, 允许用户每秒处理多少个请求。
                redis-rate-limiter.replenishRate: 1
                # 令牌桶的容量，允许在1s内完成的最大请求数。
                redis-rate-limiter.burstCapacity: 1
                # 使用SpEL表达式从Spring容器中获取Bean对象, 查看RateLimiteConfig实现类中的方法名
                key-resolver: "#{@ipKeyResolver}"
                #key-resolver: "#{@ipKeyResolver}"
                #key-resolver: "#{@userKeyResolver}"
            - name: Hystrix
              # 熔断配置
              args: 
                name: fallbackcmd
                fallbackUri: forward:/fallback
    #         重试配置
           - name: Retry
    #          args:
                 retries: 3 # 重试3ci
    #               series: SERVER_ERROR # 500 开头
    # 服务发现 这里使用nacos
    nacos:
      discovery:
        server-addr: 172.16.8.5:8848
        namespace: dev
        group: dev
server:
  port: 8080


gateway:
  ip:
    # ip 限制访问
    limits:
      - 127.0.0.2
  token:
    # 网关鉴权token 开关
    enable: true
    # 忽略的url
    ignoreUrl:
      - /cata/

  auth:
    # 网关鉴权 开关
    enable: true
    ignore-url:
      - /a/**

```

