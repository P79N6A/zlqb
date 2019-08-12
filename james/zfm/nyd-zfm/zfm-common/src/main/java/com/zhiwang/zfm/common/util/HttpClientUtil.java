package com.zhiwang.zfm.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;




/**
 * 
 * 功能说明：HTTP操作类
 * 典型用法：
 * 特殊用法：
 * @author panye
 * 修改人: 
 * 修改原因：
 * 修改时间：
 * 修改内容：
 * 创建日期：2015-6-10
 * Copyright zzl-apt
 */
public class HttpClientUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	
	/**
	 * 
	 * 功能说明：POST 请求得到数据  	
	 * panye  2015-6-10
	 * @param 
	 * @return   返回得到的结果集，如果出现异常或者连接超时	 则返回空
	 * @throws  该方法可能抛出的异常，异常的类型、含义。
	 * 最后修改时间：
	 * 修改人：panye
	 * 修改内容：
	 * 修改注意点：
	 */
	public static String sendPost(String url,Map<String, Object>paramMap) {

		HttpPost httpRequset = new HttpPost(url);
		// 使用NameValuePair来保存要传递的Post参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		//加载参数
		if(paramMap!=null){
			Set<String> keys = paramMap.keySet();
			NameValuePair[] data = new NameValuePair[keys.size()];
			Iterator it = keys.iterator();
			while(it.hasNext()){
				String key = String.valueOf(it.next());
				if(paramMap.get(key)!=null) {
					params.add(new BasicNameValuePair(key, paramMap.get(key).toString()));
				}
			}
		}
		
		try {
			// 设置字符集
			HttpEntity httpentity = new UrlEncodedFormEntity(params, "utf-8");
			
			// 请求httpRequset
			httpRequset.setEntity(httpentity);
			
			// 取得HttpClient
			HttpClient httpClient = new DefaultHttpClient();
			
			//设置连接超时时间
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,10000);
			
			// 设置读取超时时间
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,60000);
			
			// 取得HttpResponse
			HttpResponse httpResponse = httpClient.execute(httpRequset);
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return "";
			} 
			
			HttpEntity entity = httpResponse.getEntity();
			if (null != entity) {
				return EntityUtils.toString(httpResponse.getEntity());
			}
			
		} catch (Exception e) {
			logger.error("发送post请求出现异常，请求连接："+url);
			logger.error(e.getMessage(),e);
		}
		
		return "";
	}
	
	public static void main(String[] args) {
		String url = "http://172.16.10.171:8085/boc/api/bank/query/balanceQuery";
		JSONObject paramMap = new JSONObject();
		paramMap.put("accountId", "6212461030000053193");
		System.out.println(sendPost(url, new Object[]{paramMap}));
	}
	
	
	public static JSONObject sendPost(String url,Object[] params) {  
        PrintWriter out = null;  
        BufferedReader in = null;  
        StringBuffer result = new StringBuffer();  
        try {  
            URL realUrl = new URL(url);
            // 打开和URL之间的连接  
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();  
            // 设置通用的请求属性  
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent",  
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");  
            // 发送POST请求必须设置如下两行  
            conn.setRequestMethod("POST");   //设置POST方式连接
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setRequestProperty("Accept-Charset", "utf-8");
            // 获取URLConnection对象对应的输出流  
            out = new PrintWriter(conn.getOutputStream());  
            // 发送请求参数  
            for (Object object : params) {
            	 out.print(object);  
			}
            // flush输出流的缓冲  
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(  
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));  
            String line;  
            while ((line = in.readLine()) != null) {  
                result.append(line);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        // 使用finally块来关闭输出流、输入流  
        finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return JSONObject.parseObject(result.toString());
    }
	
	public static String sendGet(String url,Map<String, Object>paramMap) {

		HttpGet httpRequset = new HttpGet(url);
		// 使用NameValuePair来保存要传递的Post参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		//加载参数
		if(paramMap!=null){
			Set<String> keys = paramMap.keySet();
			NameValuePair[] data = new NameValuePair[keys.size()];
			Iterator it = keys.iterator();
			while(it.hasNext()){
				String key = String.valueOf(it.next());
				params.add(new BasicNameValuePair(key, paramMap.get(key).toString()));
			}
		}
		
		try {
//			// 设置字符集
//			HttpEntity httpentity = new UrlEncodedFormEntity(params, "utf-8");
//			// 取得HttpClient
			HttpClient httpClient = new DefaultHttpClient();
//			//设置超时时间
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,10000);
			// 取得HttpResponse
			
			HttpResponse httpResponse = httpClient.execute(httpRequset);
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				
				return "";
			}
			
			HttpEntity entity = httpResponse.getEntity();
			if (null != entity) {
				return EntityUtils.toString(httpResponse.getEntity());
			}
			
		} catch (Exception e) {
			logger.warn(e.getMessage(),e);
			return "";
		}
		
		return "";
	}
	 
}
