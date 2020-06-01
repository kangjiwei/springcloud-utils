package com.gateway.service;

import com.gateway.entity.Sys_dept;
import com.gateway.entity.Sys_user_info;
import com.gateway.repository.DeptRepository;
import com.gateway.repository.UserRepository;
import com.gateway.service.interfaces.IUserloginService;
import com.gateway.util.DCFRespMsg;
import com.gateway.util.cache.GuavaCache;
import com.gateway.util.token.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.gateway.util.CodeEnum.*;

/**
 * @Atuhor kangjw
 * @Date 2020/4/9
 */
@Slf4j
@Service
public class UserLoginServiceImpl implements IUserloginService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    DeptRepository deptRepository;

    @Autowired
    DCFRespMsg dcfRespMsg;

    @Autowired
    GuavaCache guavaCache;

    @Override
    public DCFRespMsg userLogin(String userName, String userPwd) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(userPwd)) {
            log.debug("Incomplete login information！");
            return dcfRespMsg.setCode(ACCOUNTORPWDINVAIL);
        }
        List<Sys_user_info> byUserNames = userRepository.findByAccountnameAndAccountpass(userName, userPwd);
        if (byUserNames.size() > 0) {
            Sys_user_info userInfo = byUserNames.get(0);
            if (userInfo.getStatus().equals("1") || userInfo.getDel_flag().equals("2")) {
                log.info("The user（" + userName + "） has been disabled");
                return dcfRespMsg.reset().setCode(USERISDISABLED);
            }
            Sys_dept dept_id = userInfo.getDept_id();
            if (!StringUtils.isEmpty(dept_id) && !StringUtils.isEmpty(dept_id.getAncestors())) {
                String[] split = dept_id.getAncestors().split(",");
                List<Sys_dept> byDept_idIn = deptRepository.queryDept(split);
                for (Sys_dept dept : byDept_idIn) {
                    if (dept.getDel_flag().equals("2")) {
                        log.info("The user（" + userName + "） has been disabled");
                        return dcfRespMsg.reset().setCode(USERISDISABLED);
                    }
                }
            }
            log.info("Login successfully!");
            String token = tokenManager.createToken(userName, userInfo.getUser_id() + "");
            guavaCache.put(token, userName, userInfo.getUser_id() + "");
            return dcfRespMsg.reset().setCode(SUCCESS).setToken(token);
        }
        log.info("Login failed!");
        return dcfRespMsg.reset().setCode(FAILED);
    }

    @Override
    public Sys_user_info findUserIdByAccount(String account) {
        Sys_user_info userInfo = userRepository.findUserIdByAccountname(account);
        return userInfo;
    }


}
