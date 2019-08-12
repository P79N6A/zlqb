package com.zhiwang.zfm.common.util.rest;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 类功能说明：RestClient工具类
 */
@Component
public class RestClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientUtil.class);
    
   

	/**
     * post请求
     * @param url
     * @param requestObj
     * @param responseClazz
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T>T doPost(String url, Object requestObj, Class<T> responseClazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept-Charset", "UTF-8");
            headers.set("Authorization",SignatureUtil.createSign());
   		 	HttpEntity entity = new HttpEntity(JSON.toJSON(requestObj),headers);
            Object object = RestTemplateFactory.getInstance().postForObject(url, entity, responseClazz);
            return (T)object;
        } catch (Exception e) {
            LOGGER.error("POST请求出错：{}", url, e);
        }
        return null;
    }
    
    public static JSONObject doPost(Object requestObj,String url) {
    	JSONObject json = new JSONObject();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept-Charset", "UTF-8");
            headers.set("Authorization",SignatureUtil.createSign());
   		 	HttpEntity entity = new HttpEntity(JSON.toJSON(requestObj),headers);
   		 	String result = RestTemplateFactory.getInstance().postForObject(url,entity, String.class);
   		 	json = JSON.parseObject(result); 	
            return json;
        } catch (Exception e) {
            LOGGER.error("POST请求出错：{}", e);
            
        }
        return null;
    }

    /**
     * post请求
     * @param url
     * @return
     */
    public static String doPost(String url) {
        try {
        	
            return RestTemplateFactory.getInstance().postForObject(url, HttpEntity.EMPTY, String.class);
        } catch (Exception e) {
            LOGGER.error("POST请求出错：{}", url, e);
        }

        return null;
    }

    /**
     * get请求
     * @param url
     * @return
     */
    public static String doGet(String url) {
        try {
            return RestTemplateFactory.getInstance().getForObject(url, String.class);
        } catch (Exception e) {
            LOGGER.error("GET请求出错：{}", url, e);
        }

        return null;
    }
    
    /**
     * get请求
     * @param url
     * @return
     */
    public static String doGet(String url,Map<String, Object> params) {
        try {
            return RestTemplateFactory.getInstance().getForObject(url,String.class,params);
        } catch (Exception e) {
            LOGGER.error("GET请求出错：{}", url, e);
        }

        return null;
    }
    
}