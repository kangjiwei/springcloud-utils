package com.zlt.eruakeclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author kangjiwei
 * @Date 2020/4/3.
 */
@RestController
@RequestMapping("/myControl")
public class MyRestController {


    @Autowired
    Environment environment;

    @GetMapping("/ok")
    public String health(){
        return "T'm Ok";
    }

    @GetMapping("/backend")
    public String backend(){
        System.out.println("Inside MyRestController::backend...");
        String serverPort = environment.getProperty("local.server.port");
        System.out.println("Port : " + serverPort);
        return "Hello form Backend!!! " + " Host : localhost " + " :: Port : " + serverPort;
    }
}
