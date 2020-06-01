package com.gateway.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gateway.entity.Sys_dept;
import com.gateway.entity.Sys_user_info;
import com.gateway.service.UserLoginServiceImpl;
import com.gateway.util.CodeEnum;
import com.gateway.util.DCFRespMsg;
import com.gateway.util.cache.GuavaCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.gateway.util.CodeEnum.SUCCESS;

/**
 * @Author kangjiwei
 * @Date 2020/4/8.
 * @Desctribute 用户信息的处理
 */

@Slf4j
@CrossOrigin
@Controller
@RequestMapping("/loginControl")
public class LoginController {

    @Autowired
    UserLoginServiceImpl userLoginServices;

    @Autowired
    DCFRespMsg dcfRespMsg;


    /**
     * 登录系统
     */
    @PostMapping("/userLogin")
    @ResponseBody
    public DCFRespMsg userLogin(@RequestBody String jsonStr) {
        Map<String, String> infoMap = JSONArray.parseObject(jsonStr, Map.class);
        try {
            return dcfRespMsg = userLoginServices.userLogin(infoMap.get("userName"), infoMap.get("userPwd"));
        } catch (Exception e) {
            log.error("登录出现异常{}", e);
        }
        return dcfRespMsg;
    }


}
