package com.provider.controller;

import lombok.extern.slf4j.Slf4j;
import org.jcp.xml.dsig.internal.SignerOutputStream;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by rongrong on 2020/4/6.
 */
@RestController
@Slf4j
public class MyController {


     @RequestMapping("/hell1o")
     public String  sayHello(String name,String sex){
         log.info("Controller=== name:{},sex:{}",name,sex);
         try {
             Thread.sleep(50000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         return "Hello 1011! ";
     }

     @GetMapping("/love")
     public String  sayLoveYou(){
         System.out.println("线程延迟");
         try {
             Thread.sleep(10000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         return  "love you 1011!";
     }

    @RequestMapping("/savPom")
    public String savePom(MultipartFile multipartFile){
         log.info("文件的名称:{}",multipartFile.getOriginalFilename());
        return  multipartFile.getOriginalFilename();
    }

    @RequestMapping("appJson")
    public void appJson(@RequestBody String  param){
        log.info("Param:{}",param);
    }

}
