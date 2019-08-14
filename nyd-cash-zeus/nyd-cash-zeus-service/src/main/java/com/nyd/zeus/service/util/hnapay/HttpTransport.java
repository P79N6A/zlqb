/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * www.hnapay.com
 */

package com.nyd.zeus.service.util.hnapay;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.CommunicationException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 使用httpClient形式发送报文
 *
 * @author zhanghongbo
 * @HttpTransportImpl.java
 * @2012-7-5 下午5:12:06 www.hnapay.com Inc.All rights reserved.
 */
@SuppressWarnings("unchecked")
public class HttpTransport {

	private String url;
	/**
	 * timeout.
	 */
	private int timeout = 6000;
	/**
	 * encoding
	 */
	private String sendEncoding = "UTF-8";
	/**
	 * retryConnTimes.
	 */
	private int retryConnTimes = 5;

	private HttpPost method = null;

	private X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
		@Override
		public void verify(String host, SSLSocket ssl) throws IOException {
		}

		@Override
		public void verify(String host, X509Certificate cert)
				throws SSLException {
		}

		@Override
		public void verify(String host, String[] cns, String[] subjectAlts)
				throws SSLException {
		}

		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}
	};

	private TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}
	} };

	public HttpTransport(HttpPost method) {
		this.method = method;
	}

	public HttpTransport() {
	}

	/**
	 * HTTP请求，返回接收状态
	 *
	 * @param obj
	 * @return
	 * @throws CommunicationException
	 */
	public int submit_backCode(Object obj) throws CommunicationException {
		CloseableHttpClient httpClient = HttpClients
				.custom()
				.setRetryHandler(
						new DefaultHttpRequestRetryHandler(retryConnTimes,
								false)).build();
		HttpPost method = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(timeout).setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout).build();

		if (StringUtils.isNotEmpty(this.sendEncoding)) {
			method.setHeader(HTTP.CONTENT_ENCODING, sendEncoding);
		}
		method.setHeader(HTTP.USER_AGENT, "Rich Powered/1.0");

		method.setConfig(requestConfig);

		if (obj != null) {
			if (obj instanceof Map) {
				Map<String, String> paraMap = (Map<String, String>) obj;
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (Iterator<String> iter = paraMap.keySet().iterator(); iter
						.hasNext();) {
					String key = iter.next();
					String value = "";
					if (paraMap.get(key) != null) {
						value = paraMap.get(key);
					}
					// method.addParameter(key, value);
					nvps.add(new BasicNameValuePair(key, value));
				}
				try {
					method.setEntity(new UrlEncodedFormEntity(nvps));
				} catch (UnsupportedEncodingException e) {

					System.out.println("HttpTransport Exception");
					System.out
							.println("Fatal transport error while try connect to ["
									+ url + "]");
					System.out.println("Cause: " + e.getMessage());

				}
			} else if (obj instanceof byte[]) {
				method.setEntity(new ByteArrayEntity((byte[]) obj));
				System.out.println("Sent Data: \n" + new String((byte[]) obj));
			} else {
				throw new IllegalArgumentException(
						"submit(Object obj): obj should be Map or byte[]");
			}

		}

		int statusCode = 0;
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(method);
			statusCode = response.getStatusLine().getStatusCode();
		} catch (IOException e) {
			System.out.println("HttpTransport Exception");
			System.out.println(e);
			System.out.println("Fatal transport error while try connect to ["
					+ url + "]");
			System.out.println("Cause: " + e.getMessage());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				System.out.println("HttpTransport Exception");
				System.out.println(e);
			}
		}

		return statusCode;
	}

	/**
	 * HTTP请求，返回接收状态
	 *
	 * @param obj
	 * @return
	 * @throws CommunicationException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public int submit_backCode_https(Object obj) throws CommunicationException,
			NoSuchAlgorithmException, KeyManagementException {

		SSLContext sslContext = SSLContext
				.getInstance(SSLConnectionSocketFactory.TLS);
		sslContext.init(null, trustAllCerts, new SecureRandom());
		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
				sslContext, hostnameVerifier);

		CloseableHttpClient httpClient = HttpClients
				.custom()
				.setSSLSocketFactory(socketFactory)
				.setRetryHandler(
						new DefaultHttpRequestRetryHandler(retryConnTimes,
								false)).build();

		HttpPost method = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(timeout).setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout).build();
		if (StringUtils.isNotEmpty(this.sendEncoding)) {
			method.setHeader(HTTP.CONTENT_ENCODING, sendEncoding);
		}

		method.setHeader(HTTP.USER_AGENT, "Rich Powered/1.0");

		method.setConfig(requestConfig);

		if (obj != null) {
			if (obj instanceof Map) {
				Map<String, String> paraMap = (Map<String, String>) obj;
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (Iterator<String> iter = paraMap.keySet().iterator(); iter
						.hasNext();) {
					String key = iter.next();
					String value = "";
					if (paraMap.get(key) != null) {
						value = paraMap.get(key);
					}
					// method.addParameter(key, value);
					nvps.add(new BasicNameValuePair(key, value));
				}
				try {
					method.setEntity(new UrlEncodedFormEntity(nvps));
				} catch (UnsupportedEncodingException e) {
					System.out
							.println("Fatal transport error while try connect to ["
									+ url + "]");
					System.out.println("Cause: " + e.getMessage());
					System.out.println("HttpTransport Exception");
					System.out.println(e);
				}
			} else if (obj instanceof byte[]) {
				method.setEntity(new ByteArrayEntity((byte[]) obj));

				System.out.println("Sent Data: \n" + new String((byte[]) obj));

			} else {
				throw new IllegalArgumentException(
						"submit(Object obj): obj should be Map or byte[]");
			}

		}

		int statusCode = 0;
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(method);
			statusCode = response.getStatusLine().getStatusCode();
		} catch (IOException e) {
			System.out.println("Fatal transport error while try connect to ["
					+ url + "]");
			System.out.println("Cause: " + e.getMessage());
			System.out.println("HttpTransport Exception");
			System.out.println(e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				System.out.println("HttpTransport Exception");
				System.out.println(e);
			}
		}

		return statusCode;
	}

	/**
	 * 文件下载
	 *
	 * @param url
	 * @return
	 */
	public InputStream submitGetFile(String url) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = client.execute(httpget);

			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			return is;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * HTTP请求
	 *
	 * @param obj
	 * @return
	 * @throws CommunicationException
	 */
	public String submit(Object obj) throws CommunicationException {
		CloseableHttpClient httpClient = HttpClients
				.custom()
				.setRetryHandler(
						new DefaultHttpRequestRetryHandler(retryConnTimes,
								false)).build();
		HttpPost method = this.method;
		if (method == null) {
			method = new HttpPost(url);

			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(timeout).setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout).build();
			if (StringUtils.isNotEmpty(this.sendEncoding)) {
				method.setHeader(HTTP.CONTENT_ENCODING, sendEncoding);
			}
			method.setHeader(HTTP.USER_AGENT, "Rich Powered/1.0");

			method.setConfig(requestConfig);
		}
		if (obj != null) {
			if (obj instanceof Map) {
				Map<String, String> paraMap = (Map<String, String>) obj;
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();

				for (Iterator<String> iter = paraMap.keySet().iterator(); iter
						.hasNext();) {
					String key = iter.next();
					String value = "";
					if (paraMap.get(key) != null) {
						value = paraMap.get(key);
					}

					nvps.add(new BasicNameValuePair(key, value));
				}

				try {
					if (StringUtils.isNotEmpty(this.sendEncoding))
						method.setEntity(new UrlEncodedFormEntity(nvps,
								sendEncoding));
					else
						method.setEntity(new UrlEncodedFormEntity(nvps));
				} catch (UnsupportedEncodingException e) {

					System.out
							.println("Fatal transport error while try connect to ["
									+ url + "]");
					System.out.println("Cause: " + e.getMessage());
					System.out.println("HttpTransport Exception");
					System.out.println(e);
				}
			} else if (obj instanceof byte[]) {
				method.setEntity(new ByteArrayEntity((byte[]) obj));
				System.out.println("Sent Data: \n" + new String((byte[]) obj));
			} else if (obj instanceof String) {
				try {
					StringEntity entity = new StringEntity(obj.toString(),
							this.sendEncoding);
					if (this.sendEncoding != null) {
						entity.setContentEncoding(this.sendEncoding);
					}
					entity.setContentType("application/json");
					method.setEntity(entity);
				} catch (Exception e) {
					System.out.println("设置参数值出错");
					System.out.println(e);
				}
			} else {
				throw new IllegalArgumentException(
						"submit(Object obj): obj should be Map or byte[]");
			}
		}

		int statusCode = 0;
		String result = "";
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(method);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, sendEncoding);
			System.out.println("Received Data: \n" + result);
		} catch (IOException e) {
			System.out.println("Fatal transport error while try connect to ["
					+ url + "]");
			System.out.println("Cause: " + e.getMessage());
			System.out.println("HttpTransport Exception");
			System.out.println(e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					System.out.println("HttpTransport Exception");
					System.out.println(e);
				}
			}
		}
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("Answer from [" + url + "] status code: ["
					+ statusCode + "]");
			throw new CommunicationException(String.valueOf(statusCode));
		}

		return result;
	}

	/**
	 * HTTP请求
	 *
	 * @param obj
	 * @return
	 * @throws CommunicationException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public String submit_https(Object obj) throws CommunicationException,
			NoSuchAlgorithmException, KeyManagementException {

		SSLContext sslContext = SSLContext
				.getInstance(SSLConnectionSocketFactory.TLS);
		sslContext.init(null, trustAllCerts, new SecureRandom());
		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
				sslContext, hostnameVerifier);

		CloseableHttpClient httpClient = HttpClients
				.custom()
				.setSSLSocketFactory(socketFactory)
				.setRetryHandler(
						new DefaultHttpRequestRetryHandler(retryConnTimes,
								false)).build();
		HttpPost method = this.method;
		if (method == null) {
			method = new HttpPost(url);

			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(timeout).setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout).build();
			if (StringUtils.isNotEmpty(this.sendEncoding)) {
				method.setHeader(HTTP.CONTENT_ENCODING, sendEncoding);
			}

			method.setHeader(HTTP.USER_AGENT, "Rich Powered/1.0");

			method.setConfig(requestConfig);
		}
		if (obj != null) {
			if (obj instanceof Map) {
				Map<String, String> paraMap = (Map<String, String>) obj;
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();

				for (Iterator<String> iter = paraMap.keySet().iterator(); iter
						.hasNext();) {
					String key = iter.next();
					String value = "";
					if (paraMap.get(key) != null) {
						value = paraMap.get(key);
					}

					nvps.add(new BasicNameValuePair(key, value));
				}

				try {
					if (StringUtils.isNotEmpty(this.sendEncoding))
						method.setEntity(new UrlEncodedFormEntity(nvps,
								sendEncoding));
					else
						method.setEntity(new UrlEncodedFormEntity(nvps));
				} catch (UnsupportedEncodingException e) {
					System.out
							.println("Fatal transport error while try connect to ["
									+ url + "]");
					System.out.println("Cause: " + e.getMessage());
					System.out.println("HttpTransport Exception");
					System.out.println(e);
				}
			} else if (obj instanceof byte[]) {
				method.setEntity(new ByteArrayEntity((byte[]) obj));
				System.out.println("Sent Data: \n" + new String((byte[]) obj));
			} else if (obj instanceof String) {
				try {
					StringEntity entity = new StringEntity(obj.toString(),
							this.sendEncoding);
					if (this.sendEncoding != null) {
						entity.setContentEncoding(this.sendEncoding);
					}
					entity.setContentType("application/json");
					method.setEntity(entity);
				} catch (Exception e) {
					System.out.println("设置参数值出错");
					System.out.println(e);
				}
			} else {
				throw new IllegalArgumentException(
						"submit(Object obj): obj should be Map or byte[]");
			}
		}

		int statusCode = 0;
		String result = "";
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(method);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			result = EntityUtils.toString(responseEntity, sendEncoding);
			System.out.println("Received Data: \n" + result);
		} catch (IOException e) {
			System.out.println("Fatal transport error while try connect to ["
					+ url + "]");
			System.out.println("Cause: " + e.getMessage());
			System.out.println("HttpTransport Exception");
			System.out.println(e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					System.out.println("HttpTransport Exception");
					System.out.println(e);
				}
			}
		}
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("Answer from [" + url + "] status code: ["
					+ statusCode + "]");
			throw new CommunicationException(String.valueOf(statusCode));
		}

		return result;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setSendEncoding(String sendEncoding) {
		this.sendEncoding = sendEncoding;
	}

	public void setRetryConnTimes(int retryConnTimes) {
		this.retryConnTimes = retryConnTimes;
	}

	public void setMethod(HttpPost method) {
		this.method = method;
	}
}
