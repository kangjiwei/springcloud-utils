package com.gateway.util.hystrix;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_HANDLER_MAPPER_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

/**
 * Created by rongrong on 2020/5/17.
 */
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse>{
    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        ServerHttpRequest request = serverRequest.exchange().getRequest();
        ServerHttpResponse response = serverRequest.exchange().getResponse();
        log.info("网关--降级处理--：{}", response.getStatusCode());

        Optional<Object> attribute = null;
        HttpMethod method = serverRequest.method();
        if("POST".equals(method.toString())){
            attribute = serverRequest.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
            Optional<Object> attribute1 = serverRequest.attribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            attribute1.ifPresent(attr->log.info("网关--降级--Post:{}",attribute1.get()));
            Optional<Object> attribute2 = serverRequest.attribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            attribute1.ifPresent(attr->log.info("网关--降级--Post:{}",attribute2.get()));
            log.info("网关--降级--Post:{}",attribute.get());
        }else if("GET".equals(method.toString())){
            attribute = serverRequest.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
            attribute.ifPresent(original-> log.info("网关--降级--Get:{}",original));
        }
        return  ServerResponse.status(response.getStatusCode())
                .header("Content-Type","text/plain;charset=utf-8").body(BodyInserters.fromObject("服务异常"));
    }
}
