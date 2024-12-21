package com.gateway.util;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

/**
 * Created by rongrong on 2020/4/6.
 */
@Configuration
public class MainConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
