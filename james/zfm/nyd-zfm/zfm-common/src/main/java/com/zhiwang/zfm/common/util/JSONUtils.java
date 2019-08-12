package com.zhiwang.zfm.common.util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.alibaba.fastjson.JSONObject;

public class JSONUtils {
	
	public static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);
	
	private static ObjectMapper mapper = null;
	
	static{
		mapper = new ObjectMapper();
		
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.getDeserializationConfig().without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.getDeserializationConfig().withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"));
		mapper.getSerializationConfig().withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"));
	}
	
	public JSONUtils(){}
	
	public static String listToJson(List list) throws Exception{
		return objectToJson(list);
	}
	
	public static String mapToJson(Map map) throws Exception{
		return objectToJson(map);
	}
	
	public static String setToJson(Set set) throws Exception{
		return objectToJson(set);
	}
	
	public static String arrayToJson(Object[] array) throws Exception{
		return objectToJson(array);
	}
	
	public static String objectToJson(Object obj)throws Exception{
		try{
			if((obj instanceof String) && !mayBeJSON(obj.toString()) ){
				return obj.toString();
			}
			return mapper.writeValueAsString(obj);
		}catch(Throwable e){
			logger.error("objectToJson run error, param(obj) is:" + obj , e);
			throw new Exception(e);
		}
	}
	
	public static Object[] jsonToObjectArray(String jsonString) throws Exception{
		List list = (List)jsonToObject(jsonString, List.class);
		return list.toArray();
	}
	
	public static List jsonToList(String jsonString) throws Exception{
		return (List)jsonToObject(jsonString, List.class);
	}
	
	public static Map jsonToMap(String jsonString) throws Exception{
		return (Map)jsonToObject(jsonString, Map.class);
	}
	
	public static <T> T jsonToObject(String jsonString, Class<T> pojoClass) throws Exception{
		try{
			return mapper.readValue(jsonString, pojoClass);
		}catch(Throwable e){
			logger.error("jsonToObject run error, param1(jsonString) is:" + jsonString + ", param2(pojoClass)" + pojoClass, e);
			throw new Exception(e);
		}
	}
	
	public static Object jsonToObject(String jsonString) throws Exception{
		try{
			if(jsonString != null && jsonString.startsWith("{")){
				return mapper.readValue(jsonString, Map.class);
			}
			if(jsonString != null && jsonString.startsWith("[")){
				return mapper.readValue(jsonString, List.class);
			}
			return jsonString;
		}catch(Throwable e){
			logger.error("jsonToObject run error, param is:" + jsonString, e);
			throw new Exception(e);
		}
	}
	
	public static boolean mayBeJSON(String string){
		return (string != null) && (  "null".equals(string) || (string.startsWith("[")&&string.endsWith("]")) || (string.startsWith("{")&&string.endsWith("}"))   );
	}
	
	
	public static String format(String jsonStr){
		int level = 0;
		StringBuffer resultSb = new StringBuffer();
		for(int i = 0; i < jsonStr.length(); i++){
			char c = jsonStr.charAt(i);
			if((level>0) && ('\n' == resultSb.charAt(resultSb.length() - 1))){
				resultSb.append(getLevelStr(level));
			}
			switch(c){
			case '[':
			case '{':
				resultSb.append(c + "\n");
				level++;
				break;
			case ',':
				resultSb.append(c + "\n");
				break;
			case ']':
			case '}':
				resultSb.append("\n");
				level--;
				resultSb.append(getLevelStr(level));
				resultSb.append(c);
				break;
			default:
				resultSb.append(c);
			}
		}
		return resultSb.toString();
	}
	
	
	private static String getLevelStr(int level){
		StringBuffer levelSb = new StringBuffer();
		for(int i = 0; i < level; i++){
			levelSb.append("\t");
		}
		return levelSb.toString();
		
	}
	
	public static boolean isValid(String jsonString){
		boolean flag = false;
		try{
		    JSONObject.parseObject(jsonString);
		    flag = true;
		}catch(Exception e){
			logger.error("",e);
		}
		return flag;
	}
	
	public static Object JSON2Object(JSONObject json,Object o) {
		 o = JSONObject.toJavaObject(json, o.getClass());
		return o;
	}
	
}
