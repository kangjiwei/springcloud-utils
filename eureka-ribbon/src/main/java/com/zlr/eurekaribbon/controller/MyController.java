package com.zlr.eurekaribbon.controller;

 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rongrong on 2020/4/6.
 */
@RestController
@RequestMapping("/myController")
public class MyController {

     @GetMapping("/ok")
     public String  showInfo(){
        return "我也是服务！";
     }

}
