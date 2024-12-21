package com.gateway.util.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;

/**
 * @Author kangjiwei
 * @Date 2020/4/7
 * @Describe 黑/白 名单过滤
 */
@Slf4j
public class IPCheckFilter implements GatewayFilter, Order {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        HttpHeaders headers = serverWebExchange.getRequest().getHeaders();
        String ipInfo = this.getIp(headers);
        log.info("黑白名单的使用!");
        return null;
    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    // 这边从请求头中获取用户的实际IP,根据Nginx转发的请求头获取
    private String getIp(HttpHeaders headers) {
        InetSocketAddress host = headers.getHost();
        String hostName = host.getHostName();
        System.out.println(" 是IP嘛 " + hostName);
        return hostName;
    }

}
