package com.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rongrong on 2020/4/6.
 */
@RestController
public class MyController {

    @GetMapping("/hello")
    public String  sayHello(){
        return "Hello 1012！";
    }

}
