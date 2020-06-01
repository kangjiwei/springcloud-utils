package com.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.gateway.entity.Sys_dept;
import com.gateway.entity.Sys_user_info;
import com.gateway.repository.DeptRepository;
import com.gateway.repository.UserRepository;
import com.gateway.util.DCFRespMsg;
import com.gateway.util.cache.GuavaCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.net.*;
import lombok.Cleanup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.io.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

@SpringBootTest
/*@RunWith(SpringRunner.class)*/
@Slf4j
public class DemoGatewayApplicationTests {

    //有效期15分钟
    private final int EXPIRESSTRING = 3 * 60 * 1000; //15 * 60 * 1000;

    private final String SECRET = "zlrKeJiGongSi";

    private final String ISSUSER = "iss";

    private final String AUD = "aud";

    private final String CURRENTUSER = "currentUser";

    private final String USERID = "user_id";

    @Autowired
    UserRepository userRepository;


    @Autowired
    DCFRespMsg msg;


    @Value("${authservice.url}")
    public String url;


    @Test
    public void testFeign() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:10113/myController/wo/buti";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", "xiaoshuai");
        map.add("pwd", "password");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println(" 真正的难受 " + response.getBody());
    }

    @Test
    public void createJwt() {
        String userName = "admin";
        Map<String, Object> map = new HashMap();
        Date currDate = new Date();
        LocalDateTime curDate = LocalDateTime.now();
        currDate.setTime(currDate.getTime() + EXPIRESSTRING);
        String token = JWT.create()
                .withHeader(map)//头部 header
                .withClaim(ISSUSER, "zlrUser") // 载荷 payload 签发者
                .withClaim(AUD, "All")//面向的用户
                .withClaim(CURRENTUSER, userName)//自定义的key和value
                .withClaim(USERID, "11")
                .withExpiresAt(currDate)//设置过期时间
                .withIssuedAt(new Date())//设置签发时间
                .sign(Algorithm.HMAC256(SECRET));//验签singtrue加密
        System.out.println(" Token生成成功 " + token);
        System.out.println("================================================================");
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("当前时间  " + localDateTime.toString());
        LocalDateTime localDateTime1 = localDateTime.minusMinutes(3);
        System.out.println(" 三分钟后  " + localDateTime1.toString());
        System.out.println("开始测试Token生命周期---->;");
        this.testToken(token, curDate.minusMinutes(-3));
    }


    public void testToken(String token, LocalDateTime lastDateTime) {
        GuavaCache cache = new GuavaCache();
        cache.put("admin", "ss", "sss");
        boolean isExe = true;
        JWTVerifier verify = JWT.require(Algorithm.HMAC256(SECRET)).build();
        Map<String, String> admin = cache.get("admin");
        while (isExe) {


            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                DecodedJWT jwt = verify.verify(token);
            } catch (TokenExpiredException t) {
                isExe = false;
            } catch (IllegalArgumentException i) {
                isExe = false;
                System.out.println("非法Token");
            } catch (Exception e) {
                isExe = false;
                System.out.println("登录凭证已过去，请重新登录");
            }
            if (isExe) {
                System.out.println(" Token正常！");
            } else {
                System.out.println(" Token 已经过期了 ！");
                if (admin == null) {
                    System.out.println("缓存已经清空了");
                } else {
                    if (StringUtils.isEmpty(cache.get("admin"))) {
                        System.out.println("缓存存在! ");
                        isExe = true;
                        System.out.println("需要生成新的Token！");
                    } else {
                        System.out.println("缓存已经不存在了");
                    }
                }
            }
            System.out.println("验证Token(" + LocalDateTime.now().toString() + "),将在 (" + lastDateTime.toString() + ") 后过期。");

        }
    }


    @Test
    public void verityToken() {
        try {
            java.lang.Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JWTVerifier verify = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verify.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJBbGwiLCJjdXJyZW50VXNlciI6ImFkbWluIiwidXNlcl9pZCI6IjExIiwiaXNzIjoiemxyVXNlciIsImV4cCI6MTU4NjU2ODY1MywiaWF0IjoxNTg2NTY4MzUzfQ.1DIe2XniSEdOPxsVANpwhIAC0oQsn50Ce_ylAPxjxC4");
        } catch (TokenExpiredException t) {
            System.out.println("当前token已经超时~");
        } catch (IllegalArgumentException i) {
            System.out.println("非法Token");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("登录凭证已过去，请重新登录");
        }
        Map<String, Claim> getMap = jwt.getClaims();
        Claim curUser = getMap.get(CURRENTUSER);
        Claim userId = getMap.get(USERID);
        System.out.println(" 当期用户 " + curUser.asString());
        System.out.println("用户id " + userId.asString());
    }

    @Test
    public void testPost() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://192.168.12.103:10021/v2/permission/check";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            //设置参数信息
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("uri", "/uri/ss");
            map.add("method", "POST");
            map.add("userId", StringUtils.isEmpty("11") ? "" : 11 + "");
            //访问验证接口
            HttpEntity<MultiValueMap<String, String>> request1 = new HttpEntity<>(map, headers);
            ResponseEntity<Object> response = null;

            response = restTemplate.postForEntity(url, request1, Object.class);
            Object authObj = response.getBody();
            String jsonStr = JSONObject.toJSONString(authObj);
            Map retObject = JSON.parseObject(jsonStr, Map.class);
            System.out.println(retObject.toString());
            String retCode = retObject.get("code") + "";
            if (retCode.equals("200")) {
                System.out.println("请求成功！");
            } else {
                System.out.println("请求失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Autowired
    DeptRepository deptRepository;

    @Test
    public void testJpa() {
        List<Sys_user_info> user_infoList = userRepository.findByAccountnameAndAccountpass("kangjw", "e10adc3949ba59abbe56e057f20f883e");
        System.out.println(user_infoList.get(0).getAccountname());
        Sys_dept dept_id = user_infoList.get(0).getDept_id();
        if (dept_id.getStatus().equals("1") || dept_id.getDel_flag().equals("1")) {
            System.out.println("当前已经禁用或者已经被删除~");
        }
        System.out.println("不等于空吗 " + dept_id.getAncestors());
        if (!StringUtils.isEmpty(dept_id.getAncestors())) {
            String[] split = dept_id.getAncestors().split(",");
            List<Sys_dept> byDept_idIn = deptRepository.queryDept(split);
            if (byDept_idIn.size() > 0) {
                System.out.println("祖父部门查询成功");
            } else {
                System.out.println("未查询到祖父部门信息！");
            }

            byDept_idIn.forEach(dept -> System.out.println(dept.getDept_name()));

        }

    }


    @Autowired
    GuavaCache cache;


    @Test
    public void testCache() {

/*        boolean boo = cache.put("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJBbGwiLCJjdXJyZW50VXNlciI6InNoYW5zaGFuIiwidXNlcl9pZCI6IjU2MCIsImlzcyI6InpsclVzZXIiLCJleHAiOjE1ODY5MDk3MTUsImlhdCI6MTU4Njg2NjUxNX0.lYntyYa508Mb9A0H1QbEd9aHB82KlXq9P54DRsyi-Zk.", "userName", "userId");
        if (boo) log.info("缓存新增成功！");
        else log.info("缓存增失败！{}");
        Map<String, String> map = cache.get("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJBbGwiLCJjdXJyZW50VXNlciI6InNoYW5zaGFuIiwidXNlcl9pZCI6IjU2MCIsImlzcyI6InpsclVzZXIiLCJleHAiOjE1ODY5MDk3MTUsImlhdCI6MTU4Njg2NjUxNX0.lYntyYa508Mb9A0H1QbEd9aHB82KlXq9P54DRsyi-Zk.");
        log.info("查询结果" + map.toString());

        File f = new File(this.getClass().getResource("/").getPath());
        try {
            File file = ResourceUtils.getFile("classpath:msg.properties");
            System.out.println(file.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("相对路径 " + f.getAbsolutePath());*/

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map map = new HashMap();
        map.put("one", "val");
        HttpEntity entity = new HttpEntity(map, httpHeaders);
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity("http://localhost:1011/appJson", entity, Object.class);
        System.out.println(objectResponseEntity.toString());
    }


}
