package com.nyd.msg.service.channel;

import com.alibaba.fastjson.JSONObject;
import com.nyd.msg.service.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * 创蓝短信
 * @author san
 * @version V1.0
 */
@Slf4j
@Component
public class ChuangLanChannelStrategy implements ChannelStrategy{
    @Override
    public boolean sendSms(Message message, boolean batch) {
        log.info("创蓝短信sendSms参数日志 = {}", message);
        //短信下发
        String sendUrl = message.getSmsPlatUrl();
        Map map = new HashMap();
        map.put("account",message.getSmsPlatAccount());//API账号
        map.put("password",message.getSmsPlatPwd());//API密码
        map.put("msg",message.getSmsTemplate());//短信内容
        map.put("phone",message.getCellPhones());//手机号
        map.put("report","true");//是否需要状态报告
        map.put("extend",null);//自定义扩展码 可以增加手机号
        JSONObject js = (JSONObject) JSONObject.toJSON(map);
        log.info("创蓝返回日志 = {}" , sendSmsByPost(sendUrl,js.toString()));
        return true;
    }

    public static void main(String[] args) {

//        //查询余额
//        String balanceUrl = "https://xxx/msg/balance/json";
//        Map map1 = new HashMap();
//        map1.put("account","N*******");
//        map1.put("password","************");
//        JSONObject js1 = (JSONObject) JSONObject.toJSON(map1);
//        System.out.println(sendSmsByPost(balanceUrl,js1.toString()));
    }
    public static String sendSmsByPost(String path, String postContent) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            OutputStream os=httpURLConnection.getOutputStream();
            os.write(postContent.getBytes("UTF-8"));
            os.flush();
            StringBuilder sb = new StringBuilder();
            int httpRspCode = httpURLConnection.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
