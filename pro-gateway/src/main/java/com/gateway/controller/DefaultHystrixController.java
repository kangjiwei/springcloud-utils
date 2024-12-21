package com.gateway.controller;

import com.gateway.util.DCFRespMsg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author kangjiweii
 * @Date 2020/4/7.
 */
@Slf4j
@RestController
public class DefaultHystrixController {


    @Value("${var}")
    public String var;

    @Autowired
    DCFRespMsg dcfRespMsg;

    @RequestMapping("/defaultfallback")
    public void defaultfallback(Throwable e) {
        /*InetSocketAddress inetSocketAddress = request.hostAddress();
        InetAddress address = inetSocketAddress.getAddress();
        String hostName = address.getHostName();*/
        log.info("熔断--请求:{}");
        log.info("进行熔断");
        //return dcfRespMsg.setCode(HYSTRIX).addContent("msg", e.getMessage());
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {

        return var;
    }


}
