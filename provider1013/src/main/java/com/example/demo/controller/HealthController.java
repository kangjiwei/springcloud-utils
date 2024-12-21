package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rongrong on 2020/4/6.
 */
@RestController
public class HealthController {


    @GetMapping("/isHealth")
    public Object isHealth() {
        Map<String, String> respMap = new HashMap<>();
        respMap.put("status", "1013-ok");
        return respMap;
    }
}
