package com.example.demo.controller;

import lombok.Cleanup;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by rongrong on 2020/4/6.
 */
@RestController
public class MyController {

    @GetMapping("/hello")
    public String ayHello() {
        File file = null;
        Map<String, String> map = new HashMap<>();
        try {

            //第一种方案
            /*String path = MyController.class.getResource("/").getPath();
            path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            path.replace(".jar!/BOOT-INF/classes!/","")
            System.out.println("文件路径  "+  path);
            Properties pro = new Properties();
            @Cleanup FileInputStream in = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(in, "GBK");
            pro.load(reader);
            String codeInfo = pro.get("200") + "";
            System.out.println( "笑话把 "+ codeInfo);*/

            //第二种，乱码
            InputStream stream =getClass().getClassLoader().getResourceAsStream("msg.properties");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            String  vals[] = null;
            while ((line = br.readLine()) != null) {
                vals = line.split("=");
                map.put(vals[0],vals[1]);
            }
            System.out.println("文件路径  " + map.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
       // InputStream resource = MyController.class.getResourceAsStream("/library/a.txt");

        return "Hello 1013!";
    }

    @GetMapping("/hello1")
    public String ayHello2() {
        return "Hello 10131!";
    }

    @GetMapping("/hello5")
    public String ayHello5() {
        return "Hello 10135!";
    }

    @GetMapping("/hello3")
    public String ayHello3() {
        return "Hello 10133!";
    }

    @GetMapping("/hello4")
    public String ayHello4() {
        return "Hello 10134!";
    }
}
