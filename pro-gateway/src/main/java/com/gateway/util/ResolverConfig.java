package com.gateway.util;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


/**
 * 路由限流配置
 * @Author kangjw
 * @Date 2020/4/7.
 */
@Component
public class ResolverConfig {

    /**
     * 根据请求IP限流
     * 问题: 此处的IP是网关的IP，还是路由转换后的IP?
     * @return
     */
/*
    @Bean
    KeyResolver IpKeyResolver(){
        return exchange-> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
*/

    /**
     * 根据用户信息限流
     * @return
     */
    @Bean
    KeyResolver UserKeyResolver(){
        return  exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("accountname"));//根据user字段进行限流
    }

}
