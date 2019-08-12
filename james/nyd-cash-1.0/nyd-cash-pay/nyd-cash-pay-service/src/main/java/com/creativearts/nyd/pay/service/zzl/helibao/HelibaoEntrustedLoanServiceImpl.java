package com.creativearts.nyd.pay.service.zzl.helibao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserQueryResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserQueryVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserUploadResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserUploadVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.MerchantUserVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.OrderContractSignatureQueryResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.OrderQueryResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.OrderQueryVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.OrderResVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.OrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.P9Ext;
import com.creativearts.nyd.pay.config.utils.zzl.helibao.Des3Encryption;
import com.creativearts.nyd.pay.config.utils.zzl.helibao.Disguiser;
import com.creativearts.nyd.pay.config.utils.zzl.helibao.HttpClientService;
import com.creativearts.nyd.pay.config.utils.zzl.helibao.RSA;
import com.creativearts.nyd.pay.config.utils.zzl.helibao.Uuid;
import com.creativearts.nyd.pay.service.zzl.helibao.util.MyBeanUtils;
import com.nyd.order.entity.zzl.HelibaoFilesConfig;
import com.nyd.pay.api.zzl.HelibaoEntrustedLoanService;
import com.nyd.pay.api.zzl.HelibaoFilesConfigService;
import com.nyd.pay.api.zzl.HelibaoFlowLogService;

@Service("helibaoEntrustedLoanService")
public class HelibaoEntrustedLoanServiceImpl implements HelibaoEntrustedLoanService{

    private static final Logger log = LoggerFactory.getLogger(HelibaoEntrustedLoanServiceImpl.class);

    @Autowired
	private HelibaoFilesConfigService helibaoFilesConfigService;
    
    @Autowired
    private HelibaoFlowLogService helibaoFlowLogService;
    
    private final  String CURRENCY = "CNY";//钱单位
    
    private final String ERROR_CODE = "0002";
    
    public final String SPLIT="&";
    
  
    
