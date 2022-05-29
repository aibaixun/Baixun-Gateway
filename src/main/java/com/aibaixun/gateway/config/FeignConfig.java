package com.aibaixun.gateway.config;

import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author wang xiao
 * @date 2022/5/29
 */
@Configuration
public class FeignConfig {

    /**
     * 默认配置 connect time out 10s read time out 60
     */
    @Bean
    public Request.Options feignOptions (){
        return new Request.Options();
    }
    @Bean
    public Encoder feignEncoder (){
        return new SpringEncoder(feignHttpMessageConverter());
    }

    @Bean
    public Decoder feignDecoder() {
        return new OptionalDecoder(
                new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter())));
    }

    public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        return () -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
    }

}
