package com.nyd.zeus.model.helibao.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DemoBase {

	private static final Logger logger = LoggerFactory.getLogger(DemoBase.class);

	public static final String r1_merchantNoAppPay="C1800001110";

	//绑卡支付请求地址

//	public static final String REQUEST_URL = "http://pay.trx.helipay.com/trx/quickPayApi/interface.action";
//	public static final String REQUEST_URL = "http://test.trx.helipay.com/trx/authentication/interface.action";
//
//	public static final String signkey="qWDqC8SEbz0yymyOmE2i45QvwmKYj5ur";
	public static final String signkey="KajLj5AUudD26wyiPgZz0a0vIifZAVB8";

//	public static final String REQUEST_URL = "http://192.168.33.22:18080/trx/entrustedLoan/interface.action";
//	public static final String REQUEST_FILEURL = "http://192.168.33.22:18080/trx/entrustedLoan/upload.action";

	public static final String REQUEST_URL = "http://192.168.42.14:41292/trx/entrustedLoan/interface.action";
	public static final String REQUEST_FILEURL = "http://192.168.42.14:41292/trx/entrustedLoan/upload.action";

	public static final String signKey_private ="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANsLUARqoUKaFDHEO71wWa4H/0PuUavdBorsbPUiPz9Z5hG1kms+ugDt8n4xt+nmGPAQ9znW+iOABSDV9U2t7oAkK3eb/YLpcLCwDiU/JcTmF0ryABbq7o/xLYbWVVQ27tFPJsDARDdFl3pZQBHvNCRpVYwluw419NugFR5smhL7AgMBAAECgYEAm39AYqI/sEQpKdsgfa8QOtc6g0Lff538UwrbnnmdZJ+xD8lSFAgHizlf9+3bnZPXCDVDDDBsil8LRPAen/JlaAnLtfo8OODWHIlyBkacTfwAKMMeLWajvMxnNsAVqg0BhniE1wj5ANbVniE3Y20iJzSJrjs7s0Xo0onqPjn5cGECQQD2WvcuCvIZSpOtW6sihpf1ejPit4vAkaYYT1WJlWZ2ax/q7+TT1OYK9HlQSqZMn1llzS9OE0Mw0y09KR5raLg3AkEA456gwG3v+qfxkKBi3ZewVrqZ+uLEDqVaGR2Qf17rEvd9vpHZ+glxwszLxuA4nNmSpyxYU0aavjE9alAfloiRXQJBAKLYRJYp5Bi1xGKOutQqth4tNkkCXR3g6TMelgNJP569dy68fr1L+Ph63AooHj9+AgEaHc/1Sa/nl/rayGHlqgcCQGsth030tjoVmFZcXxEuWtcLuHN9EYo/68PFuwNuo0vkA26XaMcmaU2rdwxnCeE5Rae35sMXjMefkeacYbNVDPUCQQDk6IQt3x9Or+S56UXrLIf7OwipYMMmJfrvFKFpY5RlFJjs/RPoUcr+J0+sdUtBRhZE/GGvsj8SFjvPjPlGcqCb";
//	public static final String signKey_private ="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAM+PcQN9JmrYJ+sKwI9kmBLAsBS6HHDT2Sgkh8BYbyTKovSMQat1II6l42HhUH6lr7bXwnRUw8I4qrNBZjz4cWZBi+vgkSL/1f1M/erW27t61DobvpgDpZQTtmQ7IDsiLuY7C7We+WwaBcddnju74ij3FPWCpgYBHGwdv5wwzRxdAgMBAAECgYBAzSreiPsujm/gDQpTeneUGz6eKgDpJOr+gnEzlyiUFwPLT+LM0hOpFZepHnxQHhB/CFu4kCJSB/kbYAa4cGSOlPo8zBLCfNajClZMLaKMAIb+0TmYNAnVcadC/4fXibzAW0zRS2/OK4H7wWUVEYyC66m+ieBaH5Jt/72+e6aYTQJBAPjjhGanLk22ml8i5+MzN94RBQStbGNxI6xtBXoKEIB2W/INPddZ877e7tknh+fVvctTZlE4Q5V1TT2ZL4wzke8CQQDVfaE9Cbc+aeg3Mb+Ap64tCK4WTHhWzHySN7VGTLdeF41ZjqTrIS7SSQyZOPOt/lMfFgXO0EnSdCqL+aexXFJzAkBeHyxi5bZNDVEzyS+IbEYkZKtRKYRj1tV2z4PSsxuqeRgsYXWRiyLye7w3wwtSUTKFQfTfojdsvf+H2/ZvPtFhAkAMygfctjZKAOIuXEaSmHjwrbJwF4il+n4D7F5ppbLeah7HnKn4g/ZgFowwqZ6/b5rfI9yZNRUXDGp4FC6di2BNAkB572zRbBT5Ot9mx9xVg6g/t0s3+LLEs1LBFEWQatRR9oC6qUzGNKTnZ/d5254ngnYXSRaQEZT698cJQV7kvmg4";

	public static final String deskey_key = "xglHJa0H1QKQi1Z9aaqnL0l2";

	public static final String split="&";

	public static void main(String[] args) {
	
	}
	
	public Map<String, Object> getPostJson(HttpServletRequest request) throws IOException {
		java.io.ByteArrayOutputStream inBuffer = new java.io.ByteArrayOutputStream();
		java.io.InputStream input = request.getInputStream();
		byte[] tmp = new byte[1024];
		int len = 0;
		while ((len = input.read(tmp)) > 0) {
			inBuffer.write(tmp, 0, len);
		}
		byte[] requestData = inBuffer.toByteArray();
		String requestJsonStr = new String(requestData, "UTF-8");
		logger.info(requestJsonStr);
		JSONObject requestJson = JSON.parseObject(requestJsonStr);
		logger.info(requestJson.toString());
		return parseJSON2Map(requestJson);
	}
	
	public Map<String, Object> parseJSON2Map(JSONObject json) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (json != null) {
			for (Object k : json.keySet()) {
				Object v = json.get(k);
				// 如果内层还是数组的话，继续解析
				if (v instanceof JSONArray) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					Iterator<Object> it = ((JSONArray) v).iterator();
					while (it.hasNext()) {
						JSONObject json2 = (JSONObject) it.next();
						list.add(parseJSON2Map(json2));
					}
					map.put(k.toString(), list);
				} else {
					map.put(k.toString(), v);
				}
			}
		}
		logger.info(map.toString());
		return map;
	}
}
