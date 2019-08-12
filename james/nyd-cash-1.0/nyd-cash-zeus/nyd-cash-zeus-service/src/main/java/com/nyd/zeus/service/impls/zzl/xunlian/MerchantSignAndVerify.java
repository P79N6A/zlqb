package com.nyd.zeus.service.impls.zzl.xunlian;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileVO;
import com.nyd.zeus.service.impls.zzl.chanpay.PayConfigFileService;

import cfca.sadk.algorithm.common.Mechanism;
import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.lib.crypto.JCrypto;
import cfca.sadk.lib.crypto.Session;
import cfca.sadk.util.Signature;
import cfca.sadk.x509.certificate.X509Cert;

@Service
public class MerchantSignAndVerify {
	
	@Autowired
	private PayConfigFileService payConfigFileService;
	
	public static final Logger logger = LoggerFactory.getLogger(MerchantSignAndVerify.class);
	private static Session session = null;
	private static String cryptoType = JCrypto.JSOFT_LIB;
	
	private static String XUNLIAN_PAY = "200008-0001";

	//私钥
	private static PrivateKey priKey = null;
	private static String priKeyPass = "123";
	private static String priKeyPath = null;
	private static String pubKeyPath = null;
    //公钥
    private static X509Cert cert = null;
    //证书配置文件名
    //private static String certConfigName = "cert-config.properties";
	static{
		try{
			JCrypto.getInstance().initialize(cryptoType, null);
			session = JCrypto.getInstance().openSession(cryptoType);
		}catch (Exception e){
			logger.error("初始化session出错");
			e.printStackTrace();
		}
		/**读取证书配置文件信息*/
		//InputStream in = null;
		try {

			//in = MerchantSignAndVerify.class.getClassLoader().getResourceAsStream(certConfigName);
			//Properties ps = new Properties();
			//ps.load(in);
//			priKeyPass = ps.getProperty("cert_pwd");
//			String useSwitch = ps.getProperty("use_switch");
//			if ("0".equals(useSwitch)) {
//				priKeyPath = ps.getProperty("linux_merchant_cert_path");
//				pubKeyPath = ps.getProperty("linux_platform_cert_path");
//			} else {
//				priKeyPath = ps.getProperty("window_merchant_cert_path");
//				pubKeyPath = ps.getProperty("window_platform_cert_path");
//			}
		} catch (Exception e) {
			
		} finally {}
	}
	
