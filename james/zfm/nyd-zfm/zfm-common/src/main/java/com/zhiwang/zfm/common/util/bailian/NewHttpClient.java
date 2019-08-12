package com.zhiwang.zfm.common.util.bailian;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NewHttpClient {
	
	private static Logger logger = LoggerFactory.getLogger(NewHttpClient.class);

	@SuppressWarnings("resource")
	public static String postSend(String url,String argsJson) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		logger.info(" 请求地址为： " + url);
		
		HttpPost httpPost = new HttpPost(url);
		
		StringEntity entity = new StringEntity(argsJson);
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/text");
		httpPost.setEntity(entity);
		logger.info(" 请求参数request为 ： " + argsJson);
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity resEntity = response.getEntity();
        String resp = EntityUtils.toString(resEntity);
        logger.info(" 返回参数response为 " + resp);
        return resp;
	}
	
	
}
