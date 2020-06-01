package com.gateway.service;

import com.gateway.util.DCFRespMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.gateway.util.CodeEnum.HYSTRIX;

/**
 * Created by Administrator on 2020/4/14.
 */
@Service
public class DefaultHystrixService {

    @Autowired
    DCFRespMsg dcfRespMsg;

    @RequestMapping("/defaultfallback")
    public DCFRespMsg defaultfallback(Throwable e) {
        return dcfRespMsg.setCode(HYSTRIX).addContent("msg", e.getMessage());
    }


}
