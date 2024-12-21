package com.provider;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;


@EnableCircuitBreaker
@SpringBootApplication
@EnableHystrixDashboard
@EnableDiscoveryClient
public class Provider1011Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider1011Application.class, args);
    }


}
