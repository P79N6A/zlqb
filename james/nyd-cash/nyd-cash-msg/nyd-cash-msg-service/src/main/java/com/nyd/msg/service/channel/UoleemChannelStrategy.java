package com.nyd.msg.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.nyd.msg.model.SmsResponse;
import com.nyd.msg.model.SmsUoleemResponse;
import com.nyd.msg.service.code.DaHanResultEnum;
import com.nyd.msg.service.code.UoleemResultEnum;
import com.nyd.msg.service.utils.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by leej on 2019/02/25.
 */
@Component
public class UoleemChannelStrategy implements ChannelStrategy {

    private static Logger logger = LoggerFactory.getLogger(UoleemChannelStrategy.class);
    // 请求超时时间(毫秒) 5秒
    public static int HTTP_REQUEST_TIMEOUT	= 5 * 1000;

    // 响应超时时间(毫秒) 60秒
    public static int HTTP_RESPONSE_TIMEOUT	= 60 * 1000;

    @Override
    public boolean sendSms(Message message, boolean batch) {
        try {
            String commonLog = "【UoleemChannel】";
            logger.info("{}发送短信，参数【{},{}】",commonLog,JSON.toJSONString(message),batch);
            UoLeemMessage uoLeemMessage = new UoLeemMessage();
            // 必填参数
            if(StringUtils.isNotBlank(message.getCellPhones())) {
                String phones = message.getCellPhones();
                // 如果是批量发送
                if(phones.indexOf(":") !=  -1) {
                    phones = StringUtils.replace(phones,":",",");
                    message.setCellPhones(phones);
                }
            }

            uoLeemMessage.setUsername(message.getSmsPlatAccount());
            encryptPsw(message.getSmsPlatPwd(),message.getSmsPlatAccount(),uoLeemMessage);
            uoLeemMessage.setMobile(message.getCellPhones());
            uoLeemMessage.setContent(message.getSmsTemplate());
            // 选填参数
            uoLeemMessage.setResptype("json");
            // 是否接收短信的状态和用户回复短信的内容
            //uoLeemMessage.setNeedstatus(true);
            uoLeemMessage.setProduct("124363868");
            Map<String, Object> productMap = BeansUtils.transforObjectToMap(uoLeemMessage);
            String resultStr = sendPost(message.getSmsPlatUrl(), productMap, "UTF-8");
            if(StringUtils.isBlank(resultStr)) {
                return Boolean.FALSE;
            } else {
                SmsUoleemResponse response = JSON.parseObject(resultStr, new TypeReference<SmsUoleemResponse>(){});
                if(DaHanResultEnum.SUCCESS.getCode().equals(response.getCode())) {
                    logger.info("{}短信发送成功", commonLog);
                    return Boolean.TRUE;
                } else {
                    if(StringUtils.isNotBlank(response.getCode())) {
                        logger.info("{}短信发送失败，失败原因：{}", commonLog, UoleemResultEnum.getMsgByCode(response.getCode()));
                    } else {
                        logger.info("{}短信发送失败", commonLog);
                    }
                    return Boolean.FALSE;
                }
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param params
     * @param charset
     * @return
     */
    public String sendPost(String url, Map<String, Object> params, String charset) {
        // 设置超时
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(HTTP_RESPONSE_TIMEOUT)
                .setConnectTimeout(HTTP_REQUEST_TIMEOUT)
                .setConnectionRequestTimeout(HTTP_REQUEST_TIMEOUT)
                .build();
        // 获取连接客户端工具
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
        String entityStr = "";
        CloseableHttpResponse response = null;
        try {
            // 创建POST请求对象
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(defaultRequestConfig);
            // 创建请求参数
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value.toString()));
                    }
                }
            }
            // 使用URL实体转换工具
            UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(pairs, charset);
            httpPost.setEntity(entityParam);
            // 传输的类型
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            // 执行请求
            response = httpClient.execute(httpPost);
            if(response != null && response.getStatusLine().getStatusCode() == 200) {
                // 获得响应的实体对象
                HttpEntity entity = response.getEntity();
                if(entity != null) {
                    // 使用Apache提供的工具类进行转换成字符串
                    entityStr = EntityUtils.toString(entity, charset);
                }
            } else {
                String status  = response!=null?String.valueOf(response.getStatusLine().getStatusCode()):"";
                logger.info("调用短信提供商【MO信通】服务失败，返回http状态码【{}】", status);
                return entityStr;
            }
        } catch (Exception e) {
            logger.error("【MO信通】发送短信出现异常", e);
            return entityStr;
        } finally {
            // 释放连接
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (IOException e) {
                    logger.error("释放连接出错", e);
                }
            }
        }
        return entityStr;
    }

    /**
     * 发送短信传输加密密码
     *
     * @param psw
     * @param account
     * @param uoLeemMessage
     * @return
     */
    private UoLeemMessage encryptPsw(String psw, String account, UoLeemMessage uoLeemMessage){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curerntDate = new Date();
        String formatCurerntDate = format.format(curerntDate);
        String encryptPswd = DigestUtils.md5Hex(account + psw + formatCurerntDate);
        uoLeemMessage.setTs(formatCurerntDate);
        uoLeemMessage.setPwd(encryptPswd);
        return uoLeemMessage;
    }
}
