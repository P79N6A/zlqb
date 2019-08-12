package com.nyd.capital.service.dld.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nyd.capital.service.dld.config.AppConst;

public class D2DUtil {
	protected static Logger _logger = LoggerFactory.getLogger(D2DUtil.class);

	/**
	 * 将map转化为形如key1=value1&key2=value2...
	 *
	 * @param map
	 * @return Url String
	 */
	public static String mapToUrlString(Map<String, String> map) {
		if (null == map || map.keySet().size() == 0) {
			return "";
		}

		StringBuffer url = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String value = entry.getValue();
			String str = (value != null ? value : "");
			try {
				str = URLEncoder.encode(str, AppConst._Encoding);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			url.append(entry.getKey()).append("=").append(str).append(AppConst.URL_PARAM_CONNECT_FLAG);
		}

		// 最后一个键值对后面的“&”需要去掉。
		String strURL = "";
		strURL = url.toString();
		if (AppConst.URL_PARAM_CONNECT_FLAG.equals("" + strURL.charAt(strURL.length() - 1))) {
			strURL = strURL.substring(0, strURL.length() - 1);
		}
		return (strURL);
	}

	public static boolean checkSignature(Map<String, Object> map, String sign) {
		boolean flag = true;
		Set<String> removeKey = new HashSet<String>();
		String key = map.get("dldKey").toString();
		removeKey.add("signature");
		removeKey.add("dldKey");
		String signstr = getSignStr(map, removeKey);
		signstr = (signstr+key).toLowerCase();
		_logger.info("验签开始，加签字符串：" + signstr );
		String md5str = DigestUtils.md5Hex(signstr).toUpperCase();
		_logger.info("加密后字符串：" + md5str + "原加密字符串：" + sign);
		if(md5str != null && md5str.equals(sign)) {
			return flag;
		}
		flag = false;
		return flag;

	}

	/**
	 * 设置签名，支持RSA和MD5
	 *
	 * @param paramMap
	 */
	public static void setSignature(Map<String, Object> paramMap, String shopKey) {
		Set<String> removeKey = new HashSet<String>();
		removeKey.add("Signature");
		setSignature(paramMap, removeKey, shopKey);
	}
	/**
	 * 设置签名，支持RSA和MD5
	 *
	 * @param paramMap
	 */
	public static void setSignatureWithString(Map<String, String> paramMap, String shopKey) {
		Set<String> removeKey = new HashSet<String>();
		removeKey.add("Signature");
		setSignatureWithString(paramMap, removeKey, shopKey);
	}

	/**
	 * 设置签名，支持RSA和MD5
	 *
	 * @param paramMap
	 */
	public static void setSignature(Map<String, Object> paramMap, Set<String> removeKey, String shopKey) {
		removeKey.add("Signature");
		String signMsg = getSignStr(paramMap, removeKey).toLowerCase();
		String signature = DigestUtils.md5Hex(signMsg);

		_logger.info("签名字符串：[{}], 验证字符串：[{}]", signMsg, signature);
		paramMap.put("Signature", signature);

	}
	/**
	 * 设置签名，支持RSA和MD5
	 *
	 * @param paramMap
	 */
	public static void setSignatureWithString(Map<String, String> paramMap, Set<String> removeKey, String shopKey) {
		removeKey.add("Signature");
		String signMsg = getSignStr(paramMap, removeKey).toLowerCase();
		String signature = DigestUtils.md5Hex(signMsg);

		_logger.info("签名字符串：[{}], 验证字符串：[{}]", signMsg, signature);
		paramMap.put("Signature", signature);

	}

	public static String postFileMultiPart(String url, Map<String, ContentBody> reqParam)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			// 创建httpget.
			HttpPost httppost = new HttpPost(url);

			// setConnectTimeout：设置连接超时时间，单位毫秒。setConnectionRequestTimeout：设置从connect
			// Manager获取Connection
			// 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。setSocketTimeout：请求获取数据的超时时间，单位毫秒。
			// 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
			RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(5000)
					.setConnectionRequestTimeout(5000).setSocketTimeout(15000).build();
			httppost.setConfig(defaultRequestConfig);

			_logger.info("executing request " + httppost.getURI());

			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			for (Map.Entry<String, ContentBody> param : reqParam.entrySet()) {
				multipartEntityBuilder.addPart(param.getKey(), param.getValue());
			}
			HttpEntity reqEntity = multipartEntityBuilder.build();
			httppost.setEntity(reqEntity);

			// 执行post请求.
			CloseableHttpResponse response = httpclient.execute(httppost);

			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				if (entity != null) {
					return EntityUtils.toString(entity, Charset.forName("UTF-8"));
				}
			} finally {
				response.close();
			}
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @param map       params
	 * @param removeKey removeKey
	 * @return string
	 */
	private static String getSignStr(Map map, Set removeKey) {
		StringBuffer retValue = new StringBuffer();
		List msgList = new ArrayList();
		Iterator it = map.keySet().iterator();

		while (true) {
			String msg;
			String value;
			do {
				if (!it.hasNext()) {
					Collections.sort(msgList);

					for (int i = 0; i < msgList.size(); ++i) {
						msg = (String) msgList.get(i);
						if (i > 0) {
							// retValue.append("&");
						}

						if(!msg.endsWith("=")) {
							retValue.append(msg.split("=")[1]);
						}
					}

					return retValue.toString();
				}

				msg = (String) it.next();
				value = (String) map.get(msg);
			} while (removeKey != null && removeKey.contains(msg));

			msgList.add(msg + "=" + toEmpty(value));
		}
	}

	public static String toEmpty(String aStr) {
		return aStr == null ? "" : aStr;
	}

	public static String encodeBase64Str(String plainText) throws UnsupportedEncodingException {
		return org.apache.commons.codec.binary.Base64.encodeBase64String(plainText.getBytes(AppConst._Encoding));
	}

	// 图片到byte数组
	public static byte[] image2byte(String path) {
		byte[] data = null;
		FileImageInputStream input = null;
		try {
			input = new FileImageInputStream(new File(path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numBytesRead = 0;
			while ((numBytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, numBytesRead);
			}
			data = output.toByteArray();
			output.close();
			input.close();
		} catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		} catch (IOException ex1) {
			ex1.printStackTrace();
		}
		return data;
	}

	public static void close(Closeable x) {
		if (x == null) {
			return;
		}

		try {
			x.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// file
			if (fileMap != null) {
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					String contentType = new MimetypesFileTypeMap().getContentType(file);
					if (filename.endsWith(".png")) {
						contentType = "image/png";
					}
					if (contentType == null || contentType.equals("")) {
						contentType = "application/octet-stream";
					}

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

	public static String generateOrderID() {
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Random rad=new Random();
		String result  = rad.nextInt(99) +"";
		if(result.length()==1){
			result = "0" + result;
		}
		return format.format(Calendar.getInstance().getTime())+result;
	}

}
