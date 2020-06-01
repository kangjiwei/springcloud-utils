package com.gateway.controller;

import com.gateway.util.DCFRespMsg;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;


import java.lang.annotation.Annotation;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static com.gateway.util.CodeEnum.HYSTRIX;
import static com.gateway.util.CodeEnum.NOAUTHO;

/**
 * @Author kangjiweii
 * @Date 2020/4/7.
 */
@Slf4j
@RestController
public class DefaultHystrixController {

    @Autowired
    DCFRespMsg dcfRespMsg;

    @RequestMapping("/defaultfallback")
    public void  defaultfallback(Throwable e) {
        /*InetSocketAddress inetSocketAddress = request.hostAddress();
        InetAddress address = inetSocketAddress.getAddress();
        String hostName = address.getHostName();*/
        log.info("熔断--请求:{}");
        log.info("进行熔断");
        //return dcfRespMsg.setCode(HYSTRIX).addContent("msg", e.getMessage());
    }


}