    private String moneyDiv = "1000";

    
    //-------------商户用户注册
    @Override
    public MerchantUserResVo userRegister(MerchantUserVo userVo,String custInfoId) {
    	log.info("--------商户用户注册----------"+JSON.toJSONString(userVo, SerializerFeature.WriteMapNullValue));
    	String UUID = Uuid.getUuid26();
    	String TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd_ HH:mm:ss.SSS").format(new Date());
    	 HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
    	 MerchantUserResVo resVo = new MerchantUserResVo();
    	 userVo.setP1_bizType("MerchantUserRegister");
    	 userVo.setP2_customerNumber(config.getCustomerNumber());
    	 userVo.setP3_orderId(UUID);
    	 userVo.setP8_timestamp(TIMESTAMP);
    	 P9Ext ext = null;
    	 if(StringUtils.isNotBlank(userVo.getP9_ext())){
    		 ext = JSONObject.parseObject(userVo.getP9_ext(), P9Ext.class);
    		 ext.setP3_orderId(userVo.getP3_orderId());
    	 }else{
    		 ext = new  P9Ext();
    		 ext.setP3_orderId(userVo.getP3_orderId());
    	 }
    	 String p9Ext = JSON.toJSONString(ext, SerializerFeature.WriteMapNullValue,
                 SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty);
    	 userVo.setP9_ext(p9Ext);
    	 resVo.setRt1_bizType(userVo.getP1_bizType());
    	 resVo.setRt4_customerNumber(userVo.getP2_customerNumber());
    	 resVo.setRt5_orderId(userVo.getP3_orderId());
    	
        //信息域加密
        if (StringUtils.isNotBlank(userVo.getP5_legalPersonID())) {
            userVo.setP5_legalPersonID(Des3Encryption.encode(config.getDeskeyKey(), userVo.getP5_legalPersonID()));
        }
        if (StringUtils.isNotBlank(userVo.getP6_mobile())) {
            userVo.setP6_mobile(Des3Encryption.encode(config.getDeskeyKey(), userVo.getP6_mobile()));
        }
       
		try {
			Map<String, String> map = MyBeanUtils.convertBeanReq(userVo,new LinkedHashMap());
			String oriMessage = MyBeanUtils.getSigned(map, null);
			log.info("签名原文串：" + oriMessage);
			String sign = RSA.sign(oriMessage.trim(),RSA.getPrivateKey(config.getSignkeyPrivate()));
			log.info("签名串：" + sign);
			map.put("sign", sign);
			log.info("发送参数：" + map);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(map,config.getEntrustedLoanUrl(), null);
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(userVo.getP1_bizType(), userVo.getP3_orderId(), map, resultMap,"商户用户注册",custInfoId);
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				resVo.setRt2_retCode(ERROR_CODE);
				resVo.setRt3_retMsg("请求失败");
				return resVo;
			}
			String resultMsg = (String) resultMap.get("response");
			resVo = JSONObject.parseObject(resultMsg, MerchantUserResVo.class);
			String assemblyRespOriSign = MyBeanUtils.getSigned(resVo, null);
			log.info("组装返回结果签名串：" + assemblyRespOriSign);
			String responseSign = resVo.getSign();
			log.info("响应签名：" + responseSign);
			String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim()
					+ SPLIT + config.getSignkey());
			if (!checkSign.equals(responseSign)) {
				resVo.setRt2_retCode(ERROR_CODE);
				resVo.setRt3_retMsg("验签失败");
				return resVo;
			}
		} catch (Exception e) {
    	 resVo.setRt2_retCode(ERROR_CODE);
   	     resVo.setRt3_retMsg("交易异常");
       	  log.error("商户用户注册-交易异常：" + e.getMessage(), e);
        }
		log.info("--------商户用户注册-返回报文---------"+JSON.toJSONString(resVo, SerializerFeature.WriteMapNullValue));
        return resVo;
    }

    //---------------商户用户查询
    @Override
    public MerchantUserQueryResVo userQuery(MerchantUserQueryVo userVo,String custInfoId) {
    	log.info("--------商户用户查询----------"+JSON.toJSONString(userVo, SerializerFeature.WriteMapNullValue));
    	String TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd_ HH:mm:ss.SSS").format(new Date());
    	  HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
    	  MerchantUserQueryResVo resVo = new MerchantUserQueryResVo();
    	  userVo.setP1_bizType("MerchantUserQuery");
    	  userVo.setP2_customerNumber(config.getCustomerNumber());
    	  userVo.setP5_timestamp(TIMESTAMP);
    	  userVo.setP3_orderId(Uuid.getUuid26());
    	  
    	  resVo.setRt1_bizType(userVo.getP1_bizType());
    	  resVo.setRt4_customerNumber(userVo.getP2_customerNumber());
    	  resVo.setRt5_orderId(userVo.getP3_orderId());
      try {
    	
            Map<String, String> map = MyBeanUtils.convertBeanReq(userVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);
            String sign = RSA.sign(oriMessage.trim(), RSA.getPrivateKey(config.getSignkeyPrivate()));;
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, config.getEntrustedLoanUrl(), null);
            log.info("响应结果：" + resultMap);
            helibaoFlowLogService.saveFlow(userVo.getP1_bizType(), userVo.getP3_orderId(), map, resultMap,"商户用户查询",custInfoId);
            if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				resVo.setRt2_retCode(ERROR_CODE);
				resVo.setRt3_retMsg("请求失败");
				return resVo;
			}
            String resultMsg = (String) resultMap.get("response");
            resVo = JSONObject.parseObject(resultMsg, MerchantUserQueryResVo.class);
           String assemblyRespOriSign = MyBeanUtils.getSigned(resVo, null);
           log.info("组装返回结果签名串：" + assemblyRespOriSign);
           String responseSign = resVo.getSign();
           log.info("响应签名：" + responseSign);
           String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim()+SPLIT+config.getSignkey());
           if (!checkSign.equals(responseSign)) {
        		resVo.setRt2_retCode(ERROR_CODE);
				resVo.setRt3_retMsg("验签失败");
				return resVo;
           }
            
        } catch (Exception e) {
    	 resVo.setRt2_retCode(ERROR_CODE);
   	     resVo.setRt3_retMsg("交易异常");
       	  log.error("商户用户查询-交易异常：" + e.getMessage(), e);
        }
        log.info("--------商户用户查询-返回报文---------"+JSON.toJSONString(resVo, SerializerFeature.WriteMapNullValue));
        return resVo;
    }

    //-------商户用户资质上传
    @Override
    public MerchantUserUploadResVo userUpload(MerchantUserUploadVo userVo, File tempFile,String custInfoId) {
    	log.info("--------商户用户资质上传----------"+JSON.toJSONString(userVo, SerializerFeature.WriteMapNullValue));
    	String UUID = Uuid.getUuid26();
    	String TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd_ HH:mm:ss.SSS").format(new Date());
    	HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
    	MerchantUserUploadResVo resVo = new MerchantUserUploadResVo();
    	userVo.setP1_bizType("UploadCredential");
    	userVo.setP2_customerNumber(config.getCustomerNumber());
    	userVo.setP3_orderId(UUID);
    	userVo.setP5_timestamp(TIMESTAMP);
    	resVo.setRt1_bizType(userVo.getP1_bizType());
    	resVo.setRt4_customerNumber(userVo.getP2_customerNumber());
    	resVo.setRt5_orderId(userVo.getP3_orderId());
        try {
        	//String tempDir = System.getProperty("java.io.tmpdir");
        	// File tempFile = new File(tempDir, file.getOriginalFilename());
           
            // 文件签名
            try (InputStream is = new FileInputStream(tempFile)){
                userVo.setP7_fileSign(DigestUtils.md5DigestAsHex(is));
            }
            Map<String, String> map = MyBeanUtils.convertBeanReq(userVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);
            String sign = RSA.sign(oriMessage.trim(), RSA.getPrivateKey(config.getSignkeyPrivate()));
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
           
            //map.put("file", "");
            Map resultMap = HttpClientService.getHttpResp(map, config.getEntrustedUploanUrl(), tempFile);
            log.info("响应结果：" + resultMap);
            helibaoFlowLogService.saveFlow(userVo.getP1_bizType(),userVo.getP3_orderId(), map, resultMap,"商户用户资质上传",custInfoId);
            if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				resVo.setRt2_retCode(ERROR_CODE);
				resVo.setRt3_retMsg("请求失败");
				return resVo;
			}
            String resultMsg = (String) resultMap.get("response");
            resVo = JSONObject.parseObject(resultMsg, MerchantUserUploadResVo.class);
           String assemblyRespOriSign = MyBeanUtils.getSigned(resVo, null);
           log.info("组装返回结果签名串：" + assemblyRespOriSign);
           String responseSign = resVo.getSign();
           log.info("响应签名：" + responseSign);
           String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim()+SPLIT+config.getSignkey());
           if (!checkSign.equals(responseSign)) {
        	   resVo.setRt2_retCode(ERROR_CODE);
			   resVo.setRt3_retMsg("验签失败");
			   return resVo;
           } 
           
        } catch (Exception e) {
    	 resVo.setRt2_retCode(ERROR_CODE);
   	     resVo.setRt3_retMsg("交易异常");
       	  log.error("商户用户资质上传-交易异常：" + e.getMessage(), e);
        }
        log.info("--------商户用户资质上传-返回报文---------"+JSON.toJSONString(resVo, SerializerFeature.WriteMapNullValue));
        return resVo;
    }

    //-------用户资质查询
    @Override
    public MerchantUserUploadResVo userUploadQuery(MerchantUserUploadVo userVo,String custInfoId) {
    	log.info("--------用户资质查询----------"+JSON.toJSONString(userVo, SerializerFeature.WriteMapNullValue));
    	String TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd_ HH:mm:ss.SSS").format(new Date());
    	HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
    	MerchantUserUploadResVo resVo = new MerchantUserUploadResVo();
    	userVo.setP1_bizType("UploadCredentialQuery");
    	userVo.setP2_customerNumber(config.getCustomerNumber());
    	userVo.setP5_timestamp(TIMESTAMP);
    	userVo.setP3_orderId(Uuid.getUuid26());
    	
    	resVo.setRt1_bizType(userVo.getP1_bizType());
    	resVo.setRt4_customerNumber(userVo.getP2_customerNumber());
    	resVo.setRt5_orderId(userVo.getP3_orderId());
        try {
            String[] excludes = {"P7_fileSign"};
            Map<String, String> map = MyBeanUtils.convertBeanReq(userVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, excludes);
            log.info("签名原文串：" + oriMessage);
            String sign = RSA.sign(oriMessage.trim(), RSA.getPrivateKey(config.getSignkeyPrivate()));;
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, config.getEntrustedLoanUrl(), null);
            log.info("响应结果：" + resultMap);
            helibaoFlowLogService.saveFlow(userVo.getP1_bizType(), userVo.getP3_orderId(), map, resultMap,"用户资质查询",custInfoId);
            if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				resVo.setRt2_retCode(ERROR_CODE);
				resVo.setRt3_retMsg("请求失败");
				return resVo;
			}
            String resultMsg = (String) resultMap.get("response");
            resVo = JSONObject.parseObject(resultMsg, MerchantUserUploadResVo.class);
           String assemblyRespOriSign = MyBeanUtils.getSigned(resVo, null);
           log.info("组装返回结果签名串：" + assemblyRespOriSign);
           String responseSign = resVo.getSign();
           log.info("响应签名：" + responseSign);
           String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim()+SPLIT+config.getSignkey());
           if (!checkSign.equals(responseSign)) {
        	   resVo.setRt2_retCode(ERROR_CODE);
			   resVo.setRt3_retMsg("验签失败");
			   return resVo;
           }
          
        } catch (Exception e) {
        	resVo.setRt2_retCode(ERROR_CODE);
      	    resVo.setRt3_retMsg("交易异常");
      	   log.error("用户资质查询-交易异常：" + e.getMessage(), e);
        }
        log.info("--------用户资质查询-返回报文---------"+JSON.toJSONString(resVo, SerializerFeature.WriteMapNullValue));
        return resVo;
    }

    //-------委托代付下单
    @Override
    public OrderResVo createOrder(OrderVo orderVo,String custInfoId) {
    	log.info("--------委托代付下单----------"+JSON.toJSONString(orderVo, SerializerFeature.WriteMapNullValue));
    	String UUID = Uuid.getUuid26();
    	String TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd_ HH:mm:ss.SSS").format(new Date());
    	HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
    	OrderResVo resVo = new OrderResVo();
    	orderVo.setP1_bizType("EntrustedLoanTransfer");
    	orderVo.setP2_customerNumber(config.getCustomerNumber());
    	orderVo.setP3_orderId(UUID);
    	orderVo.setP5_timestamp(TIMESTAMP);
    	orderVo.setP6_currency(CURRENCY);
    	orderVo.setP19_callbackUrl(config.getEntrustedCallbackUrl());
    	orderVo.setP13_onlineCardType("DEBIT");
    	orderVo.setP8_business("B2C");
    	
		
    	
    	resVo.setRt1_bizType(orderVo.getP1_bizType());
    	resVo.setRt4_customerNumber(orderVo.getP2_customerNumber());
    	resVo.setRt5_orderId(orderVo.getP3_orderId());
    	
    	
        if (StringUtils.isNotBlank(orderVo.getP10_bankAccountNo())) {
            orderVo.setP10_bankAccountNo(Des3Encryption.encode(config.getDeskeyKey(), orderVo.getP10_bankAccountNo()));
        }
        if (StringUtils.isNotBlank(orderVo.getP11_legalPersonID())) {
            orderVo.setP11_legalPersonID(Des3Encryption.encode(config.getDeskeyKey(), orderVo.getP11_legalPersonID()));
        }
        if (StringUtils.isNotBlank(orderVo.getP12_mobile())) {
            orderVo.setP12_mobile(Des3Encryption.encode(config.getDeskeyKey(), orderVo.getP12_mobile()));
        }
        if (StringUtils.isNotBlank(orderVo.getP14_year())) {
            orderVo.setP14_year(Des3Encryption.encode(config.getDeskeyKey(), orderVo.getP14_year()));
        }
        if (StringUtils.isNotBlank(orderVo.getP15_month())) {
            orderVo.setP15_month(Des3Encryption.encode(config.getDeskeyKey(), orderVo.getP15_month()));
        }
        if (StringUtils.isNotBlank(orderVo.getP16_cvv2())) {
            orderVo.setP16_cvv2(Des3Encryption.encode(config.getDeskeyKey(), orderVo.getP16_cvv2()));
        }
        try {
            Map<String, String> map = MyBeanUtils.convertBeanReq(orderVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);
            String sign = RSA.sign(oriMessage.trim(), RSA.getPrivateKey(config.getSignkeyPrivate()));;
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, config.getEntrustedLoanUrl(), null);
            log.info("响应结果：" + resultMap);
            helibaoFlowLogService.saveFlow(orderVo.getP1_bizType(), orderVo.getP3_orderId(), map, resultMap,"委托代付下单",custInfoId);
            if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				resVo.setRt2_retCode(ERROR_CODE);
				resVo.setRt3_retMsg("请求失败");
				return resVo;
			}
            String resultMsg = (String) resultMap.get("response");
            resVo = JSONObject.parseObject(resultMsg, OrderResVo.class);
           String assemblyRespOriSign = MyBeanUtils.getSigned(resVo, null);
           log.info("组装返回结果签名串：" + assemblyRespOriSign);
           String responseSign = resVo.getSign();
           log.info("响应签名：" + responseSign);
           String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim()+SPLIT+config.getSignkey());
           if (!checkSign.equals(responseSign)) {
        	   resVo.setRt2_retCode(ERROR_CODE);
			   resVo.setRt3_retMsg("验签失败");
			   return resVo;
           } 
           
        } catch (Exception e) {
        	resVo.setRt2_retCode(ERROR_CODE);
      	    resVo.setRt3_retMsg("交易异常");
      	   log.error("委托代付下单-交易异常：" + e.getMessage(), e);
        }
        log.info("--------委托代付下单-返回报文---------"+JSON.toJSONString(resVo, SerializerFeature.WriteMapNullValue));
        return resVo;
    }

    //------------ 委托代付订单查询
    @Override
    public OrderQueryResVo orderQuery(OrderQueryVo orderVo,String custInfoId) {
    	log.info("--------委托代付订单查询----------"+JSON.toJSONString(orderVo, SerializerFeature.WriteMapNullValue));
    	String TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd_ HH:mm:ss.SSS").format(new Date());
    	HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
    	OrderQueryResVo resVo = new OrderQueryResVo();
    	orderVo.setP1_bizType("EntrustedLoanTransferQuery");
    	orderVo.setP2_customerNumber(config.getCustomerNumber());
    	orderVo.setP5_timestamp(TIMESTAMP);
    	
    	resVo.setRt1_bizType(orderVo.getP1_bizType());
    	resVo.setRt4_customerNumber(orderVo.getP2_customerNumber());
    	resVo.setRt5_orderId(orderVo.getP3_orderId());
        try {
            Map<String, String> map = MyBeanUtils.convertBeanReq(orderVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);
            String sign = RSA.sign(oriMessage.trim(), RSA.getPrivateKey(config.getSignkeyPrivate()));;
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, config.getEntrustedLoanUrl(), null);
            log.info("响应结果：" + resultMap);
            helibaoFlowLogService.saveFlow(orderVo.getP1_bizType(), orderVo.getP3_orderId(), map, resultMap,"委托代付订单查询",custInfoId);
            if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				resVo.setRt2_retCode(ERROR_CODE);
				resVo.setRt3_retMsg("请求失败");
				return resVo;
			}
            String resultMsg = (String) resultMap.get("response");
            resVo = JSONObject.parseObject(resultMsg, OrderQueryResVo.class);
           String assemblyRespOriSign = MyBeanUtils.getSigned(resVo, null);
           log.info("组装返回结果签名串：" + assemblyRespOriSign);
           String responseSign = resVo.getSign();
           log.info("响应签名：" + responseSign);
           String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim()+SPLIT+config.getSignkey());
           if (!checkSign.equals(responseSign)) {
        	   resVo.setRt2_retCode(ERROR_CODE);
			   resVo.setRt3_retMsg("验签失败");
			   return resVo;
           } 
          
        } catch (Exception e) {
        	resVo.setRt2_retCode(ERROR_CODE);
      	    resVo.setRt3_retMsg("交易异常");
      	    log.error("委托代付订单查询-交易异常：" + e.getMessage(), e);
      	 }
        log.info("--------委托代付订单查询-返回报文---------"+JSON.toJSONString(resVo, SerializerFeature.WriteMapNullValue));
        return resVo;
    }

    //------获取合同地址
    @Override
    public OrderContractSignatureQueryResVo contractSignature(OrderQueryVo orderVo,String custInfoId) {
    	log.info("--------获取合同地址----------"+JSON.toJSONString(orderVo, SerializerFeature.WriteMapNullValue));
    	String TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd_ HH:mm:ss.SSS").format(new Date());
    	HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
    	 OrderContractSignatureQueryResVo resVo = new  OrderContractSignatureQueryResVo();
    	 orderVo.setP1_bizType("MerchantContractSignature");
    	 orderVo.setP2_customerNumber(config.getCustomerNumber());
    	 orderVo.setP5_timestamp(TIMESTAMP);
    	 
    	 resVo.setRt1_bizType(orderVo.getP1_bizType());
    	 resVo.setRt4_customerNumber(orderVo.getP2_customerNumber());
    	 resVo.setRt5_orderId(orderVo.getP3_orderId());
        try {
            Map<String, String> map = MyBeanUtils.convertBeanReq(orderVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);
            String sign = RSA.sign(oriMessage.trim(), RSA.getPrivateKey(config.getSignkeyPrivate()));;
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, config.getEntrustedLoanUrl(), null);
            log.info("响应结果：" + resultMap);
            helibaoFlowLogService.saveFlow(orderVo.getP1_bizType(), orderVo.getP3_orderId(), map, resultMap,"获取合同地址",custInfoId);
            if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				resVo.setRt2_retCode(ERROR_CODE);
				resVo.setRt3_retMsg("请求失败");
				return resVo;
			}
            String resultMsg = (String) resultMap.get("response");
             resVo = JSONObject.parseObject(resultMsg, OrderContractSignatureQueryResVo.class);
            String assemblyRespOriSign = MyBeanUtils.getSigned(resVo, null);
            log.info("组装返回结果签名串：" + assemblyRespOriSign);
            String responseSign = resVo.getSign();
            log.info("响应签名：" + responseSign);
            String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim()+SPLIT+config.getSignkey());
            if (!checkSign.equals(responseSign)) {
               resVo.setRt2_retCode(ERROR_CODE);
  			   resVo.setRt3_retMsg("验签失败");
  			   return resVo;
            } 
            
        } catch (Exception e) {
        	resVo.setRt2_retCode(ERROR_CODE);
      	    resVo.setRt3_retMsg("交易异常");
      	   log.error("获取合同地址-交易异常：" + e.getMessage(), e);
      	}
        log.info("--------获取合同地址-返回报文----------"+JSON.toJSONString(resVo, SerializerFeature.WriteMapNullValue));
        return resVo;
    }

    //--------委托代付结果通知(主动通知，无请求参数)
	@Override
	public String notfyOrderQueryResVo(OrderQueryResVo orderVo) {
		if(null == orderVo){
			return "nullError";
		}
		try {
			HelibaoFilesConfig config = helibaoFilesConfigService
					.queryHelibaoFilesConfigInfo();
			String assemblyRespOriSign = MyBeanUtils.getSigned(orderVo, null);
			log.info("组装返回结果签名串：" + assemblyRespOriSign);
			String responseSign = orderVo.getSign();
			log.info("响应签名：" + responseSign);
			String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim()
					+ SPLIT + config.getSignkey());
			if (!checkSign.equals(responseSign)) {
				return "signError";
			}
			helibaoFlowLogService.saveFlow(orderVo.getRt1_bizType(), orderVo.getRt5_orderId(), MyBeanUtils.convertBeanReq(orderVo,new LinkedHashMap()), null,"委托代付结果通知","");
		} catch (Exception e) {
			log.error("--------委托代付结果-异常:"+JSON.toJSONString(orderVo, SerializerFeature.WriteMapNullValue),e.getMessage());
			return "excError";
		}
		return "success";
	}
    
   
}