	/**
	 * 将明文加签得到摘要密文
	 * @param plain
	 */
	public static byte[] sign(String plain, String merchantId,PayConfigFileVO payConfigFileVO){
		byte[] signByte = null;
		try{
			priKeyPath = payConfigFileVO.getPrdKey();
			pubKeyPath = payConfigFileVO.getPubKey();
			priKeyPass = payConfigFileVO.getChannel();
			byte[] plainByte = plain.getBytes("UTF-8");
			priKey = GetPvkformPfx(merchantId, priKeyPass);
			signByte = new Signature().p1SignMessage(Mechanism.SHA1_RSA, plainByte, priKey, session);
		} catch (Exception e) {
			logger.error("签名出现异常",e);
		}
		logger.info("明文串：" + plain);
		logger.info("密文串：" + new String(signByte));
		return signByte;
	}
	
	
	public static boolean verify(byte[] source, byte[] signatureBase64){
		InputStream is = null;
		try {
			//is = ClassUtils.class.getResourceAsStream("/xunlianFiles/zt_test.cer");
			is = ClassUtils.class.getResourceAsStream(pubKeyPath);
			//is = new FileInputStream(pubKeyPath);
			cert =  new X509Cert(is);
			return new Signature().p1VerifyMessage(Mechanism.SHA1_RSA, source, signatureBase64, cert.getPublicKey(), session);
		} catch (PKIException e) {
			logger.error("验签出错！", e);
			return false;
		} catch (Exception e) {
			logger.error("验签出错！", e);
			return false;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("关闭流失败",e);
				}
			}
		}
	}
	
	/** 
     * 除去数组中的空值和签名参数
     * @param params 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> params) {

        Map<String, String> result = new HashMap<String, String>();
        if(params==null || params.size()<=0){
            return result;
        }

        for(String key: params.keySet()){
            String value = params.get(key);
            if (value == null || value.equals("") || value.equalsIgnoreCase("null") 
            	|| key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")){
                continue;
            }
            result.put(key, value);
        }
        return result;
        
    }
    
	/**
	 * 生成加签、验签字符串
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串，String自带排序方法排序
	 * @param params
	 * @return
	 */
    public static String createLinkString(Map<String, String> params) {
    	//直接过滤无效信息
    	params = paraFilter(params);
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i=0; i<keys.size(); i++) {
        	String key = keys.get(i);
            String value = params.get(key);
            //空串不参与加签 、验签
         	if(!StringUtils.isEmpty(value)){
         		 if(i == keys.size() - 1){//拼接时，最后一个字段后不包括&字符
                     prestr = prestr + key + "=" + value;
                 }else{
                     prestr = prestr + key + "=" + value + "&";
                 }
        	}
        }
        logger.info("拼接等待加签、验签的字符串为：{}", prestr);
        return prestr;
    }
    
    private static PrivateKey GetPvkformPfx(String merchantId, String strPassword){
    	
        try { 
            KeyStore ks = KeyStore.getInstance("PKCS12");  
            //InputStream fis = MerchantSignAndVerify.class.getClassLoader().getResourceAsStream("cert/" + merchantId + ".pfx");
            //InputStream fis = new FileInputStream(priKeyPath+File.separator+merchantId + ".pfx");  
            //InputStream fis = ClassUtils.class.getResourceAsStream("/xunlianFiles/" + merchantId + ".pfx");
            
            InputStream fis = ClassUtils.class.getResourceAsStream(priKeyPath);

            // If the keystore password is empty(""), then we have to set  
            // to null, otherwise it won't work!!!  
            char[] nPassword = null;  
            if ((strPassword == null) || strPassword.trim().equals("")){  
                nPassword = null;  
            } else {  
                nPassword = strPassword.toCharArray();  
            }  
            ks.load(fis, nPassword);  
            fis.close();  
            // Now we loop all the aliases, we need the alias to get keys.  
            // It seems that this value is the "Friendly name" field in the  
            // detals tab <-- Certificate window <-- view <-- Certificate  
            // Button <-- Content tab <-- Internet Options <-- Tools menu   
            // In MS IE 6.  
            Enumeration<?> enumas = ks.aliases();  
            String keyAlias = null;  
            if (enumas.hasMoreElements())// we are readin just one certificate.  
            {  
                keyAlias = (String)enumas.nextElement(); 
                logger.info("alias=[" + keyAlias + "]");
            }  
            // Now once we know the alias, we could get the keys.  
            PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword); 
            //这里也可以获取公钥
//            Certificate cert = ks.getCertificate(keyAlias); 
//            PublicKey pubkey = cert.getPublicKey();  
            return prikey;  
        }  
        catch (Exception e) {  
            logger.error("获取私钥发生异常",e);  
        }  
        return null;  
    }
    
    
    public static void main(String[] args){
//    	String plain = "account=6221881811022564132&accountType=90&amount=66.66&idCode=142727199008030020&idType=10&merchantId=000010056&mobile=13028011546&name=李亚琴&organCode=4001800003&payOrderId=20170412163936&protocolId=201702239900001304&version=1.0.1";
//    	byte[] signature = sign(plain, "000010056");
//    	System.out.println(signature.toString());
		/*Properties p = System.getProperties();
		for (Iterator iterator = p.entrySet().iterator(); iterator.hasNext();) {
			Object type = iterator.next();
			System.out.println(type);
		}*/
//    	String plain = "merchantId=000010395&orderId=201705240000001&reqDate=20170605 16:34:41&type=1&version=1.0.1";
    	
    }
    
}
