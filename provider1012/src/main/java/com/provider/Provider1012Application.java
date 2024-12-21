package com.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class Provider1012Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider1012Application.class, args);
    }

}
