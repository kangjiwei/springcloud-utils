package com.gateway.util.filters;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by rongrong on 2020/5/19.
 */
@Slf4j
@Component
public class LogResponseGlobalFilter implements GlobalFilter ,Ordered{
    private static final String REQUEST_PREFIX = "Request Info [ ";

    private static final String REQUEST_TAIL = " ]";

    private static final String RESPONSE_PREFIX = "Response Info [ ";

    private static final String RESPONSE_TAIL = " ]";

    private StringBuilder normalMsg = new StringBuilder();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String id = request.getId();
        log.info("RequestId:{}",id);
        log.info("method:{}",request.getMethod());
        log.info("contentType:{}",request.getHeaders().getContentType());
        log.info("RequestTime:{}");
        log.info("RequestParams:{}",request.getQueryParams().toString());
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String method = serverHttpRequest.getMethodValue();
        URI uri1 = serverHttpRequest.getURI();
        if ("POST".equals(method)) {
            Flux<DataBuffer> body = exchange.getRequest().getBody();
            body.subscribe(buffer -> {
                byte[] bytes = new byte[buffer.readableByteCount()];
                buffer.read(bytes);
                DataBufferUtils.release(buffer);
                InputStream inputStream = buffer.asInputStream();
                try {
                    inputStream.read(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    String bodyString = new String(bytes, "utf-8");
                   // System.out.println(bodyString);
                    log.info("==============RequestBody:{}",bodyString);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });

        }
        DataBufferFactory bufferFactory = response.bufferFactory();
        normalMsg.append(RESPONSE_PREFIX);
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        // probably should reuse buffers
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        String responseResult = new String(content, Charset.forName("UTF-8"));
                        String rspStatusCode = response.getStatusCode().name();
                        log.info("RequestBody:{}",responseResult);
                        log.info("RequstStatus:{}",rspStatusCode);
                        normalMsg.append("status=").append(this.getStatusCode());
                        normalMsg.append(";header=").append(this.getHeaders());
                        normalMsg.append(";responseResult=").append(responseResult);
                        Map map = JSON.parseObject(responseResult, Map.class);
                        log.info("Gateway-response-响应时间:{}",map.get("timestamp"));
                        normalMsg.append(RESPONSE_TAIL);
                        log.info(" 让人想哭:{}",normalMsg.toString());

                        return bufferFactory.wrap(content);
                    }));
                }
                return super.writeWith(body); // if body is not a flux. never got there.
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
