package com.creativearts.nyd.pay.service.helibao.config;

import com.creativearts.nyd.pay.service.helibao.common.HLConstant;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Cong Yuxiang
 * 2017/12/15
 **/
@Component
@Data
public class HelibaoConfig {
    @Value("${helibao.merchant.id}")
    private String merchantNo;

    @Value("${helibao.request.url}")
    private String requestUrl;

    @Value("${helibao.signkey.md5}")
    private String signkeyMd5;

    @Value("${helibao.des.key}")
    private String deskey;

    @Value("${helibao.serverCallback.url}")
    private String callBackUrl;

    @Value("${helibao.quickPayApi.url}")
    private String quickPayApiUrl;

    @Value("${helibao.signkey.quickpay}")
    private String signKeyQuickPay;



    public  Map convertBean(Object bean, Map retMap) throws IllegalAccessException {
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
        }
        for (Field f : fields) {
            String key = f.toString().substring(f.toString().lastIndexOf(".") + 1);
            Object value = f.get(bean);
            String temp;
            if(value == null)
                temp = "";
            else {
                temp = value.toString();
            }
            retMap.put(key, temp);
        }
        return retMap;
    }

    public  String getSigned(Map<String, String> map, String[] excludes){
        StringBuffer sb = new StringBuffer();
        Set<String> excludeSet = new HashSet<String>();
        excludeSet.add("sign");
        if(excludes != null){
            for(String exclude : excludes){
                excludeSet.add(exclude);
            }
        }
        for(String key : map.keySet()){
            if(!excludeSet.contains(key)){
                String value = map.get(key);
                value = (value == null ? "" : value);
                sb.append(HLConstant.SPLIT);
                sb.append(value);
            }
        }
        sb.append(HLConstant.SPLIT);
        sb.append(signkeyMd5);
//        sb.append(signKeyQuickPay);
        return sb.toString();
    }



    public  String getSigned(Object bean, String[] excludes) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Map map  = convertBean(bean, new LinkedHashMap());
        String signedStr = getSigned(map, excludes);
        return signedStr;
    }
}
