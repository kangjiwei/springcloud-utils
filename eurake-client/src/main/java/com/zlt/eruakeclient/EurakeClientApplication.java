package com.zlt.eruakeclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class EurakeClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurakeClientApplication.class, args);
	}

}
