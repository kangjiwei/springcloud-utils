package com.gateway.util.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gateway.util.CodeEnum;
import com.gateway.util.DCFRespMsg;
import com.gateway.util.token.TokenManager;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.sun.istack.Nullable;
import com.sun.jndi.toolkit.url.Uri;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.hibernate.Session;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultClientResponse;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/**
 * @Author kangjiwei
 * @Date 2020/4/7.
 * @Describe 权限验证拦截器
 */

@Slf4j
@Component
public class PowerCheckFilter implements GlobalFilter, Order {

  /*  @Autowired
    UserLoginServiceImpl userLoginService;*/

    @Value("${authservice.url}")
    public String authUrl;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    DCFRespMsg dcfRespMsg;

    @Autowired
    RestTemplate restTemplate;

    public static final String USERID = "userId";

    public static final String REQURI = "uri";

    public static final String REQREQUEST = "method";

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        ServerWebExchange build = null;
        ServerHttpRequest request = serverWebExchange.getRequest();
        ServerHttpResponse response = serverWebExchange.getResponse();
        log.info("网关--request-Path:{}", request.getPath());
        log.info("网关--response-code:{}",response.getStatusCode());
        try {
             HttpHeaders headers = request.getHeaders();
            String methodValue = serverWebExchange.getRequest().getMethodValue();
            log.info(methodValue);
          /*  if (StringUtils.isEmpty(headers.get("token"))) {
                log.debug("网关--没有得到Token信息！");
                return null;
            }
            List<String> headersList = headers.get("token");
            String token = headersList.get(0);
            log.debug("网关--得到Token信息：" + token);
*/
            //验证Token
            /*DCFRespMsg dcf = tokenManager.verrifyToken(token);

            if (dcf.getRetCode() != 200) {
                log.info("网关--Token验证失败!");
                return this.bingoResponse(serverWebExchange, dcf);
            }*/
            //验证权限
            /*boolean retValidation = this.isValidation(request, dcf);
            if (!retValidation) {
                log.info("Authority authentication failed！");
                return this.bingoResponse(serverWebExchange, dcfRespMsg.setCode(CodeEnum.NOAUTHO));
            }*/

           /* Object userId = dcf.getContents().get(USERID);
            Object accountname = dcf.getContents().get("accountname");
            ServerHttpRequest host = request.mutate().header(USERID, StringUtils.isEmpty(userId) ? "" : userId + "").build();
            host = host.mutate().header("accountname", StringUtils.isEmpty(accountname) ? "" : accountname + "").build();
            //将现在的request 变成 change对象
            build = serverWebExchange.mutate().request(host).build();*/

           /* response.getHeaders().add("newToken", "12312312");
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            response.getHeaders().add("konw", "卸料");*/
     /*   dcfRespMsg.reset().setCode(CodeEnum.NEWTOKEN);
        byte[] bytes = JSON.toJSONBytes(dcfRespMsg);
        DataBuffer wrap = response.bufferFactory().wrap(bytes);*/
            serverWebExchange = serverWebExchange.mutate().response(response).request(request).build();
        } catch (Exception e) {
            log.error("Authority authentication error:" + e.getMessage(),e);
             return bingoResponse(serverWebExchange, dcfRespMsg.setCode(CodeEnum.NOAUTHO));
        }

        return gatewayFilterChain.filter(serverWebExchange);
    }

    /**
     * 是否有权限
     *
     * @param request
     * @return
     */
    public boolean isValidation(ServerHttpRequest request, DCFRespMsg dcf) {
        try {
            Object dcgInfo = dcf.getContents().get(USERID);
            //设置api接口
            String url = authUrl + "v2/permission/check";
            //设置请求头
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            List<String> list = new ArrayList<>();
            list.add(dcgInfo + "");
            headers.put(USERID, list);

            //设置参数
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put(REQURI, request.getURI().getPath());
            jsonMap.put(REQREQUEST, request.getMethodValue());

            //访问验证接口
            HttpEntity request1 = new HttpEntity(jsonMap, headers);
            ResponseEntity<Object> response = restTemplate.postForEntity(url, request1, Object.class);
            Object authObj = response.getBody();
            String jsonStr = JSONObject.toJSONString(authObj);
            Map retObject = JSON.parseObject(jsonStr, Map.class);

            String retCode = retObject.get("retCode") + "";
            if (retCode.equals("200")) {
                log.info("Authority authentication succeeded！");
                return true;
            }
        } catch (Exception e) {
            log.error("(后台权限系统)Authority serious error/" + e.getMessage(), e);
            return false;
        }
        return false;
    }

    /**
     * 在过滤链中执行执行顺寻
     *
     * @return
     */
    @Override
    public int value() {
        return -10;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }


    public Mono<Void> bingoResponse(ServerWebExchange swe, DCFRespMsg dcfRespMsg) {
        ServerHttpResponse response = swe.getResponse();
        try {
            swe.mutate().response(response).build();
            dcfRespMsg.reset().setCode(CodeEnum.NOAUTHO);
            byte[] bytes = JSON.toJSONBytes(dcfRespMsg);
            DataBuffer wrap = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(wrap));
        } catch (Exception e) {
            log.error("设置");
        }
        return null;
    }


}
