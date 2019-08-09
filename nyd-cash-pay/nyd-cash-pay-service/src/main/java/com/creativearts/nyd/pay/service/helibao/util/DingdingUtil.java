package com.creativearts.nyd.pay.service.helibao.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;


public class DingdingUtil {
	
	
	  
	  public final static String WEBHOOKTOKENURL = "https://oapi.dingtalk.com/robot/send?access_token=1ddb3a1b3028f26c35d28efc1c86fa769beb47b10aa48c0c558706c921b8aad4";
	
	  
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
	    	DingdingUtil.getErrMsg("我是杰飞，进来测试");
		}
	
	}
