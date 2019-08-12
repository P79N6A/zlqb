package com.nyd.msg.service;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.fluent.Request;

/**
 * 演示明码发送短信，不建议在生产环境使用。但可以在浏览器上手动测试
 */
public class SendDemoTC {

	public static void main(String[] args) throws Exception {

		// ====================================================================
		// 本例子只是用于演示，请不要在实际生产中使用
		// ====================================================================
		long startTime = System.currentTimeMillis();
		// 必须的验证参数
		String username = "020686";
		String password = "fk29ge35";
		String url = "http://101.227.68.68:7891/mt?";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStamp = sdf.format(new Date());
		// 短信相关的必须参数
		String mobile = "15618624753";
//		String extendCode = "4443";
		String message = "测试hahaqq";
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update( username.getBytes("utf8") );
		md5.update( password.getBytes("utf8") );
		md5.update( timeStamp.getBytes("utf8") );
		md5.update( message.getBytes("utf8") );
		password = Base64.encodeBase64String( md5.digest() );
		System.out.println(password);
		System.out.println(URLEncoder.encode(message, "utf8"));
		// 装配GET所需的参数
		StringBuilder sb = new StringBuilder();
		sb.append(url)
				.append("un=").append( username )
				.append("&dc=").append(15)
				.append("&pw=").append( URLEncoder.encode(password,"utf8") )
				.append("&ts=").append( timeStamp )
				.append("&da=").append( mobile )
				.append("&tf=").append(3)
				.append("&rf=").append(2)
				.append("&sm=").append( URLEncoder.encode(message, "utf8") );

		String request = sb.toString();
		System.out.println( request );

		// 以GET方式发起请求
		String result = Request.Get( request ).execute().returnContent().asString();

		System.out.println(result);
		System.out.println(System.currentTimeMillis()-startTime);
	}

}
