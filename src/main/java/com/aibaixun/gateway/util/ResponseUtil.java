package com.aibaixun.gateway.util;

import com.aibaixun.basic.result.BaseResultCode;
import com.aibaixun.basic.result.JsonResult;
import com.aibaixun.basic.result.ResultCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author wang xiao
 * @date 2022/5/27
 */
public class ResponseUtil {



    private ResponseUtil (){}

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseUtil.class);




    public static Mono<Void> ipLimited(ServerHttpResponse resp){
        return error(resp,BaseResultCode.BAD_REQUEST, "IP IS LIMITED");
    }

    public static Mono<Void> noToken(ServerHttpResponse resp){
        return error(resp,BaseResultCode.BAD_REQUEST, "TOKEN IS EMPTY,PLEASE ADD TOKEN!");
    }

    public static Mono<Void> noAuth(ServerHttpResponse resp){
        return error(resp,BaseResultCode.NO_AUTH, "AUTH IS FAIL!");
    }

    public static Mono<Void> toManyRequest(ServerHttpResponse resp){
        return error(resp,BaseResultCode.TOO_MANY_REQUEST, "TOO MANY REQUEST!");
    }

    private static Mono<Void> error(ServerHttpResponse resp, ResultCode resultCode,String msg){
        resp.setStatusCode(HttpStatus.OK);
        resp.getHeaders().add("Content-Type","application/json;charset=UTF-8");
        JsonResult<String> jsonResult = JsonResult.failed(resultCode,msg);
        String returnStr = "{}";
        try {
            returnStr = OBJECT_MAPPER.writeValueAsString(jsonResult);
        } catch (JsonProcessingException e) {
            LOGGER.error("covert jsonResult :{}to json str,is error:{}",jsonResult,e.getMessage());
        }
        DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }
}
