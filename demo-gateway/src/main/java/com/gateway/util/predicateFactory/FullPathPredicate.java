/*
package com.gateway.util.predicateFactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Predicate;

*/
/**
 * @Author kangjiwei
 * @Date 2020/4/15
 * @Describe 自定义PredicateFactory
 *//*

@Slf4j
@Component
public class FullPathPredicate extends AbstractRoutePredicateFactory<FullPathPredicate.Config>  {

    public FullPathPredicate(Class<Config> configClass) {
        super(configClass);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return null;
    }

    public static class Config {
        */
/**
         * 传输token header key
         *//*

        private String headerName;

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }
    }

}
*/
