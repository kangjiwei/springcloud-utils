package com.gateway.util.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.gateway.util.DCFRespMsg;
import com.gateway.util.cache.GuavaCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Map;

import static com.gateway.util.CodeEnum.*;

/**
 * @Author kangjw
 * @Date 2020/4/9
 * @Describe Token 生成 和 解析
 */
@Slf4j
@Component
public class TokenManager {

    private static final String SECRET = "NiNjjfasdfgsdfgsdasdf123123DFSDF1235646dfasdfasdf23SSSSS<>?)(*&^%$#@!_+?>KUIY";

    private final int EXPIRESSTRING = 1 * 60 * 60 * 1000;//有效期15分钟

    private final String ISSUSER = "iss";

    private final String AUD = "aud";

    private final String CURRENTUSER = "currentUser";

    private final String USERID = "user_id";

  /*  @Autowired
    UserLoginServiceImpl userLoginService;
*/
    @Autowired
    DCFRespMsg dcfRespMsg;

    @Autowired
    GuavaCache guavaCache;


    /**
     * 生成token放入session
     *
     * @param accountName
     */
    public String createToken(String accountName, String userId) {
        Date currDate = new Date();
        currDate.setTime(currDate.getTime() + EXPIRESSTRING);
        String token = JWT.create()
                .withClaim(ISSUSER, "zlrUser") // 载荷 payload 签发者
                .withClaim(AUD, "All")//面向的用户
                .withClaim(CURRENTUSER, accountName)
                .withClaim(USERID, userId)
                .withExpiresAt(currDate)//设置过期时间
                .withIssuedAt(new Date())//设置签发时间
                .sign(Algorithm.HMAC256(SECRET));//验签singtrue加密
        log.info("Successful generation of Token at gateway.");
        return token;
    }

    /**
     * 验证Token信息
     *
     * @param token
     * @return
     */
    public DCFRespMsg verrifyToken(String token) {


        JWTVerifier verify = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        Claim userId = null, accountName = null;
        try {
            jwt = verify.verify(token);
            Map<String, Claim> getMap = jwt.getClaims();
            userId = getMap.get(USERID);
            accountName = getMap.get(CURRENTUSER);
        } catch (TokenExpiredException t) {
            log.error("当前token已经超时" + t.getMessage());
            return this.isFrequently(token);
        } catch (IllegalArgumentException i) {
            log.error("非法Token!" + i.getMessage());
            return dcfRespMsg.setCode(TOKENINVALI);
        } catch (JWTDecodeException j) {
            log.error("Token信息不完整" + j.getMessage());
            return dcfRespMsg.setCode(FAILED);
        } catch (Exception e) {
            log.error("Token已经过期，请重新登陆：" + e.getMessage(), e);
            return dcfRespMsg.setCode(FAILED);
        }
        log.info("Token verification succeeded!");
        log.info(" get account name:" + accountName.asString());
        dcfRespMsg.addContent("userId", userId.asString() + "");
        dcfRespMsg.addContent("accountname", accountName.asString());
        //刷新踪迹记录
        guavaCache.get(token);
        return dcfRespMsg.setCode(SUCCESS);
    }

    /**
     * 当Token过期的时候，需要判断用户操作是否频繁，是，则生成新的额Token。
     *
     * @return
     */
    public DCFRespMsg isFrequently(String tokenInfo) {
        Map<String, String> userInfoMap = guavaCache.get(tokenInfo);
        if (!StringUtils.isEmpty(userInfoMap)) {
            String userName = userInfoMap.get(guavaCache.USERNAME);
            String userId = userInfoMap.get(guavaCache.USERID);
            if (StringUtils.isEmpty(guavaCache.get(tokenInfo))) {
                String newToken = createToken(userName, userId);
                guavaCache.put(newToken, userName, userId);
                return dcfRespMsg.reset().setCode(SUCCESS).setToken(newToken);
            } else {
                log.info("the Token is Empty :" + tokenInfo);
            }
        }
        return dcfRespMsg.reset().setCode(NEWTOKEN);
    }

}
