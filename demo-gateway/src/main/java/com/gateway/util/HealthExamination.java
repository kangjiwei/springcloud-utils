package com.gateway.util;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Author kangjiwei
 * @Date  2020/4/6.
 */
@Component
@Slf4j
public class HealthExamination implements IPing {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public boolean isAlive(Server server) {
        String url ="http://"+server.getId()+ "/isHealth";
        try{
            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
            if(forEntity.getStatusCode() == HttpStatus.OK){
                log.info("ping "+url+" success and response is "+forEntity.getBody());
                return true;
            }
            log.info("ping "+url+" error the response is "+forEntity.getBody());
        }catch (Exception e){
            log.error("ping "+url+" failed ");
            e.printStackTrace();
        }
        return false;
    }
}
