package com.nyd.capital.service.pocket.util;

import org.springframework.util.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/** 
 * @Description:发送http请求帮助类 
 * @author:zhangcheng
 * @time:2018年9月28日
 */  
public class HttpClientHelper {

	
	/**
	 * <p>
	 * 修改Url的某个参数的值。
	 * </p>
	 * 
	 * @param url
	 *            要替换参数的URL
	 * @param key
	 *            要替换参数的name
	 * @param value
	 *            要为该参数替换的值
	 * @param isAdd
	 *            如果该参数不存在,是否添加
	 * @since 1.0
	 */
	public static String parseUrl(String url, String key, String value,
			boolean isAdd) {
		Assert.notNull(url);
		Assert.notNull(key);
		boolean isExist = false;
		String[] split1 = url.split("\\?");
		String urlLeft = split1[0]; // URI
		String query = split1.length == 2 ? split1[1] : ""; // 参数
		if (split1.length >= 2) {// 如果有参数的话
			query = split1[1];
		} else {// 如果有参数的话
			if (isAdd) {// 如果添加
				return split1[0] + "?" + key + "=" + value;
			} else {// 如果不添加
				return split1[0];
			}
		}
		String[] parseUrl = query.split("&");
		query = "";
		for (int i = 0; i < parseUrl.length; i++) {
			String[] split2 = parseUrl[i].split("=");
			String paramName = split2[0];
			String paramValue = split2.length == 2 ? split2[1] : "";
			if (paramName.equals(key)) {// 如果这个参数的要修改的参数
				isExist = true;
				paramValue = value;
			}
			if (query.equals("")) {
				query = paramName + "=" + paramValue;
			} else {
				query = query + "&" + paramName + "=" + paramValue;
			}
		}
		url = query.equals("") ? urlLeft : urlLeft + "?" + query;
		if (isAdd && !isExist) {// 如果需要添加,并且不存在,就添加
			url =addParam(url, key, value);
		}
		return url;
	}

	/**
	 * <p>
	 * 添加参数。
	 * </p>
	 * 
	 * @param url
	 * @param key
	 * @param value
	 * @return
	 * @throws 。
	 * @see
	 * @since 1.0
	 */
	public static String addParam(String url, String key, String value) {
		Assert.notNull(url);
		Assert.notNull(key);
		if (url.indexOf("?") < 1) {
			url += "?" + key + "=" + value;
		} else {
			url += "&" + key + "=" + value;
		}
		return url;
	}

	public static String delParam(String url, String key) {
		Assert.notNull(url);
		Assert.notNull(key);
		String tempUrl = "";
		if (url.indexOf("?") >= 1) {
			String[] tempSplit = url.split("\\?");
			tempUrl = url.split("\\?")[0];
			if (tempSplit.length >= 2) {
				url = url.split("\\?")[1];
			}
		} else {
			return url;
		}
		String[] parseUrl = url.split("&");
		url = "";
		for (int i = 0; i < parseUrl.length; i++) {
			String[] split2 = parseUrl[i].split("=");
			if (split2.length <= 0) {
				continue;
			}
			if (!split2[0].equals(key)) {
				if (split2.length >= 2) {
					url += split2[0] + "=" + split2[1] + "&";
				} else {
					url += split2[0] + "=" + "" + "&";
				}
			}

		}
		if ((url.lastIndexOf("&") != -1)
				&& (url.lastIndexOf("&") == (url.length() - 1))) {
			url = url.substring(0, url.lastIndexOf("&"));
		}
		if (url.equals("")) {
			return tempUrl;
		} else {
			return tempUrl + "?" + url;
		}
	}
	
	
	/**
	 * Post Request
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String parameterData) throws Exception {
		URL localURL = new URL(url);
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

		httpURLConnection.setDoInput(true);
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
		/*httpURLConnection.setRequestProperty("Content-Type",
		 "application/x-www-form-urlencoded");*/
		httpURLConnection.setRequestProperty("Content-type", "application/json;charset=UTF-8"); 
		httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameterData.length()));
		httpURLConnection.setReadTimeout(10000);
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;

		try {
			
			outputStream = httpURLConnection.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream);
			
//			outputStreamWriter.write(parameterData.toString());
			outputStreamWriter.write(parameterData.toString());
			outputStreamWriter.flush();

			if (httpURLConnection.getResponseCode() >= 300) {
				throw new Exception(
						"HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
			}

			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}

		} finally {

			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}

			if (outputStream != null) {
				outputStream.close();
			}

			if (reader != null) {
				reader.close();
			}

			if (inputStreamReader != null) {
				inputStreamReader.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}

			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}

		}

		return resultBuffer.toString();
	}
	
	/**
	 * @Description:使用URLConnection发送post
	 * @time:2018年8月8日 下午3:26:52
	 */
	public static String sendPost(String urlParam, String sbParams, String charset) {
		StringBuffer resultBuffer = null;
		// 构建请求参数
		URLConnection con = null;
		HttpURLConnection httpURLConnection=null;
		OutputStreamWriter osw = null;
		BufferedReader br = null;
		try {
			URL realUrl = new URL(urlParam);
			// 打开和URL之间的连接
			con = realUrl.openConnection();
			httpURLConnection = (HttpURLConnection) con;
			// 设置通用的请求属性
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setReadTimeout(10000);
			
			httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-type", "application/json;charset=UTF-8"); 
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(sbParams.length()));
			
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			osw = new OutputStreamWriter(httpURLConnection.getOutputStream(), charset);
			// 发送请求参数
			osw.write(sbParams);
			// flush输出流的缓冲
			osw.flush();
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode >= 300) {
				throw new Exception(
					"HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
			}
			// 定义BufferedReader输入流来读取URL的响应
			resultBuffer = new StringBuffer();
			br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), charset));
			String temp;
			while ((temp = br.readLine()) != null) {
				resultBuffer.append(temp);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					osw = null;
					throw new RuntimeException(e);
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
					throw new RuntimeException(e);
				}
			}
		}
		return resultBuffer.toString();
	}
	
}
