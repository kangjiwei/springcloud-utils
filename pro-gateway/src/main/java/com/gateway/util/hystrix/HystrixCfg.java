package com.gateway.util.hystrix;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * Created by rongrong on 2020/5/17.
 */
@Slf4j
@Component
@AllArgsConstructor
public class HystrixCfg {

    private final HystrixFallbackHandler  hystrixFallbackHandler;

    @Bean
    public RouterFunction routerFunction(){
        return RouterFunctions.route(RequestPredicates.path("/defultFallback")
        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),hystrixFallbackHandler);
    }
}
