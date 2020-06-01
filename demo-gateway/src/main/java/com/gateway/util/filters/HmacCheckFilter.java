/*

package com.gateway.util.filters;

import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


*/
/**
 * @Auther kangjw
 * @Date 2020/5/10.
 *//*


@Component
@Slf4j
public class HmacCheckFilter implements GlobalFilter, Ordered {

    public static final String PostBodyData = "PostBodyData";
    public static final String PostBodyByteData = "PostBodyByteData";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        Collection<List<String>> values = queryParams.values();
        Object[] objects = values.toArray();
        for (Object obj : objects) {
            log.info("种下的:{}", obj);
        }
        for (List<String> list : values) {
            list.forEach(li -> log.info(" 白雪 " + li));
        }
        String method = request.getMethodValue();
        String contentType = request.getHeaders().getFirst("Content-Type");
        log.info("Request Method:{}", method);

        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    try {
                        String bodyString = new String(bytes, "utf-8");
                        log.info("得到的路径：{}", bodyString);
                        log.info("靠到底是什么类型:{}", contentType);
                        if (contentType.startsWith("multipart/form-data")) {
                            String jsonStr = parseBodyInfo(bodyString);
                            log.info("排序之后的字符串:{}", jsonStr);
                        } else if (contentType.startsWith("application/json")) {
                            String jsonStr = parseBodyJson(bodyString);
                            log.info("json去空格:{}", jsonStr);
                        } else if (contentType.startsWith("application/x-www-form-urlencoded")) {
                            String encode = URLDecoder.decode(bodyString, "UTF-8");
                            encode = this.parseUrlEncoded(encode);
                            log.info("还是汉字吧，urlEncoded :{}", encode);
                        }
                        exchange.getAttributes().put("POST_BODY", bodyString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    DataBufferUtils.release(dataBuffer);
                    Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                        DataBuffer buffer = exchange.getResponse().bufferFactory()
                                .wrap(bytes);
                        return Mono.just(buffer);
                    });

                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                            exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return cachedFlux;
                        }
                    };
                    return chain.filter(exchange.mutate().request(mutatedRequest)
                            .build());
                });


*/
/* return chain.filter(exchange);*//*



  ServerRequest serverRequest = new DefaultServerRequest(exchange);

        // mediaType先判断请求类型和媒体类型，我们只对post请求且Content-Type是application/json的请求做特殊处理，请他的都放行
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        if(mediaType!=null) {
            log.info("PostBodyFilter3.....getType==={}", mediaType.getType());
            if ("POST".equals(exchange.getRequest().getMethodValue())*//*
*/
/*

*/
/* &&
                    (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)
                            || MediaType.APPLICATION_JSON_UTF8.isCompatibleWith(mediaType))*//*
*/
/*
*//*

*/
/*) {

                //post请求且Content-Type是application/json的继续走下面的modifiedBody（修改请求体）
            }else {//其他请求例如post请求（Content-Type是application/json），input,get,delete请求，不做特殊处理，直接放行。让后面的filter拿到原始数据

                log.info("PostBodyFilter3.....非post + [application/json]请求，不做特殊处理，直接放行~~~~~~");
                return chain.filter(exchange);//请求放行
            }
        }else {//mediaType为null的情况，基本上是非法的请求，直接返回错误提示给前端
            //设置status和body
            return Mono.defer(() -> {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);//设置status
                exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");//设置返回类型
                final ServerHttpResponse response = exchange.getResponse();
                byte[] bytes = "{\"code\":\"99999\",\"message\":\"非法访问,Content-Type不合法~~~~~~\"}".getBytes(StandardCharsets.UTF_8);

                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                response.getHeaders().set("aaa", "bbb");//设置header
                log.info("PostBodyFilter3拦截非法请求，没有检测到token............");

                return response.writeWith(Flux.just(buffer));//设置body
            });
        }

        // 修改请求体 read & modify body
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
            log.info("PostBodyFilter3.....原始length==="+body.length()+",内容==="+body);

            String method = exchange.getRequest().getMethodValue();
            if ("POST".equals(method)) {
//	        	if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
//
//					// origin body map
//					Map<String, Object> bodyMap = decodeBody(body);
//
//					//TODO
//
//					// new body map
//					Map<String, Object> newBodyMap = new HashMap<>();
//					return Mono.just(encodeBody(newBodyMap));
//				}
                //这里对application/json;charset=UTF-8的数据进行截获。
                if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)
                        || MediaType.APPLICATION_JSON_UTF8.isCompatibleWith(mediaType)) {
                    String newBody;
                    try {
                        newBody = body;//可以修改请求体
                    } catch (Exception e) {
                        return processError(e.getMessage());
                    }
                    log.info("PostBodyFilter3.....newBody长度==="+newBody.length()+",newBody内容===="+newBody);
                    exchange.getAttributes().put(PostBodyData, newBody);//为了向后传递，放入exchange.getAttributes()中，后面直接取

                    return Mono.just(newBody);
                }
            }
            log.info("PostBodyFilter3.....empty or just haha{}");
            return Mono.just(body);

        });
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());

        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {

            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {

                public HttpHeaders getHeaders() {
                    long contentLength = headers.getContentLength();
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(super.getHeaders());
                    if (contentLength > 0) {
                        httpHeaders.setContentLength(contentLength);
                    } else {
                        httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                    }
                    return httpHeaders;
                }

                public Flux<DataBuffer> getBody() {
                    return outputMessage.getBody();
                }
            };

            return chain.filter(exchange.mutate().request(decorator).build());
        }));*//*
*/
/*






       *//*

*/
/* ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String method = serverHttpRequest.getMethodValue();
        URI uri1 = serverHttpRequest.getURI();
        log.info(" uri:{}",uri1.toString());
        if ("POST".equals(method)) {

            Flux<DataBuffer> body = exchange.getRequest().getBody();

            body.subscribe(buffer -> {
                byte[] bytes = new byte[buffer.readableByteCount()];
                buffer.read(bytes);
                DataBufferUtils.release(buffer);
                InputStream inputStream = buffer.asInputStream();
                try {
                    inputStream.read(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(bytes.toString());
                try {
                    String bodyString = new String(bytes, "utf-8");
                    System.out.println(bodyString);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });*//*
*/
/*



           *//*

*/
/* //从请求里获取Post请求体
            String bodyStr = resolveBodyFromRequest(serverHttpRequest);
            log.info("请求体:{}",bodyStr);
            //TODO 得到Post请求的请求参数后，做你想做的事

            //下面的将请求体再次封装写回到request里，传到下一级，否则，由于请求体已被消费，后续的服务将取不到值
            URI uri = serverHttpRequest.getURI();
            ServerHttpRequest request = serverHttpRequest.mutate().uri(uri).build();
            DataBuffer bodyDataBuffer = stringBuffer(bodyStr);
            Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);

            request = new ServerHttpRequestDecorator(request) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return bodyFlux;
                }
            };*//*
*/
/*

        //封装request，传给下一级
       *//*

*/
/*     return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        } else if ("GET".equals(method)) {
            Map requestQueryParams = serverHttpRequest.getQueryParams();
            //TODO 得到Get请求的请求参数后，做你想做的事
            log.info("GET 请求体：{}",requestQueryParams.toString());

            return chain.filter(exchange);
        }*//*
*/
/*

       *//*

*/
/* return  chain.filter(exchange);*//*
*/
/*

    }


    private Map<String, Object> decodeBody(String body) {
        return Arrays.stream(body.split("&")).map(s -> s.split("="))
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
    }

    private String encodeBody(Map<String, Object> map) {
        return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
    }

    private Mono processError(String message) {
        *//*

*/
/*
		 * exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); return
		 * exchange.getResponse().setComplete();
		 *//*
*/
/*


        return Mono.error(new Exception(message));
    }

    *//*

*/
/**
     * 从Flux<DataBuffer>中获取字符串的方法
     *
     * @return 请求体
     *//*
*/
/*

    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        String retStr = null;
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            log.info("转成字符串：{}", charBuffer.toString());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return retStr;
    }

    private DataBuffer stringBuffer(String value) {
        byte[] bytes = null;
        if (!StringUtils.isEmpty(value)) {
            bytes = value.getBytes(StandardCharsets.UTF_8);
        } else {
            bytes = new byte[0];
        }
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }


    public static void main(String[] args) {
       *//*

*/
/* //Collator类是用来执行区分语言环境的String比较的，这里是选择CHINA
        Comparator comparator = Collator.getInstance(Locale.CHINA);
        String[] a = {"乔飞", "687","乔巴", "路飞","Cat", "张无忌", "cat","小龙女", "123","史蒂芬","HeHe"};
        //使根据指定比较器产生的顺序对指定对象数组进行排序
        Arrays.sort(a, comparator);
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }*//*
*/
/*

        String[] arr = {"0", "1", "2", "3", "4", "5"};
        String str3 = org.apache.commons.lang.StringUtils.join(arr);

        System.out.println(str3); // 012345
        String str4 = org.apache.commons.lang.StringUtils.join(arr, ","); // 数组转字符串(逗号分隔)(推荐)
        System.out.println(str4); // 0,1,2,3,4,5

    }

    *//*

*/
/**
     * form-data 排序
     *
     * @param bodyInfo
     * @return
     *//*
*/
/*

    public String parseBodyInfo(String bodyInfo) {
        if (bodyInfo != null && bodyInfo.length() > 0) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(bodyInfo);
            log.info(m.replaceAll(""));
            bodyInfo = m.replaceAll("");
        }
        Map<String, String> retMap1 = splitInfo(bodyInfo);
        Set<String> keys = retMap1.keySet();
        List<String> formDatas = new ArrayList<>();
        keys.stream().forEach(key -> {
            formDatas.add(retMap1.get(key));
        });
        Object[] objects = formDatas.toArray(new String[formDatas.size()]);
        Comparator comparator = Collator.getInstance(Locale.CHINA);
        Arrays.sort(objects, comparator);
        String retStr = org.apache.commons.lang.StringUtils.join(objects);
        return retStr;
    }


*/
/**
     * json 去掉空格
     *
     * @param bodyInfo
     * @return
     *//*


    public String parseBodyJson(String bodyInfo) {
        Map<String, String> retMap = new HashMap<>();
        if (bodyInfo != null && bodyInfo.length() > 0) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(bodyInfo);
            log.info(m.replaceAll(""));
            bodyInfo = m.replaceAll("");
        }
        return bodyInfo;
    }

    public Map<String, String> splitInfo(String bodyInfo) {
        Map<String, String> retMap = new HashMap<>();
        String[] names = bodyInfo.split("name=");
        for (String name : names) {
            //log.info(name);
            if (name.indexOf("----------------------------") > 0) {
                String[] split = name.split("----------------------------");
                if (split.length > 0) {
                    String sp = split[0];
                    //log.info(" 可谓:{} ", sp);
                    String[] split1 = sp.split("\"");
                    String keyStr = split1[1];
                    String valStr = sp.substring(keyStr.length() + 2, sp.length());
                    retMap.put(keyStr, valStr);
                }
            }
        }
        return retMap;
    }


*/
/**
     * 解析 x-www-urlEncoded
     *
     * @return
     *//*


    public String parseUrlEncoded(String bodyInfo) {
        String[] infos = bodyInfo.split("&");
        List<String> allVal = new ArrayList();
        for (String info : infos) {
            String[] split = info.split("=");
            allVal.add(split[1]);
        }
        Object[] objects = allVal.toArray();
        //汉字排序顺序
        Comparator comparator = Collator.getInstance(Locale.CHINA);
        Arrays.sort(objects, comparator);
        String retStr = org.apache.commons.lang.StringUtils.join(objects);
        return retStr;
    }


    @Override
    public int getOrder() {
        return -10;
    }
}

*/
