package com.nyd.msg.service.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.nyd.msg.model.SmsDaHanResponse;
import com.nyd.msg.service.code.DaHanResultEnum;
import com.nyd.msg.service.utils.BeansUtils;
import com.nyd.msg.service.utils.DaHanMessage;
import com.nyd.msg.service.utils.PostUtil;
import com.nyd.msg.service.utils.Message;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class DaHanTricomChannelStrategy implements ChannelStrategy{

    private static Logger logger = LoggerFactory.getLogger(DaHanTricomChannelStrategy.class);

    // 请求超时时间(毫秒) 5秒
    public static int HTTP_REQUEST_TIMEOUT	= 5 * 1000;

    // 响应超时时间(毫秒) 60秒
    public static int HTTP_RESPONSE_TIMEOUT	= 60 * 1000;

    @Override
    public boolean sendSms(Message message, boolean batch) {
        try {
            String commonLog = "【大汉三通通道】";
            logger.info("{}发送短信，参数【{},{}】",commonLog,JSON.toJSONString(message),batch);
            DaHanMessage daHanMessage = new DaHanMessage();
            // 必填参数
            if(StringUtils.isNotBlank(message.getCellPhones())) {
                String phones = message.getCellPhones();
                // 如果是批量发送
                if(phones.indexOf(":") !=  -1) {
                    phones = StringUtils.replace(phones,":",",");
                    message.setCellPhones(phones);
                }
            }

            daHanMessage.setAccount(message.getSmsPlatAccount());
            encryptPsw(message.getSmsPlatPwd(),message.getSmsPlatAccount(),daHanMessage);
            daHanMessage.setPhones(message.getCellPhones());
            daHanMessage.setSign("【助乐钱包】");
            daHanMessage.setContent(message.getSmsTemplate());

            String resultStr =  PostUtil.post(message.getSmsPlatUrl(),JSON.toJSONString(daHanMessage));
            if(StringUtils.isBlank(resultStr)) {
                return Boolean.FALSE;
            } else {

                SmsDaHanResponse response = JSON.parseObject(resultStr, new TypeReference<SmsDaHanResponse>(){});
                if(DaHanResultEnum.SUCCESS.getCode().equals(response.getResult())) {
                    logger.info("{}短信发送成功", commonLog);
                    return Boolean.TRUE;
                } else {
                    if(StringUtils.isNotBlank(response.getResult())) {
                        logger.info("{}短信发送失败，失败原因：{}", commonLog, DaHanResultEnum.getMsgByCode(response.getResult()));
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
                logger.info("调用短信提供商【大汉三通】服务失败，返回http状态码【{}】", status);
                return entityStr;
            }
        } catch (Exception e) {
            logger.error("【大汉三通】发送短信出现异常", e);
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
     * @param daHanMessage
     * @return
     */
    private DaHanMessage encryptPsw(String psw, String account, DaHanMessage daHanMessage){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curerntDate = new Date();
        String formatCurerntDate = format.format(curerntDate);
        String encryptPswd = DigestUtils.md5Hex(psw);
        daHanMessage.setPassword(encryptPswd);
        daHanMessage.setSendtime(formatCurerntDate);
        return daHanMessage;
    }

}
