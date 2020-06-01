package com.gateway.util.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ObjectArrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author kangjw
 * @Date 2020/4/14
 * @Describe GuavaCahe
 */
@Slf4j
@Component
public class GuavaCache {

    //若用户在规定时间内，没有更新Cache则视为Token过期.
    private static Cache<String, Map<String,String>> cacheMap = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.MINUTES).build();

    private Map<String, String> contentMap = new HashMap<>();

    public final String USERNAME = "userName";

    public final String USERID = "userId";

    //缓存新增
    public boolean put(String keys, String userName, String userId) {
        log.debug("GuavaCache Start recording footsteps!");
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(userId)) {
            return false;
        }
        this.contentMap.put(USERNAME, userName);
        this.contentMap.put(USERID, userId);
        try {
            cacheMap.put(keys, this.contentMap);
            log.info("缓存新增成功！");
        } catch (Exception e) {
            log.error("缓存--Put出现错误: " + e.getMessage());
            return false;
        }
        log.info(this.get(keys).toString());
        log.debug("keys "+keys);
        return true;
    }


    //缓存查询
    public Map<String,String> get(String keys) {
        Map<String,String> retMap = null;
        log.debug("GuavaCache Flush footsteps!");
        try {
            retMap = cacheMap.get(keys,()->{
                Map<String,String> map
                        = new HashMap<String, String>();
                return map;
            });
        } catch (Exception e) {
            log.error("查询缓存出现错误:" + e.getMessage());
            return null;
        }
        return retMap;
    }

}
