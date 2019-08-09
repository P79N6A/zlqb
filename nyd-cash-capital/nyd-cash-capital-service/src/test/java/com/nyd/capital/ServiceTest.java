package com.nyd.capital;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.model.qcgz.enums.BankNameResetEnum;
import com.nyd.capital.service.dld.service.DldService;
import com.nyd.capital.service.dld.utils.D2DUtil;
import com.nyd.capital.service.dld.utils.DateUtil;
import com.nyd.capital.service.dld.utils.HttpClientUtil;
import com.nyd.capital.service.dld.utils.JsonUtils;
import com.nyd.capital.service.dld.utils.RSAUtil;
import com.nyd.capital.service.utils.HttpsUtils;
import com.tasfe.framework.support.model.ResponseData;

import sun.misc.BASE64Encoder;

/**
 * Cong Yuxiang
 * 2018/5/10
 **/

public class ServiceTest {
	

    @Autowired
    private RestTemplate restTemplate;
	
	public static final String url = "https://m.daidailink.com:8443/Service/RegisterMerUserInfo";
	public static final String version = "1.0.0";
	public static final String UserId = "200008";
	public static final String UserKey = "FYTCP99Y21MY2ZPUKGBHRFI6SDXH23KN";
	public static final String PpFlag = "01";
	public static final String sign = "RSA";
	public static final String PKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLfxzs5vB2Zg4r1U+XOQSilsfQ22PhpTcgu7hsmfC505TobUbDHWnpy+WZ2+wAxPDIyv6rjsJ/Zc9hQYCScNRFR4eVidisHuZ1BLGFNd7aGxkY3kWIrB7haljDIN76OUOyB9kZQ/sjGIYkcSRA5oyCV/5idaRK6DC+xGXI3DxPuQIDAQAB";
	public static final String RealName = "龙千秋";
	public static final String IdentityNumber = "450481199212222460";
	public static final String Phone = "13200000000";
	public static final String PhotoOne = "D:\\one.png";
	public static final String PhotoTwo = "D:\\two.png";
	
	private static String shopID = "200027";
    private static String shopKEY = "EFEFD8F6F81044938DEBDCD423210E6B";
    private static String shopRSA = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbGSFZWzlUa4wFpj0l8Es+g8d80GZ1U6oyD41EGDdvLTbaxrJjb6zJIZt/xsAsNnVSpXvudLksJL/zaMfTWA0n3XwidjEX4KNKv6AGBzCAFIhxF+bDjkUbeBfgLKPsPJ6EJtou7Q6kjWwq521sTHgjd/veOknBSQqXZmWtPev7wwIDAQAB";
    private static String pTpMerId = "929000095015427";
    private static String testUrl = "https://shop.daidailink.com";
    private static String shopVERSION = "1.0.0";
    private static String pRealName = "龙千秋";
    private static String pIdentityNumber = "450481199212222460";
    private static String pPhone = "13912341234";
    private static String pPpFlag = "01";  //对公对私	string	必填	(00=对公 01=对私)
    private static String pSex = "1";  //性别		string	选填	(0:女1:男)
    private static String pBirthday = "119700101";  //出生日期	string	选填	(格式：yyyyMMdd)
    private static String pCompanyName = ""; //对公必填(base64编码)
    private static String pUSCI = "";  //统一社会信用代码	string	对公必填
    private static String pAddress = ""; //选填
    private static String pEducation = "";  //选填
    
    @Autowired
    private DldService dldService;
    
