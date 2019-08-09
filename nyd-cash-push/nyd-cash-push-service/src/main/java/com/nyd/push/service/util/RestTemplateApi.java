package com.nyd.push.service.util;

import com.google.gson.*;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

/**
 * 封装了RestTemplate的一些接口
 *
 * @author hwei
 *
 * @version RestTemplateApiImpl.java, V1.0 2017年11月6日 下午3:54:39
 */
@Component
public class RestTemplateApi {

	@Resource(name = "defaultRestTemplate")
	private RestTemplate defaultRestTemplate;

	/**
	 * post请求
	 * @param url
	 * @param bodyParams
	 * @param responseType
	 * @param <T>
	 * @return
	 */
	public <T> T postForObject(String url, Map<String, Object> bodyParams, Class<T> responseType) {
		Gson gson = new Gson();
		String body = gson.toJson(bodyParams);
		// 请求header
		HttpHeaders authHeader = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		authHeader.setContentType(type);
		authHeader.add("Accept", MediaType.APPLICATION_JSON.toString());
		// 封装entity
		HttpEntity<String> entity = new HttpEntity<String>(body, authHeader);
		return defaultRestTemplate.postForObject(url, entity, responseType);
	}

	public <T> T postForObjectOtherHeader(String url, Map<String, Object> bodyParams, Class<T> responseType, Map<String, Object> otherHeaders) {
		Gson gson = new Gson();
		String body = gson.toJson(bodyParams);
		// 请求header
		HttpHeaders authHeader = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		authHeader.setContentType(type);
		authHeader.add("Accept", MediaType.APPLICATION_JSON.toString());

		if (otherHeaders != null) {
			for (Map.Entry<String, Object> entry : otherHeaders.entrySet()) {
				authHeader.add(entry.getKey(), (String) entry.getValue());
			}
		}
		// 封装entity
		HttpEntity<String> entity = new HttpEntity<String>(body, authHeader);
		return defaultRestTemplate.postForObject(url, entity, responseType);
	}

	/**
	 * get请求
	 * @param url
	 * @param type
	 * @param <T>
	 * @return
	 */
	public <T> T getForObject(String url, Type type) {
		GsonBuilder builder = new GsonBuilder();

		// Register an adapter to manage the date types as long values
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				return new Date(json.getAsJsonPrimitive().getAsLong());
			}
		});
		Gson gson = builder.create();
		String getResult = defaultRestTemplate.getForObject(url, String.class);
		T result = gson.fromJson(getResult, type);
		return result;
	}

	public <T> T putForObject(String url, String requestBody, Class<T> responseType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
		ResponseEntity<T> response = defaultRestTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
		return response.getBody();
	}

	public RestTemplate getDefaultRestTemplate() {
		return defaultRestTemplate;
	}

	public void setDefaultRestTemplate(RestTemplate defaultRestTemplate) {
		this.defaultRestTemplate = defaultRestTemplate;
	}

}
