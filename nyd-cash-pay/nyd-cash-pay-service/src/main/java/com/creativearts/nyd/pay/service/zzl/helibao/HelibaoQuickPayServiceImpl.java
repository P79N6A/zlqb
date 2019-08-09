package com.creativearts.nyd.pay.service.zzl.helibao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.enums.AppTypeEnum;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.enums.DealSceneTypeEnum;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BankCardUnbindVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BankCardbindVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BindCardSendValidateCodeVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.BindPaySendValidateCodeVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.ConfirmBindCardVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.ConfirmBindPayVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.FirstPayConfirmPayVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.FirstPaySendValidateCodeVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QueryOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QuickPayBindCardPreOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QuickPayBindPayPreOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QuickPayConfirmPayVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.req.QuickPayFirstPayPreOrderVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BankCardUnbindResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BankCardbindResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BindCardPreOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BindCardSendValidateCodeResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.BindPaySendValidateCodeResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.ConfirmBindCardResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.ConfirmBindPayResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QueryOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPayBindPayPreOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPayConfirmPayResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPayCreateOrderResponseVo;
import com.creativearts.nyd.collectionPay.model.zzl.helibao.resp.QuickPaySendValidateCodeResponseVo;
import com.creativearts.nyd.pay.config.utils.zzl.helibao.HttpClientService;
import com.creativearts.nyd.pay.config.utils.zzl.helibao.Uuid;
import com.creativearts.nyd.pay.service.zzl.helibao.util.ArithUtil;
import com.creativearts.nyd.pay.service.zzl.helibao.util.MessageHandle;
import com.creativearts.nyd.pay.service.zzl.helibao.util.MyBeanUtils;
import com.creativearts.nyd.pay.service.zzl.helibao.util.RowLock;
import com.nyd.order.entity.zzl.HelibaoFilesConfig;
import com.nyd.pay.api.zzl.HelibaoFilesConfigService;
import com.nyd.pay.api.zzl.HelibaoFlowLogService;
import com.nyd.pay.api.zzl.HelibaoQuickPayService;



@Service("helibaoQuickPayService")
public class HelibaoQuickPayServiceImpl implements HelibaoQuickPayService{
	
	Logger log = LoggerFactory.getLogger(HelibaoQuickPayServiceImpl.class);
	

	@Autowired
	private HelibaoFlowLogService helibaoFlowLogService;
	
	@Autowired
	private HelibaoFilesConfigService helibaoFilesConfigService;
	
	private final String P14_CURRENCY = "CNY";//钱单位
	
	private final String IDCARD = "IDCARD";//身份证类型
	
	private final String ERROR_CODE = "0002";
	
	private final String SIGNATURETYPE = "MD5WITHRSA";//签名方式
	
	@Value("${money.test}")
	private String money_test;//金钱挡板 true 测试环境
	
	private String moneyDiv = "1000";
	
	

