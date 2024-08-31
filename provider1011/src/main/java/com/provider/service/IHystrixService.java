package com.provider.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

public interface IHystrixService {


    String getHystrix(String name);

    String fallback(String name);

}
