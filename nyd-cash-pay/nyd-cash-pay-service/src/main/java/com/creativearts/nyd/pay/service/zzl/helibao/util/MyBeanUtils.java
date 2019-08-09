package com.creativearts.nyd.pay.service.zzl.helibao.util;


import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.creativearts.nyd.pay.config.utils.zzl.helibao.DemoBase;
import com.creativearts.nyd.pay.config.utils.zzl.helibao.Disguiser;

/**
 * Created by heli50 on 2017/4/14.
 */
public class MyBeanUtils extends BeanUtils{
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("P1_bizType", "");
        map.put("P2_customerNumber", "");
        map.put("P3_userId", "");
        map.put("P4_orderId", "");
        map.put("P5_verifyType", "");
        map.put("P6_timestamp", "");
        map.put("P7_payerName", "");
        map.put("P8_idCardType", "");
        map.put("P9_idCardNo", "");
        map.put("P10_cardNo", "");
        map.put("P11_year", "");
        map.put("P12_month", "");
        map.put("P13_cvv2", "");
        map.put("P14_phone", "");
        map.put("sign", "");

        String oriMessage = MyBeanUtils.getSigned(map, null);
        System.out.println("签名原文串：" +oriMessage);
        String sign = Disguiser.disguiseMD5(oriMessage.trim());
        System.out.println("签名串：" + sign);
    }

    public static Map convertBean(Object bean, Map retMap)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
        }
        for (Field f : fields) {
            String key = f.toString().substring(f.toString().lastIndexOf(".") + 1);
            Object value = f.get(bean);
            if(value == null)
                value = "";
            retMap.put(key, value);
        }
        return retMap;
    }
    
    public static Map convertBeanReq(Object bean, Map retMap)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
        }
        for (Field f : fields) {
            String key = f.toString().substring(f.toString().lastIndexOf(".") + 1);
            if(key.contains("_")){
				key = MessageHandle.toUpperCaseFirstOne(key);
			}
            Object value = f.get(bean);
            if(value == null)
                value = "";
            /*if(!String.class.isAssignableFrom(f.getType())){
         	  value =JSON.toJSONString(value, SerializerFeature.WriteMapNullValue,
                      SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty);
            }*/
            retMap.put(key, value);
        }
        return retMap;
    }

    public static String getSigned(Map<String, String> map, String[] excludes){
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
                sb.append(DemoBase.split);
                sb.append(value);
            }
        }
        return sb.toString();
    }

    public static String getSigned(Object bean, String[] excludes) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Map map  = convertBean(bean, new LinkedHashMap());
        String signedStr = getSigned(map, excludes);
        return signedStr;
    }

}
