package com.gateway.util.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author xiongda
 * @Date 2024/12/20 11:35
 * @Version V1.8.1
 */

@Component
public class RouteLoggingGlobalFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 打印请求路径
        System.out.println("Request Path: " + exchange.getRequest().getPath());

        // 获取匹配到的路由 ID
        String routeId = exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute");
        System.out.println("Matched Route ID: " + routeId);

        // 获取目标 URI
        String targetUri = exchange.getRequest().getURI().toString();
        System.out.println("Target URI: " + targetUri);

        // 将请求继续传递
        return chain.filter(exchange);
    }
}
