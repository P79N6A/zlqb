package com.creativearts.nyd.pay.service.changjie.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	
	/**
	 * javaBean转换成json
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static String writeValueToJson(Object object) throws Exception {
		if (object == null) {
			throw new Exception("必要参数为空:  object:" + object);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
    /**
     * 属性：private String operId 
     * 转换结果中词首字母是大写， 例如：{"OperId":"13818930251"}"
     * 
     * @param obj
     * @return
     */
    public static String writeValue2JSONCapital(Object object) throws Exception {
		if (object == null) {
			throw new Exception("必要参数为空:  object:" + object);
		}
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        return objectMapper.writeValueAsString(object);
    }
    /**
     * 通过字符串获取指定的Json值 ({"OperId":"13818930251"}" key为OperId )
     * @param jsonStr
     * @param paramName
     * @return
     */
	public static String findValueFromJsonStr(String jsonString, String paramName) throws Exception {
		if (jsonString == null || jsonString.equals("") || paramName == null || paramName.equals("")) {
			throw new Exception("必要参数为空:  jsonString:" + jsonString + " ,paramName:" + paramName);
		}
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(jsonString);
		return node.get(paramName).toString();
	}
	
    /**
     * 首字母转小写
     * @param str
     * @return
     */
	private static String toLowerCaseFirstCharacter(String str) {
		if (Character.isLowerCase(str.charAt(0))) {
			return str;
		} else {
			return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1))
					.toString();
		}
	}

    /**
     * 首字母转大写
     * @param str
     * @return
     */
	private static String toUpperCaseFirstCharacter(String str) {
		if (Character.isUpperCase(str.charAt(0))) {
			return str;
		} else {
			return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1))
					.toString();
		}
	}
	
    /**
     * 通过Json字符串转换为MAP
     * @param jsonStr
     * @return
     */
	public static Map<String, String> readJSON2Map(String jsonString) throws Exception {
		if (jsonString == null || jsonString.equals("")) {
			throw new Exception("必要参数为空:  object:" + jsonString);
		}
		JsonNode node = new ObjectMapper().readTree(jsonString);
		Map<String, String> map = new HashMap<String,String>();
		Entry<String, JsonNode> jsonNode = null;
		for (Iterator<Entry<String, JsonNode>> iterator = node.fields(); iterator.hasNext();) {
			jsonNode = iterator.next();
			map.put(jsonNode.getKey(), jsonNode.getValue().toString());
		}
		return map;
	}
	
	/**
	 * json字符串转换成javaBean对象
	 * @param jsonString
	 * @param clazz
	 * @return
	 * @throws Exception 
	 */
	public static <T> T readValueToBean(String jsonString, Class<T> clazz) throws Exception {
		if (StringUtils.isBlank(jsonString) || clazz == null) {
			throw new Exception("必要参数为空:  json:" + jsonString + " ,clazz:" + clazz);
		}
		T result = null;
		ObjectMapper mapper = new ObjectMapper();
		result = mapper.readValue(jsonString, clazz);
		return result;
	}
	
	/**
	 * 将json字符串转为Map结构 如果json复杂，结果可能是map嵌套map
	 * 
	 * @param jsonStr
	 *            入参，json格式字符串
	 * @return 返回一个map
	 */
	public static Map<String, Object> json2Map(String jsonStr) {
		Map<String, Object> map = new HashMap<>();
		if (jsonStr != null && !"".equals(jsonStr)) {
			// 最外层解析
			JSONObject json = JSONObject.fromObject(jsonStr);
			for (Object k : json.keySet()) {
				Object v = json.get(k);
				// 如果内层还是数组的话，继续解析
				if (v instanceof JSONArray) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					Iterator<JSONObject> it = ((JSONArray) v).iterator();
					while (it.hasNext()) {
						JSONObject json2 = it.next();
						list.add(json2Map(json2.toString()));
					} 
					map.put(k.toString(), list);
				} else {
					map.put(k.toString(), v);
				}
			}
			return map;
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * objectToMap:(对象转map). <br/>
	 * @author wangzhch
	 * @param obj
	 * @return
	 * @throws Exception
	 * @since JDK 1.8
	 */
	public static Map<String, String> objectToMap(Object obj) throws Exception {    
        if(obj == null){    
            return null;    
        }   
        Map<String, String> map = new HashMap<String, String>();    
        Field[] declaredFields = obj.getClass().getDeclaredFields();    
        for (Field field : declaredFields) { 
        	//使private成员可以被访问、修改
            field.setAccessible(true);  
            if(null != field.get(obj)) {
            	map.put(field.getName(), String.valueOf(field.get(obj)));
            }
        }    
        return map;  
    }
	
	/**
	 * 
	 * keyValueToMap:(key=value&key=value转map). <br/>
	 * @author wangzhch
	 * @param result
	 * @return
	 * @since JDK 1.8
	 */
	public static Map<String, String> keyValueToMap(String result){
		String[] strs = result.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String s : strs) {
			String[] ms = s.split("=");
			map.put(ms[0], ms[1]);
		}
		return map;
	}
}
