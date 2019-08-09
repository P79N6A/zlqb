package com.creativearts.nyd.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Yuxiang Cong
 **/
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);



    public Map<String, Object> getPostJson(HttpServletRequest request) throws IOException {
        java.io.ByteArrayOutputStream inBuffer = new java.io.ByteArrayOutputStream();
        java.io.InputStream input = request.getInputStream();
        byte[] tmp = new byte[1024];
        int len = 0;
        while ((len = input.read(tmp)) > 0) {
            inBuffer.write(tmp, 0, len);
        }
        byte[] requestData = inBuffer.toByteArray();
        String requestJsonStr = new String(requestData, "UTF-8");
        logger.info(requestJsonStr);
        JSONObject requestJson = JSON.parseObject(requestJsonStr);
        logger.info(requestJson.toJSONString());
        return parseJSON2Map(requestJson);
    }

    public Map<String, Object> parseJSON2Map(JSONObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (json != null) {
            for (Object k : json.keySet()) {
                Object v = json.get(k);
                // 如果内层还是数组的话，继续解析
                if (v instanceof JSONArray) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    Iterator<Object> it = ((JSONArray) v).iterator();
                    while (it.hasNext()) {
                        JSONObject json2 = (JSONObject) it.next();
                        list.add(parseJSON2Map(json2));
                    }
                    map.put(k.toString(), list);
                } else {
                    map.put(k.toString(), v);
                }
            }
        }
        logger.info(JSON.toJSONString(map));
        return map;
    }

    public HttpServletResponse getResponse(){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }
    public HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public void renderHtml(String html){
        HttpServletResponse httpResponse = getResponse();
        httpResponse.setContentType("text/html;charset=UTF-8");
        try {
            httpResponse.getWriter().write(html);// 直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void renderText(String msg){
        HttpServletResponse httpResponse = getResponse();
        httpResponse.setContentType("text/plain;charset=UTF-8");
        try {
            httpResponse.getWriter().write(msg);// 直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
