package com.gateway.service.interfaces;


import com.gateway.entity.Sys_user_info;
import com.gateway.util.DCFRespMsg;

/**
 * @Author kangjw
 * @Date 2020/4/9.
 * @Describe  接口
 */
public interface IUserloginService {

    DCFRespMsg userLogin(String userName, String userPwd);

    Sys_user_info findUserIdByAccount(String account);

}
