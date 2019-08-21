package com.nyd.msg.service.channel;

import com.nyd.msg.service.channel.model.SmsConfigDto;
import com.nyd.msg.service.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

/**
 * @author san
 * @version V1.0
 */
@Slf4j
@Component
public class TianRuiChannelStrategy implements ChannelStrategy{
    @Override
    public boolean sendSms(Message message, boolean batch) {
        // 手机号
        String phones = message.getCellPhones();
        return false;
    }

    // 测试普通短信
    public void sendsms() throws Exception {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod("http://api.1cloudsp.com/api/v2/single_send");
        postMethod.getParams().setContentCharset("UTF-8");
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());

        String accesskey = "fRH31seQR5KesPKs"; //用户开发key
        String accessSecret = "gZELpo0k0a1GBVu3srKHs45vSMsrHTFn"; //用户开发秘钥

        NameValuePair[] data = {
                new NameValuePair("accesskey", accesskey),
                new NameValuePair("secret", accessSecret),
                new NameValuePair("sign", "【助乐钱包】"),
                new NameValuePair("templateId", "154141"),
                new NameValuePair("mobile", "17521140764"),
                new NameValuePair("content", URLEncoder.encode("测试##2000##198##198##测试号码", "utf-8"))//（示例模板：{1}您好，您的订单于{2}已通过{3}发货，运单号{4}）
        };
        postMethod.setRequestBody(data);
        postMethod.setRequestHeader("Connection", "close");
        int statusCode = httpClient.executeMethod(postMethod);
        System.out.println("statusCode: " + statusCode + ", body: "
                + postMethod.getResponseBodyAsString());
    }

    // 逻辑调用
    public boolean sendSms(SmsConfigDto smsConfigDto) throws Exception {
        log.info("天瑞云请求参数 sendSms = {}", smsConfigDto);
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(smsConfigDto.getSmsPlatUrlSingle());
        postMethod.getParams().setContentCharset("UTF-8");
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
        NameValuePair[] data = {
                new NameValuePair("accesskey", smsConfigDto.getSmsPlatAccount()),
                new NameValuePair("secret", smsConfigDto.getSmsPlatPwd()),
                new NameValuePair("sign", smsConfigDto.getSign()),
                new NameValuePair("templateId", smsConfigDto.getTemplateId()),
                new NameValuePair("mobile", smsConfigDto.getMobile()),
                new NameValuePair("content", URLEncoder.encode(smsConfigDto.getContent(), "utf-8"))//（示例模板：{1}您好，您的订单于{2}已通过{3}发货，运单号{4}）
        };
        postMethod.setRequestBody(data);
        postMethod.setRequestHeader("Connection", "close");
        int statusCode = httpClient.executeMethod(postMethod);

        log.info("天瑞云返回参数 sendSms = {}", "statusCode: " + statusCode + ", body: "
                + postMethod.getResponseBodyAsString());
        if (200 == statusCode){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    public static void main(String[] args) throws Exception {
        TianRuiChannelStrategy t = new TianRuiChannelStrategy();
        //普通短信
        t.sendsms();
    }
}