    @Test
    public void test04() {
    	dldService.registerUserByUserId("181312000001","1111");
    }
    @Test
    public void test004() {
    	String merOrderNo = "101544585429327001";
    	String orderNo = merOrderNo.split("_")[0];
    	System.out.println(merOrderNo);
    	System.out.println(orderNo);
    }
    @Test
    public void test05() {
    	String s = "101545896886140001_1545897122535,101545897101853001_1545897113521,101545897232486001_1545897322775,101545897268473001_1545897462578,101545897371197001_1545897497703,101545897585573001_1545897594366,101545897620240001_1545897626164";
    	Map<String, Object> signParam = new HashMap<String, Object>();
        signParam.put("Version", shopVERSION);
        signParam.put("UserId", shopID);
        signParam.put("UserKey", shopKEY);
        signParam.put("Signature", shopKEY);
    	String[] ss = s.split(",");
    	for(String or:ss) {
    		 String retVal = "";
    				try {
    					signParam.put("MerOrderNo", or);
						retVal = HttpClientUtil.post(testUrl + "/Service/LoanOrderQuery", signParam);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    	        System.out.println(retVal);
    	}
    	
    }
    
    
    @Test
    public void test03() {
    	File loanerPhotoOne = new File("D:\\one.png");
        File loanerPhotoTwo = new File("D:\\two.png");
        File loanerPhotoThree = new File("D:\\two.png");
    	Map<String, Object> signParam = new HashMap<String, Object>();

        signParam.put("Version", shopVERSION);
        signParam.put("UserId", shopID);
        signParam.put("UserKey", shopKEY);
        signParam.put("Signature", shopKEY);
        signParam.put("RealName", pRealName);
        signParam.put("IdentityNumber", pIdentityNumber);
        signParam.put("Phone", pPhone);
        signParam.put("PpFlag", pPpFlag);
        //signParam.put("CompanyName", pCompanyName);
        //signParam.put("USCI", pUSCI);
        //signParam.put("Address", pAddress);
        signParam.put("Sex", pSex);
        signParam.put("Birthday", pBirthday);
        //signParam.put("Education", pEducation);

        Set<String> removeKey = new HashSet<String>();
        removeKey.add("Signature");
        removeKey.add("PhotoOne");
        removeKey.add("PhotoTwo");
        removeKey.add("PhotoThree");

        D2DUtil.setSignature(signParam, removeKey, shopKEY);

        Map<String, ContentBody> reqParam = new HashMap<String, ContentBody>();
        reqParam.put("Version", new StringBody(shopVERSION, ContentType.DEFAULT_TEXT));
        reqParam.put("UserId", new StringBody(shopID, ContentType.DEFAULT_TEXT));
        reqParam.put("UserKey", new StringBody(shopKEY, ContentType.DEFAULT_TEXT));
        reqParam.put("Signature", new StringBody(String.valueOf(signParam.get("Signature")), ContentType.DEFAULT_TEXT));
        reqParam.put("RealName", new StringBody(RSAUtil.EncodePwd(PKey, pRealName), ContentType.DEFAULT_TEXT));
        reqParam.put("IdentityNumber", new StringBody(RSAUtil.EncodePwd(PKey, pIdentityNumber), ContentType.DEFAULT_TEXT));
        reqParam.put("Phone", new StringBody(RSAUtil.EncodePwd(PKey, pPhone), ContentType.DEFAULT_TEXT));
        reqParam.put("PpFlag", new StringBody(pPpFlag, ContentType.DEFAULT_TEXT));

        reqParam.put("PhotoOne", new FileBody(loanerPhotoOne, ContentType.DEFAULT_BINARY));
        reqParam.put("PhotoTwo", new FileBody(loanerPhotoTwo, ContentType.DEFAULT_BINARY));
        reqParam.put("PhotoThree", new FileBody(loanerPhotoThree, ContentType.DEFAULT_BINARY));
        //reqParam.put("CompanyName", new StringBody(Base64.encodeBase64String(pCompanyName.getBytes(AppConst._Encoding)), ContentType.DEFAULT_TEXT));
        //reqParam.put("USCI", new StringBody(pUSCI, ContentType.DEFAULT_TEXT));
        //reqParam.put("Address", new StringBody(pAddress, ContentType.DEFAULT_TEXT));
        reqParam.put("Sex", new StringBody(pSex, ContentType.DEFAULT_TEXT));
        reqParam.put("Birthday", new StringBody(pBirthday, ContentType.DEFAULT_TEXT));
        //reqParam.put("Education", new StringBody(pEducation, ContentType.DEFAULT_TEXT));

        String retVal = "";
		try {
			retVal = HttpClientUtil.postFileMultiPart(testUrl + "/Service/RegisterMerUserInfo", reqParam);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(retVal);
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test01() {
		Map<String,Object> request = new HashMap<String,Object>();
		request.put("Version", version);
		request.put("UserId", UserId);
		request.put("UserKey", UserKey);
		request.put("Signature", "");
		request.put("RealName", RealName);
		request.put("IdentityNumber", IdentityNumber);
		request.put("Phone", Phone);
		request.put("PpFlag", PpFlag);
		try {
			request.put("PhotoOne", InputStream2ByteArray(PhotoOne));
			request.put("PhotoTwo", InputStream2ByteArray(PhotoTwo));
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		Map<String, String> headMap = new HashMap<>();
        headMap.put("Content-Type", "multipart/form-data");
        String r = null;
		try {
			//r = HttpHelper.sendPost(url, request, "utf-8");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        System.out.println(r);
        
		HttpHead header = new HttpHead();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Version", version);
		jsonObject.put("UserId", UserId);
		jsonObject.put("UserKey", UserKey);
		jsonObject.put("Signature", "");
		jsonObject.put("RealName", RealName);
		jsonObject.put("IdentityNumber", IdentityNumber);
		jsonObject.put("Phone", Phone);
		jsonObject.put("PpFlag", PpFlag);
		/*try {
			jsonObject.put("PhotoOne", InputStream2ByteArray(PhotoOne));
			jsonObject.put("PhotoTwo", InputStream2ByteArray(PhotoTwo));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		String str = jsonObject.getString("IdentityNumber")
					+jsonObject.getString("Phone")
					+jsonObject.getString("PpFlag")
					+jsonObject.getString("RealName")
					+jsonObject.getString("UserId")
					+jsonObject.getString("UserKey")
					+jsonObject.getString("Version");
		str = str.toLowerCase();
		try {
			str = EncoderByMd5(str);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*jsonObject.replace("Signature", "", str);*/
		String s = UserKey;
		BASE64Encoder encoder = new BASE64Encoder();
		try {
			s = encoder.encode(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		/*try {
			jsonObject.replace("RealName", RealName, RSAUtils.privateEncrypt(RealName, RSAUtils.getPrivateKey(s)));
			jsonObject.replace("IdentityNumber", IdentityNumber, RSAUtils.privateEncrypt(IdentityNumber, RSAUtils.getPrivateKey(UserKey)));
			jsonObject.replace("Phone", Phone, RSAUtils.privateEncrypt(Phone, RSAUtils.getPrivateKey(UserKey)));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		/*Map<String, String> headMap = new HashMap<>();
        headMap.put("Content-Type", "multipart/form-data");
		String response = HttpsUtils.post(url, headMap, jsonObject);	*/
		Map<String, String> headMap1 = new HashMap<>();
	    headMap1.put("Content-Type", "application/json");
		String response2 = null;
		try {
			response2 = HttpsUtils.post("https://m.daidailink.com:8443", headMap1, new HashMap<String,String>());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		System.out.println(response2);
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(request.toString(), headers);
        ResponseEntity<JSONObject> s2 = restTemplate.exchange("https://m.daidailink.com:8443", HttpMethod.POST, entity, JSONObject.class);
        System.out.println(s2.getBody().toString());
        
        /*HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity entity = new HttpEntity(jsonObject.toString(), headers);
        ResponseEntity<JSONObject> s1 = restTemplate.exchange(url, HttpMethod.POST, entity, JSONObject.class);
		System.out.println(s1.getBody().toString());*/
	}
	
	@Test
	public void test02() {
		try {
			Map<String, String> headMap1 = new HashMap<>();
		    headMap1.put("Content-Type", "application/json");
			//String r = HttpHelper.post(headMap1,new HashMap<String,String>(), "https://m.daidailink.com:8443");
			//System.out.println(r);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**利用MD5进行加密
     * @param str  待加密的字符串
     * @return  加密后的字符串
     * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException  
     */
    public String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }
    
    private byte[] InputStream2ByteArray(String filePath) throws IOException {
    	 File f = new File(filePath);
    	 if(!f.exists()) {
    		 System.out.println("图片不存在");
    	 }
        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();
     
        return data;
    }
     
    private byte[] toByteArray(InputStream in) throws IOException {
     
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
    
    @Test
    public void test06() {
    	String retVal = "{\"data\":{\"respCode\":\"1001\",\"respMsg\":\"注册成功\",\"orderId\":null,\"status\":1,\"data\":{\"customerId\":\"1809143772100011\"}},\"code\":\"0000\",\"msg\":\"请求成功\",\"success\":true}";
    	
    	Object o = JsonUtils.getValue(retVal, 3,new String[]{"data","data","customerId"});
    }
    
    @Test
    public void test07() {
    	String url = "http://p173wa8lq.bkt.clouddn.com/FspTG76VgajJ01VtRlgcu-TS9Wao?e=1537176847&token=Z2Tyi3YA4GQxY0D4tisG6xdisuF2I9K5KFKNNnjw:_YNMi6OBUBSa8_v0w_H62TrfChs=";
    	System.out.println(url.substring(url.indexOf("e=") + 2, url.indexOf("&token")));
    }
    
    @Test
    public  void test08() {
    	File file = new File("D:\\15371796702.temp");
    	if(file.exists()) {
    		System.out.println(file.delete());
    	}
    }
    
    @Test
    public void test09() {
    	System.out.println(getResetBankName("光大银行"));
    }
    
    private String getResetBankName(String bankName) {
    	String reset = "";
    	BankNameResetEnum bank = BankNameResetEnum.getByValue(bankName);
    	if(bank != null) {
    		reset = bank.getResetName();
    	}else {
    		reset = bankName;
    	}
    	return reset;
    }
    
    @Test
    public void test10() {
    	Date date = new Date();
    	System.out.println(DateUtil.parseDateToStr(date, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
    	Date endDate = DateUtils.addMinutes(date, -10);
    	System.out.println(DateUtil.parseDateToStr(date, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
    	System.out.println(DateUtil.parseDateToStr(endDate, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));

    }
    
}
