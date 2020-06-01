package com.example.demo;


import com.example.demo.util.MyDay;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest

public class Provider1013ApplicationTests {

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
	@Test
	public void contextLoads() {
		test(MyDay.MONDAY);
	}

	public void  test(MyDay myDay){
		EnumMap<MyDay, Object> enumMap = new EnumMap<>(MyDay.class);
		Object o = enumMap.get(myDay);
		System.out.println(o);
		/*MyDay monday = myDay;
		int code = monday.getCode();
		System.out.println(code);
		System.out.println(monday.getName());*/

	}


}
