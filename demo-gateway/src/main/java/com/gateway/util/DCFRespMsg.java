package com.gateway.util;

import lombok.Cleanup;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
@Data
@ToString
public class DCFRespMsg {

    private int retCode;
    private String message;
    private String token;
    private HashMap<String, Object> contents = new HashMap<>();

    /**
     * 设置code
     *
     * @param code
     * @return
     */
    public DCFRespMsg setCode(CodeEnum code) {
        try {
            /*InputStream inputStream = getClass().getClassLoader().getResourceAsStream("msg.properties");
            BufferedReader   bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"gbk"));
            char[] ch = new char[1024];
            int index = bufferedReader.read(ch);
            System.out.println(new String(ch,0,index));
            Properties pro = new Properties();
            pro.load(bufferedReader);
            String codeInfo = pro.get(code + "") + "";
            System.out.println(" 哈哈  "+codeInfo);
            if (codeInfo.equals("null")) {
                log.debug("未查询对应的Code状态!");
            }
*/
            /*
            Properties pro = new Properties();
            @Cleanup FileInputStream in = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(in, "GBK");
            pro.load(reader);
            String codeInfo = pro.get(code + "") + "";
            if (codeInfo.equals("null")) {
                log.debug("未查询对应的Code状态!");
            }*/

            this.setRetCode(Integer.parseInt( code+ ""));
            this.setMessage(code.getVals(code+""));
            log.info(this.retCode + "  "+ message);
        } catch (Exception e) {
            log.debug("读取资源文件失败~", e.getMessage());
            e.printStackTrace();
        }
        return this;
    }


    /**
     * 设置token
     *
     * @param tokenInfo
     * @return
     */

    public DCFRespMsg setToken(String tokenInfo) {
        this.token = tokenInfo;
        return this;
    }

    /**
     * 相应信心主题信息
     *
     * @param keys
     * @param vals
     * @return
     */
    public DCFRespMsg addContent(String keys, String vals) {
        this.contents.put(keys, vals);
        return this;
    }

    public DCFRespMsg reset() {
        this.retCode = 0;
        this.token = null;
        this.contents.clear();
        return this;
    }



}
