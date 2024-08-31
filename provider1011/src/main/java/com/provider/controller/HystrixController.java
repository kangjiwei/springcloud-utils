package com.provider.controller;

import com.provider.service.IHystrixService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author xiongda
 * @Date 2024/8/29 17:31
 * @Version V1.8.1
 */

@RestController
public class HystrixController {

    @Resource
    IHystrixService hystrixService;

    @GetMapping("/hystrixTest")
    public String hystrixMethod(String name) {
        return hystrixService.getHystrix(name);
    }
}