	//--------------首次支付预下单
	@Override
	public QuickPayCreateOrderResponseVo quickPayFirstPayPreOrder(QuickPayFirstPayPreOrderVo requestVo,String custInfoId) {
		log.info("--------进入创建订单接口----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		QuickPayCreateOrderResponseVo responseVo = new QuickPayCreateOrderResponseVo();
		String UUID = Uuid.getUuid26();
		String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("QuickPayFirstPayPreOrder");
		requestVo.setP2_customerNumber(config.getCustomerNumber());
		requestVo.setP4_orderId(UUID);
		requestVo.setP5_timestamp(TIMESTAMP);
		requestVo.setP7_idCardType(IDCARD);
		requestVo.setP14_currency(P14_CURRENCY);
		requestVo.setP20_orderIp(config.getPayIp());
		requestVo.setP23_serverCallbackUrl(config.getPayCallbackUrl());
		requestVo.setSignatureType(SIGNATURETYPE);
		if(StringUtils.isBlank(requestVo.getP16_goodsName())){
			requestVo.setP16_goodsName(config.getGoodsName());
		}
		if(StringUtils.isBlank(requestVo.getP18_terminalType())){
			requestVo.setP18_terminalType("OTHER");
		}
		if(StringUtils.isBlank(requestVo.getGoodsQuantity())){
			requestVo.setGoodsQuantity("1");
		}
		if(StringUtils.isBlank(requestVo.getAppType())){
			requestVo.setAppType(AppTypeEnum.OTHER.getCode());
		}
		if(StringUtils.isBlank(requestVo.getAppName())){
			requestVo.setAppName(config.getGoodsName());
		}
		if(StringUtils.isBlank(requestVo.getDealSceneType())){
			requestVo.setDealSceneType(DealSceneTypeEnum.OTHER.getCode());
		}
		
		if("true".equals(money_test)){
			if(StringUtils.isNotBlank(requestVo.getP15_orderAmount())){
				String money = requestVo.getP15_orderAmount();
				BigDecimal m = new BigDecimal(money);
				BigDecimal w = new BigDecimal(moneyDiv);
				requestVo.setP15_orderAmount(ArithUtil.divForBigDecimal(m, w, 2));
			}
		}
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP2_customerNumber());
		responseVo.setRt5_orderId(requestVo.getP4_orderId());
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP4_orderId(), reqestMap, resultMap,"首次支付预下单",custInfoId);
			log.info("响应结果：" + resultMap);
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				return responseVo;
			}

			 responseVo = JSONObject.parseObject(resultMsg, QuickPayCreateOrderResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				return responseVo;
			}

		} catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("交易异常");
			
			log.error("首次支付预下单-交易异常：" + e.getMessage(), e);
		}
		log.info("--------首次支付预下单-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}


	//----------------首次支付短信
	@Override
	public QuickPaySendValidateCodeResponseVo firstPaySendValidateCode(FirstPaySendValidateCodeVo requestVo,String custInfoId) {
		log.info("--------进入发送短信接口----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		QuickPaySendValidateCodeResponseVo responseVo = new QuickPaySendValidateCodeResponseVo();
		String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("FirstPaySendValidateCode");
		requestVo.setP2_customerNumber(config.getCustomerNumber());
		requestVo.setP4_timestamp(TIMESTAMP);
		requestVo.setSignatureType(SIGNATURETYPE);
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP2_customerNumber());
		responseVo.setRt5_orderId(requestVo.getP3_orderId());
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP3_orderId(), reqestMap, resultMap,"首次支付短信",custInfoId);
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				 return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				 return responseVo;
			}
             responseVo = JSONObject.parseObject(resultMsg, QuickPaySendValidateCodeResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				 return responseVo;
			}

		} catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("交易异常");
			log.error("首次支付短信-交易异常：" + e.getMessage(), e);
		}
		log.info("--------首次支付短信-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}


	//---------------首次支付确认支付
	@Override
	public QuickPayConfirmPayResponseVo firstPayconfirmPay(FirstPayConfirmPayVo requestVo,String custInfoId) {
		log.info("--------进入确认支付接口----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		QuickPayConfirmPayResponseVo responseVo = new QuickPayConfirmPayResponseVo();
		String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("FirstPayConfirmPay");
		requestVo.setP2_customerNumber(config.getCustomerNumber());
		requestVo.setP4_timestamp(TIMESTAMP);
		requestVo.setSignatureType(SIGNATURETYPE);
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP2_customerNumber());
		responseVo.setRt5_orderId(requestVo.getP3_orderId());
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP3_orderId(), reqestMap, resultMap,"首次支付确认支付",custInfoId);
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				 return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				return responseVo;
			}

			 responseVo = JSONObject.parseObject(resultMsg, QuickPayConfirmPayResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				
			}
            responseVo.setRt2_retCode(responseVo.getRt2_retCode());
			responseVo.setRt3_retMsg(responseVo.getRt3_retMsg());
		} catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("交易异常");
			log.error("首次支付确认支付-交易异常：" + e.getMessage(), e);
		}
		log.info("--------首次支付确认支付-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}


	//--------------绑卡预下单
	@Override
	public BindCardPreOrderResponseVo quickPayBindCardPreOrder(QuickPayBindCardPreOrderVo requestVo,String custInfoId) {
		log.info("--------进入创建订单接口----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		BindCardPreOrderResponseVo responseVo = new BindCardPreOrderResponseVo();
		String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String UUID = Uuid.getUuid26();
		
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("QuickPayBindCardPreOrder");
		requestVo.setP2_customerNumber(config.getCustomerNumber());
		requestVo.setP4_orderId(UUID);
		requestVo.setP3_userId(Uuid.getUuidYH26());
		requestVo.setP5_timestamp(TIMESTAMP);
		requestVo.setP7_idCardType(IDCARD);
		requestVo.setSignatureType(SIGNATURETYPE);
		requestVo.setSendValidateCode("TRUE");
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP2_customerNumber());
		responseVo.setRt6_orderId(requestVo.getP4_orderId());
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP4_orderId(), reqestMap, resultMap,"绑卡预下单",custInfoId);
            if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				return responseVo;
			}

			 responseVo = JSONObject.parseObject(resultMsg, BindCardPreOrderResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				return responseVo;
			}
        } catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("交易异常");
			log.error("绑卡预下单-交易异常：" + e.getMessage(), e);
		}
		log.info("--------绑卡预下单-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}


	//---------------鉴权绑卡短信
	@Override
	public BindCardSendValidateCodeResponseVo bindCardSendValidateCode(BindCardSendValidateCodeVo requestVo,String custInfoId) {
		log.info("--------进入发送短信接口----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		BindCardSendValidateCodeResponseVo responseVo = new BindCardSendValidateCodeResponseVo();
		String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("BindCardSendValidateCode");
		requestVo.setP2_customerNumber(config.getCustomerNumber());
		requestVo.setP4_timestamp(TIMESTAMP);
		requestVo.setSignatureType(SIGNATURETYPE);
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP2_customerNumber());
		responseVo.setRt5_orderId(requestVo.getP3_orderId());
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP3_orderId(), reqestMap, resultMap,"鉴权绑卡短信",custInfoId);
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				return responseVo;
			}

			 responseVo = JSONObject.parseObject(resultMsg, BindCardSendValidateCodeResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				return responseVo;
			}
         } catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("请求异常");
			log.error("鉴权绑卡短信-交易异常：" + e.getMessage(), e);
         }
		log.info("--------鉴权绑卡短信-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}


	//--------------鉴权绑卡
	@Override
	public ConfirmBindCardResponseVo bindCard(ConfirmBindCardVo requestVo,String custInfoId) {
		log.info("--------进入绑卡接口----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		ConfirmBindCardResponseVo responseVo = new ConfirmBindCardResponseVo();
		String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("ConfirmBindCard");
		requestVo.setP2_customerNumber(config.getCustomerNumber());
		requestVo.setP4_timestamp(TIMESTAMP);
		requestVo.setSignatureType(SIGNATURETYPE);
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP2_customerNumber());
		responseVo.setRt6_orderId(requestVo.getP3_orderId());
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP3_orderId(), reqestMap, resultMap,"鉴权绑卡",custInfoId);
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				return responseVo;
			}

			 responseVo = JSONObject.parseObject(resultMsg, ConfirmBindCardResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				return responseVo;
			}

		} catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("交易异常");
			log.error("鉴权绑卡-交易异常：" + e.getMessage(), e);
		}
		log.info("--------鉴权绑卡-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}



	//--------------绑卡支付预下单
	@Override
	public QuickPayBindPayPreOrderResponseVo quickPayBindPayPreOrder(QuickPayBindPayPreOrderVo requestVo,String custInfoId) {
		log.info("--------进入创建订单接口----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		QuickPayBindPayPreOrderResponseVo responseVo = new QuickPayBindPayPreOrderResponseVo();
		String UUID = Uuid.getUuid26();
		String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("QuickPayBindPayPreOrder");
		requestVo.setP2_customerNumber(config.getCustomerNumber());
		requestVo.setP5_orderId(UUID);
		requestVo.setP6_timestamp(TIMESTAMP);
		requestVo.setP7_currency(P14_CURRENCY);
		requestVo.setP13_orderIp(config.getPayIp());
		requestVo.setP16_serverCallbackUrl(config.getPayCallbackUrl());
		requestVo.setSignatureType(SIGNATURETYPE);
		if("true".equals(money_test)){
			if(StringUtils.isNotBlank(requestVo.getP8_orderAmount())){
				String money = requestVo.getP8_orderAmount();
				BigDecimal m = new BigDecimal(money);
				BigDecimal w = new BigDecimal(moneyDiv);
				requestVo.setP8_orderAmount(ArithUtil.divForBigDecimal(m, w, 2));
			}
		}
		
		
		if(StringUtils.isBlank(requestVo.getP9_goodsName())){
			requestVo.setP9_goodsName(config.getGoodsName());
		}
		if(StringUtils.isBlank(requestVo.getP11_terminalType())){
			requestVo.setP11_terminalType("OTHER");
		}
		if(StringUtils.isBlank(requestVo.getGoodsQuantity())){
			requestVo.setGoodsQuantity("1");
		}
		if(StringUtils.isBlank(requestVo.getAppType())){
			requestVo.setAppType(AppTypeEnum.OTHER.getCode());
		}
		if(StringUtils.isBlank(requestVo.getAppName())){
			requestVo.setAppName(config.getGoodsName());
		}
		if(StringUtils.isBlank(requestVo.getDealSceneType())){
			requestVo.setDealSceneType(DealSceneTypeEnum.OTHER.getCode());
		}
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP2_customerNumber());
		responseVo.setRt5_orderId(requestVo.getP5_orderId());
		try {
			
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP5_orderId(), reqestMap, resultMap,"绑卡支付预下单",custInfoId);
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				return responseVo;
			}

			 responseVo = JSONObject.parseObject(resultMsg, QuickPayBindPayPreOrderResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				return responseVo;
			}

		} catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("交易异常");
			
			log.error("绑卡支付预下单-交易异常：" + e.getMessage(), e);
		}
		log.info("--------绑卡支付预下单-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}



	//------------绑卡支付短信
	@Override
	public BindPaySendValidateCodeResponseVo sendValidateCode(BindPaySendValidateCodeVo requestVo,String custInfoId) {
		log.info("--------进入发送短信接口----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		BindPaySendValidateCodeResponseVo responseVo = new BindPaySendValidateCodeResponseVo();
		String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("BindPaySendValidateCode");
		requestVo.setP2_customerNumber(config.getCustomerNumber());
		requestVo.setP4_timestamp(TIMESTAMP);
		requestVo.setSignatureType(SIGNATURETYPE);
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP2_customerNumber());
		responseVo.setRt5_orderId(requestVo.getP3_orderId());
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP3_orderId(), reqestMap, resultMap,"绑卡支付短信",custInfoId);
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				return responseVo;
			}

			 responseVo = JSONObject.parseObject(resultMsg, BindPaySendValidateCodeResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
			    responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				return responseVo;
			}

		} catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("交易异常");
			log.error("绑卡支付短信-交易异常：" + e.getMessage(), e);
		}
		log.info("--------绑卡支付短信-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}


	//----------------绑卡支付
	@Override
	public ConfirmBindPayResponseVo confirmBindPay(ConfirmBindPayVo requestVo,String custInfoId) {
		log.info("--------进入确认支付接口----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		ConfirmBindPayResponseVo responseVo = new ConfirmBindPayResponseVo();
		String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("ConfirmBindPay");
		requestVo.setP2_customerNumber(config.getCustomerNumber());
		requestVo.setP4_timestamp(TIMESTAMP);
		requestVo.setSignatureType(SIGNATURETYPE);
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP2_customerNumber());
		responseVo.setRt5_orderId(requestVo.getP3_orderId());
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP3_orderId(), reqestMap, resultMap,"绑卡支付",custInfoId);
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				return responseVo;
			}

			 responseVo = JSONObject.parseObject(resultMsg, ConfirmBindPayResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				return responseVo;
			}

		} catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("交易异常");
			log.error("绑卡支付-交易异常：" + e.getMessage(), e);
		}
		log.info("--------绑卡支付-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}

	//进入订单查询接口
	@Override
	public QueryOrderResponseVo queryOrder(QueryOrderVo requestVo) {
		log.info("--------进入订单查询接口----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		QueryOrderResponseVo responseVo = new QueryOrderResponseVo();
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("QuickPayQuery");
		requestVo.setP3_customerNumber(config.getCustomerNumber());
		requestVo.setSignatureType(SIGNATURETYPE);
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP3_customerNumber());
		responseVo.setRt5_orderId(requestVo.getP2_orderId());
		try {
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP2_orderId(), reqestMap, resultMap,"进入订单查询接口","");
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				return responseVo;
			}

			 responseVo = JSONObject.parseObject(resultMsg, QueryOrderResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				return responseVo;
			}

		} catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("交易异常");
			log.error("进入订单查询接口-交易异常：" + e.getMessage(), e);
		}
		log.info("--------进入订单查询接口-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}

	

	//----------------银行卡解绑
	@Override
	public BankCardUnbindResponseVo bankCardUnbind(BankCardUnbindVo requestVo,String custInfoId) {
		log.info("--------银行卡解绑----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		BankCardUnbindResponseVo responseVo = new BankCardUnbindResponseVo();
		String UUID = Uuid.getUuid26();
		String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("BankCardUnbind");
		requestVo.setP2_customerNumber(config.getCustomerNumber());
		requestVo.setP5_orderId(UUID);
		requestVo.setP6_timestamp(TIMESTAMP);
		requestVo.setSignatureType(SIGNATURETYPE);
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP2_customerNumber());
		try {
			
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP5_orderId(), reqestMap, resultMap,"银行卡解绑",custInfoId);
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				return responseVo;
			}

			 responseVo = JSONObject.parseObject(resultMsg, BankCardUnbindResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				return responseVo;
			}

		} catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("交易异常");
			log.error("银行卡解绑-交易异常：" + e.getMessage(), e);
		}
		log.info("--------银行卡解绑-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}


	//----------------用户绑定银行卡信息查询
	@Override
	public BankCardbindResponseVo bankCardbindList(
			BankCardbindVo requestVo,String custInfoId) {
		log.info("--------用户绑定银行卡信息查询----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		BankCardbindResponseVo responseVo = new BankCardbindResponseVo();
		HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
		requestVo.setP1_bizType("BankCardbindList");
		requestVo.setP2_customerNumber(config.getCustomerNumber());
		requestVo.setSignatureType(SIGNATURETYPE);
		String TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		requestVo.setP5_timestamp(TIMESTAMP);
		
		responseVo.setRt1_bizType(requestVo.getP1_bizType());
		responseVo.setRt4_customerNumber(requestVo.getP2_customerNumber());
		try {
			
			Map reqestMap = MessageHandle.getReqestMap(requestVo,config.getCertPath(),config.getPfxPath(),config.getPfxPwd());
			log.info("请求参数：" + reqestMap);
			Map<String, Object> resultMap = HttpClientService.getHttpResp(reqestMap, config.getPayUrl());
			log.info("响应结果：" + resultMap);
			helibaoFlowLogService.saveFlow(requestVo.getP1_bizType(), requestVo.getP3_userId(), reqestMap, resultMap,"用户绑定银行卡信息查询",custInfoId);
			if ((Integer) resultMap.get("statusCode") != HttpStatus.SC_OK) {
				responseVo.setRt2_retCode(((Integer) resultMap.get("statusCode")).toString());
				responseVo.setRt3_retMsg("请求失败");
				return responseVo;
			}

			String resultMsg = (String) resultMap.get("response");
			if (!isJSON(resultMsg)) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg(resultMsg);
				return responseVo;
			}

			 responseVo = JSONObject.parseObject(resultMsg, BankCardbindResponseVo.class);
			if (!MessageHandle.checkSign(responseVo,config.getCertPath())) {
				responseVo.setRt2_retCode(ERROR_CODE);
				responseVo.setRt3_retMsg("验签失败");
				return responseVo;
			}
        /* String jsonString = responseVo.getRt5_bindCardList();

          JSONArray json = JSONArray.fromObject(jsonString);
          List<BankCardbindBody> list = JSONArray.toList(json, BankCardbindBody.class);*/
		} catch (Exception e) {
			responseVo.setRt2_retCode(ERROR_CODE);
			responseVo.setRt3_retMsg("交易异常");
			log.error("用户绑定银行卡信息查询-交易异常：" + e.getMessage(), e);
		}
		log.info("--------用户绑定银行卡信息查询-返回报文---------"+JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue));
		return responseVo;
	}

  /**
	* 合利宝支付平台对商户的支付请求数据处理完成后，
	*	会将处理的结果（成功才通知，失败和异常不通知，
	*	商户通过订单结果查询来查询订单状态）数据通过服务器
	*	主动通知的方式通知给商户的通知地址，此地址就是支付
	*	请求时的serverCallbackUrl。通知机制为：订单创建时间24小时内，
	*	通知的时间间隔频率为 2 分钟，直到通知被商户成功接受为止，
	*	如果通知时间大于订单创建时间加24小时，
	*	也没有被商户接受成功则不再发通知，
	*	由商户通过订单结果查询接口进行查询
	*	（商户需要做好重复通知的接收控制，避免重复通知导致重复入账）
	*/
   //----------------异步通知接口
	@Override
	public String quickPayConfirmPay(QuickPayConfirmPayVo requestVo) {
		log.info("--------标准快捷异步通知接口----------"+JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue));
		if(null == requestVo){
			return "nullError";
		}
		RowLock lock = new RowLock();
		String resp = "success";
		try {
			lock.lock(lock.HELIBAO_KEY+requestVo.getRt5_orderId());
			HelibaoFilesConfig config = helibaoFilesConfigService.queryHelibaoFilesConfigInfo();
			if (!MessageHandle.checkSign(requestVo,config.getCertPath())) {
				resp = "signError";
			}else{
				helibaoFlowLogService.saveFlow(requestVo.getRt1_bizType(), requestVo.getRt5_orderId(), MyBeanUtils.convertBeanReq(requestVo,new LinkedHashMap()), null,"异步通知接口","");
				String resultStatus = requestVo.getRt9_orderStatus();
				if("SUCCESS".equals(resultStatus) || "FAILED".equals(resultStatus)){
					//  status (1 成功 0 失败)
					int status = 0;
					if("SUCCESS".equals(resultStatus)){
						status = 1;
					}
			        //具体业务处理
				}
			}
		} catch (Exception e) {
			log.error("异步通知接口-交易异常：" +JSON.toJSONString(requestVo, SerializerFeature.WriteMapNullValue), e.getMessage(), e);
			resp = "excError";
		}finally{
			lock.unLock(lock.HELIBAO_KEY+requestVo.getRt5_orderId());
		}
		
		return resp;
	}

	private static boolean isJSON(String test) {
		try {
			if(StringUtils.isBlank(test)){
				return false;
			}
			JSONObject.parseObject(test);
		} catch (JSONException ex) {
			return false;
		}
		return true;
	}

	
	
}
