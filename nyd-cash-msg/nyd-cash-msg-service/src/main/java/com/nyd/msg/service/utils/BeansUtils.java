package com.nyd.msg.service.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shaoqing.liu
 * @date 2018/6/11 15:56
 */
public class BeansUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeansUtils.class);




    /**
     * 多层map转换
     * @param obj
     * @return
     */
    public static Map objectToMap(Object obj){
        try{
            Class type = obj.getClass();
            Map returnMap = new HashMap();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(obj, new Object[0]);
                    if(result == null){
                        continue;
                    }
                    //判断是否为 基础类型 String,Boolean,Byte,Short,Integer,Long,Float,Double
                    //判断是否集合类，COLLECTION,MAP
                    if(result instanceof String
                            || result instanceof Boolean
                            || result instanceof Byte
                            || result instanceof Short
                            || result instanceof Integer
                            || result instanceof Long
                            || result instanceof Float
                            || result instanceof Double
                            || result instanceof Enum
                            ){
                        if (result != null) {
                            returnMap.put(propertyName, result);
                        }
                    }else if(result instanceof Collection){
                        Collection<?> lstObj = arrayToMap((Collection<?>)result);
                        returnMap.put(propertyName, lstObj);

                    }else if(result instanceof Map){
                        Map<Object,Object> lstObj = mapToMap((Map<Object,Object>)result);
                        returnMap.put(propertyName, lstObj);
                    } else {
                        Map mapResult = objectToMap(result);
                        returnMap.put(propertyName, mapResult);
                    }

                }
            }
            return returnMap;
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
    private static Map<Object, Object> mapToMap(Map<Object, Object> orignMap) {
        Map<Object,Object> resultMap = new HashMap<Object,Object>();
        for(Map.Entry<Object, Object> entry:orignMap.entrySet()){
            Object key = entry.getKey();
            Object resultKey = null;
            if(key instanceof Collection){
                resultKey = arrayToMap((Collection)key);
            }else if(key instanceof Map){
                resultKey = mapToMap((Map)key);
            }
            else{
                if(key instanceof String
                        || key instanceof Boolean
                        || key instanceof Byte
                        || key instanceof Short
                        || key instanceof Integer
                        || key instanceof Long
                        || key instanceof Float
                        || key instanceof Double
                        || key instanceof Enum
                        ){
                    if (key != null) {
                        resultKey = key;
                    }
                }else{
                    resultKey = objectToMap(key);
                }
            }


            Object value = entry.getValue();
            Object resultValue = null;
            if(value instanceof Collection){
                resultValue = arrayToMap((Collection)value);
            }else if(value instanceof Map){
                resultValue = mapToMap((Map)value);
            }
            else{
                if(value instanceof String
                        || value instanceof Boolean
                        || value instanceof Byte
                        || value instanceof Short
                        || value instanceof Integer
                        || value instanceof Long
                        || value instanceof Float
                        || value instanceof Double
                        || value instanceof Enum
                        ){
                    if (value != null) {
                        resultValue = value;
                    }
                }else{
                    resultValue = objectToMap(value);
                }
            }

            resultMap.put(resultKey, resultValue);
        }
        return resultMap;
    }


    private static Collection arrayToMap(Collection lstObj){
        ArrayList arrayList = new ArrayList();

        for (Object t : lstObj) {
            if(t instanceof Collection){
                Collection result = arrayToMap((Collection)t);
                arrayList.add(result);
            }else if(t instanceof Map){
                Map result = mapToMap((Map)t);
                arrayList.add(result);
            } else {
                if(t instanceof String
                        || t instanceof Boolean
                        || t instanceof Byte
                        || t instanceof Short
                        || t instanceof Integer
                        || t instanceof Long
                        || t instanceof Float
                        || t instanceof Double
                        || t instanceof Enum
                        ){
                    if (t != null) {
                        arrayList.add(t);
                    }
                }else{
                    Object result = objectToMap(t);
                    arrayList.add(result);
                }
            }
        }
        return arrayList;
    }

    


    /**
     * javaBean转Map
     * @param bean
     * @return
     * @throws Exception
     */
    public static Map<String, Object> transforObjectToMap(Object bean){
        if(bean == null){
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                //不需要put到map中的对象
               /* ITransientMap transientMap =  field.getAnnotation(ITransientMap.class);
                if(transientMap != null){
                    continue;
                }*/
                map.put(field.getName(), field.get(bean));
            }catch (IllegalAccessException ex){
                LOGGER.error(String.format("transforObjectToMap Error"), ex);
            }
        }
        return map;
    }

    /**
     * Map对象转javaBean
     *
     * @param map
     * @param obj
     */
    public static Object transMapToBean(Map<String, Object> map, Object obj) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    try {
                        Method getter = property.getReadMethod();
                        Type returnType = getter.getGenericReturnType();// 返回类型
                        if ("int".equals(returnType.toString())) {
                            value = Integer.parseInt(value.toString());
                        } else if ("java.lang.String".equals(returnType.toString())
                                || ("class java.lang.String".equals(returnType.toString()))) {
                            if (value == null) {
                                value = "";
                            } else {
                                value = value.toString();
                            }

                        }
                        if ("float".equals(returnType.toString())) {
                            value = Float.parseFloat(value.toString());
                        }
                        setter.invoke(obj, value);
                    } catch (Exception e) {
                        LOGGER.error(String.format("transMapToBean Error key:%s value:%s", key, value), e);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("transMapToBean Error"), e);
        }
        return obj;
    }

    /**
     * 将map转换成url
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }

    /**
     * 判断字符串是否是json
     * @param content
     * @return
     */
    public static boolean isJson(String content){
        try {
            JSONObject jsonStr= JSONObject.parseObject(content);
            return  true;
        } catch (Exception e) {
            return false;
        }
    }
}
