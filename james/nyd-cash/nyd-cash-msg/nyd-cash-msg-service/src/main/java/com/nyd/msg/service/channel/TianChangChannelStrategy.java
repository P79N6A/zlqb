package com.nyd.msg.service.channel;

import com.nyd.msg.service.utils.DesUtil;
import com.nyd.msg.service.utils.Message;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TianChangChannelStrategy implements ChannelStrategy{
    Logger logger = LoggerFactory.getLogger(TianChangChannelStrategy.class);


    @Override
    public boolean sendSms(Message vo, boolean batch) {
        String username = vo.getSmsPlatAccount();
        String password = vo.getSmsPlatPwd();
        String url = vo.getSmsPlatUrl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStamp = sdf.format(new Date());
        // 短信相关的必须参数

          String  mobile = vo.getCellPhones(); //天畅的batch用分号间隔


        String message = vo.getSmsTemplate();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(username.getBytes("utf8"));
            md5.update(DesUtil.decrypt(password).getBytes("utf8"));
            md5.update(timeStamp.getBytes("utf8"));
            md5.update(message.getBytes("utf8"));
            password = Base64.encodeBase64String(md5.digest());
            password = URLEncoder.encode(password,"utf8");
            message = URLEncoder.encode(message, "utf8");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        // 装配GET所需的参数
        StringBuilder sb = new StringBuilder();
        sb.append(url)
                .append("?un=").append(username)
                .append("&dc=").append(15)
                .append("&pw=").append(password)
                .append("&ts=").append( timeStamp )
                .append("&da=").append( mobile )
                .append("&tf=").append(3)
                .append("&rf=").append(2)
                .append("&sm=").append(message);

        String request = sb.toString();
        // 以GET方式发起请求
        String result = null;
        try {
            result = Request.Get( request ).execute().returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

       if(result.contains("id")){
            logger.info("天畅发送结果成功:"+result);
            return true;
       }else {
           logger.error("天畅发送结果出错"+result);
           return false;
       }
    }



}
