package com.nyd.zeus.model.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.alibaba.fastjson.JSONObject;

public class ObjectTool {

	public static void copy(Object from, Object to) {
		JSONObject json = (JSONObject) JSONObject.toJSON(from);
		try {
			Method[] methods = to.getClass().getMethods();
			for (Method method : methods) {
				String methodName = method.getName();
				if (methodName.startsWith("set")) {
					String key = methodName.substring(3, methodName.length());
					key = key.replaceAll("_", "");
					String mKey = key.toLowerCase();
					for (String jsonKey : json.keySet()) {
						String jKey = jsonKey.replaceAll("_", "").toLowerCase();
						if (jKey.equalsIgnoreCase(mKey)) {
							Parameter[] paras = method.getParameters();
							String type = paras[0].getType().getName();
							try {
								if (type.endsWith("String")) {
									method.invoke(to, new Object[] { json
											.getString(jsonKey) });
								}
								if (type.toLowerCase().endsWith("double")) {
									method.invoke(to, new Object[] { json
											.getDouble(jsonKey) });
								}
								if (type.endsWith("Integer")
										|| type.toLowerCase().endsWith("int")) {
									method.invoke(to, new Object[] { json
											.getInteger(jsonKey) });
								}
								if (type.toLowerCase().endsWith("long")) {
									method.invoke(to, new Object[] { json
											.getLong(jsonKey) });
								}
								if (type.endsWith("Date")) {
									method.invoke(to, new Object[] { json
											.getDate(jsonKey) });
								}
								if (type.endsWith("ecimal")) {
									method.invoke(to, new Object[] { json
											.getBigDecimal(jsonKey) });
								}
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}

}
