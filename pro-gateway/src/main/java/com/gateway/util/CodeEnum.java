package com.gateway.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2020/4/10.
 */
public enum CodeEnum {

    SUCCESS(200),
    NOAUTHO(3009),
    ACCOUNTORPWDINVAIL(4001),
    NOACCOUNT(3007),
    TOKENTIMEOUT(3003),
    TOKENINVALI(3004),
    LOGINTIMEOUT(3008),
    HYSTRIX(3010),
    USERISDISABLED(3011),
    NEWTOKEN(5000),
    FAILED(-1);

    private int nCode;
    private String msg;

    private CodeEnum(int _nCode) {
        this.nCode = _nCode;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public int VALUE() {
        return nCode;
    }

    public String  getVals(String ks){
         Map<String, String> map = new HashMap<String, String>() {
            {
                put("200", "登陆成功");
                put("-1", "ORM exception");
                put("3002", "登陆超时");
                put("3003", "Token超时");
                put("3004", "Token异常");
                put("3005", "mising hmac check");
                put("3006", "invalid hmac check");
                put("3008", "登陆超时，请重新登陆");
                put("3009", "没有权限");
                put("3010", "请求超时或者是接口不通");
                put("3011", "当前用户已被禁用,请联系管理员");
                put("4001", "账号或者密码错误");
                put("5000", "新的Token");
            }
        };
        return  map.get(ks);
    }
}
