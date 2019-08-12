package com.nyd.capital.service.jx.util;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.service.dld.config.DldConfig;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author liuqiu
 */
public class DingdingUtil{

    @Autowired
    private  DldConfig dldConfig;
    @Autowired
    private static RedisTemplate redisTemplate;
    public final static String WEBHOOKTOKENURL = "https://oapi.dingtalk.com/robot/send?access_token=9440caab1be0e44a97bc457d5f137d3e5aaf6ae7e62645ef0797a0ae24277673";
    //public final static String WEBHOOKTOKENURL = ResourceBundle.getBundle("dld").getString("nyd.dd.talk");

    /**
     * 获取异常信息
     *
     * @param count 异常
     * @throws Exception
     */
    public static void getErrMsg(String count) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(WEBHOOKTOKENURL);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
//        StringWriter sw = new StringWriter();
//        e.printStackTrace(new PrintWriter(sw, true));
        map1.put("content", count);
        map2.put("msgtype", "text");
        map2.put("text", map1);
        String textMsg = JSON.toJSONString(map2);
        StringEntity se = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(se);
        try {
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                //System.out.println(result);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            //int a = 1 / 0;
            Integer test = null;
            test.equals(1);
        } catch (Exception e) {
            DingdingUtil.getErrMsg("这是一条测试数据：出错了！");
        }
    }

}
