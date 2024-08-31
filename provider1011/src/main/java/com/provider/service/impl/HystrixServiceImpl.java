package com.provider.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.provider.service.IHystrixService;
import org.springframework.stereotype.Service;

/**
 * @Author xiongda
 * @Date 2024/8/29 17:35
 * @Version V1.8.1
 */

@Service
public class HystrixServiceImpl implements IHystrixService {


    @HystrixCommand(fallbackMethod = "fallback")
    @Override
    public String getHystrix(String name) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("处理请求超时");
        }
        return "hello, " + name;
    }

    @Override
    public String fallback(String name) {
        return "fallback, " + name;
    }
}
