package com.provider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rongrong on 2020/4/12.
 */

@Controller
public class SystContorller {

    @RequestMapping("/index")
    public String  index(){
         return  "/index.html";
    }

}
