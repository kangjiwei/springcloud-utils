package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rongrong on 2020/4/6.
 */
@RestController
public class HealthController {

    @GetMapping("/isHealth")
    public  String    isHealth(){
        return "ok";
    }
}
