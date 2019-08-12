/**
 * Copyright (c) 2015-2017, Javen  (javen205@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.creativearts.nyd.web.controller.unionpay;


import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.config.utils.DateKit;
import com.creativearts.nyd.pay.config.utils.HttpKit;
import com.creativearts.nyd.pay.model.AjaxResult;
import com.creativearts.nyd.pay.service.unionpay.UnionPayApi;
import com.creativearts.nyd.pay.service.unionpay.UnionPayApiConfig;
import com.creativearts.nyd.pay.service.unionpay.sdk.AcpService;
import com.creativearts.nyd.pay.service.unionpay.sdk.LogUtil;
import com.creativearts.nyd.pay.service.unionpay.sdk.SDKConfig;
import com.creativearts.nyd.web.controller.BaseController;
import com.nyd.zeus.model.PayModelEnum;
import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 */
@RestController
@RequestMapping(value = "/pay/union")
public class UnionPayController extends BaseController {
	static Logger log = LoggerFactory.getLogger(UnionPayController.class);
	private AjaxResult ajax = new AjaxResult();
//	@Autowired(required = false)
//	private RepayContract repayContract;

	@RequestMapping(value = "/index",produces = "text/plain;charset=UTF-8")
	public String index() {
		log.info("欢迎使用银联支付");
		return "欢迎使用银联支付";
	}

	/**
	 * PC网关支付
	 * B2C跟B2B查询区别就在于bizType的不同
	 */
	@RequestMapping("/frontConsume1")
	public void frontConsume() {
		try {
			Map<String, String> createMap = UnionPayApiConfig.builder()
//					.setChannelType("07") 默认值就是07
					.setMerId("777290058153147")
					.setTxnAmt("6666")
					.setReqReserved("reqReserved")
//					.setFrontUrl(SDKConfig.getConfig().getFrontUrl()) 有默认值
//					.setBackUrl(SDKConfig.getConfig().getBackUrl()) 有默认值
					.createMap();
			Map<String, String> submitFromData = AcpService.sign(createMap,DemoBase.encoding);
			UnionPayApi.frontRequest(getResponse(), submitFromData);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * B2B的网关支付
	 * B2C跟B2B查询区别就在于bizType的不同
	 */
	@RequestMapping("/frontConsume2")
	public void frontConsume2() {
		try {
			Map<String, String> createMap = UnionPayApiConfig.builder()
					.setBizType("000202")
					.setChannelType("07")
					.setMerId("777290058153147")
					.setTxnAmt("2222")
					.setReqReserved("reqReserved")
					.createMap();
			UnionPayApi.frontRequest(getResponse(), createMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * WAP支付 请在手机端访问此action
	 */
	@RequestMapping("/wapPay")
	public void wapConsume() {
		try {
			Map<String, String> createMap = UnionPayApiConfig.builder()
					.setChannelType("08")
					.setMerId("777290058154079")
					.setTxnAmt("6868")
//					.setReqReserved("1234")
					/*.setReqReserved("reqReserved")*/
//					.setFrontUrl(SDKConfig.getConfig().getFrontUrl())
//					.setBackUrl(SDKConfig.getConfig().getBackUrl())
					.createMap();
//			Map<String, String> submitFromData = AcpService.sign(createMap,DemoBase.encoding);
//			System.out.println("****************1");
			UnionPayApi.frontRequest(getResponse(), createMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * APP支付获取tn 控件支付
	 */
	@RequestMapping("/appConsume")
	@ResponseBody
	public AjaxResult appConsume() {
		try {
			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setChannelType("08")
					.setMerId("777290058154079")
					.setTxnAmt("8888").setAccType("01")
//					.setReqReserved("reqReserved")
					.setBackUrl(SDKConfig.getConfig().getBackUrl())
					.createMap();
			Map<String, String> rspData = UnionPayApi.AppConsumeByMap(reqData);
			// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
			if (!rspData.isEmpty()) {
				if (AcpService.validate(rspData, "UTF-8")) {
					log.info("验证签名成功");
					String respCode = rspData.get("respCode");
					if (("00").equals(respCode)) {
						// 成功,获取tn号
						String tn = rspData.get("tn");
						ajax.success(tn);
					} else {
						// 其他应答码为失败请排查原因或做失败处理
						ajax.addError(respCode);
					}
				} else {
					log.error("验证签名失败");
					// 检查验证签名失败的原因
					ajax.addError("验证签名失败");
				}
			} else {
				// 未返回正确的http状态
				log.error("未获取到返回报文或返回http状态码非200");
				ajax.addError("未获取到返回报文或返回http状态码非200");
			}
			String reqMessage = getHtmlResult(reqData);
			String rspMessage = getHtmlResult(rspData);
			log.info("app>reqMessage>>>" + reqMessage + " rspMessage>>>" + rspMessage);
			return ajax;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 订单状态查询
	 * B2C跟B2B查询区别就在于bizType的不同
	 */
	public void query(){
		Map<String, String> reqData = UnionPayApiConfig.builder()
				.setTxnType("00")
				.setTxnSubType("00")
				.setBizType("000301")
				.setMerId("777290058154079")
//				.setOrderId("1507214190872")
//				.setTxnTime("20171005223630")
				.setOrderId("1508488509626")
				.setTxnTime("20171020163509")
				.createMap();
		Map<String, String> rspData = UnionPayApi.singleQueryByMap(reqData);
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, "UTF-8")){
				log.info("验证签名成功");
				if("00".equals(rspData.get("respCode"))){//如果查询交易成功
					//处理被查询交易的应答码逻辑
					String origRespCode = rspData.get("origRespCode");
					if("00".equals(origRespCode)){
						//交易成功，更新商户订单状态
					}else if("03".equals(origRespCode) ||
							"04".equals(origRespCode) ||
							"05".equals(origRespCode)){
						//需再次发起交易状态查询交易
					}else{
						//其他应答码为失败请排查原因
					}
				}else{//查询交易本身失败，或者未查到原交易，检查查询交易报文要素
				}
			}else{
				log.error("验证签名失败");
				// 检查验证签名失败的原因
			}
		}else{
			//未返回正确的http状态
			log.error("未获取到返回报文或返回http状态码非200");
		}
		String reqMessage = getHtmlResult(reqData);
		String rspMessage = getHtmlResult(rspData);
		renderHtml("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
	}


	/**
	 * 订单状态查询 B2B
	 * B2C跟B2B查询区别就在于bizType的不同
	 */
	public void queryB2B(){
		Map<String, String> reqData = UnionPayApiConfig.builder()
				.setBizType("000202")
				.setTxnType("00")
				.setTxnSubType("00")
				.setMerId("777290058151764")
				.setOrderId("1507277885640")
				.setTxnTime("20171006161805")
				.createMap();
		Map<String, String> rspData = UnionPayApi.singleQueryByMap(reqData);
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, "UTF-8")){
				log.info("验证签名成功");
				if("00".equals(rspData.get("respCode"))){//如果查询交易成功
					//处理被查询交易的应答码逻辑
					String origRespCode = rspData.get("origRespCode");
					if("00".equals(origRespCode)){
						//交易成功，更新商户订单状态
					}else if("03".equals(origRespCode) ||
							"04".equals(origRespCode) ||
							"05".equals(origRespCode)){
						//需再次发起交易状态查询交易
					}else{
						//其他应答码为失败请排查原因
					}
				}else{//查询交易本身失败，或者未查到原交易，检查查询交易报文要素
				}
			}else{
				log.error("验证签名失败");
				// 检查验证签名失败的原因
			}
		}else{
			//未返回正确的http状态
			log.error("未获取到返回报文或返回http状态码非200");
		}
		String reqMessage = getHtmlResult(reqData);
		String rspMessage = getHtmlResult(rspData);
		renderHtml("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
	}

	/**
	 *
	 * https://open.unionpay.com/ajweb/help/faq/list?id=95&level=0&from=0
	 * java.lang.RuntimeException: Unexpected code Response{protocol=http/1.1, code=500, message=Internal Server Error, url=https://filedownload.test.95516.com/}
	 */
	public void fileTransfer(){
//		String settleDate = DateKit.toStr(new Date(), DateKit.UnionDateStampPattern);
		Map<String, String> reqData = UnionPayApiConfig.builder()
				.setAccessType("0")
				.setTxnType("76")  //交易类型 76-对账文件下载
				.setTxnSubType("01") //交易子类型 01-对账文件下载
				.setBizType("000000") //业务类型，固定
				.setMerId("700000000000001")
				.setSettleDate("0119") //清算日期，如果使用正式商户号测试则要修改成自己想要获取对账文件的日期， 测试环境如果使用777290058151764商户号则固定填写0119
				.setFileType("00")
				.createMap();
		//移除默认参数
		reqData.remove("orderId");
		reqData.remove("payTimeout");
		reqData.remove("backUrl");
		reqData.remove("channelType");
		reqData.remove("currencyCode");
		reqData.remove("frontUrl");
		reqData.remove("txnAmt");
		//重新签名
		reqData = UnionPayApiConfig.builder().setSignMap(reqData);
		Map<String, String> rspData = UnionPayApi.fileTransferByMap(reqData);
		String fileContentDispaly = "";
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, DemoBase.encoding)){
				log.info("验证签名成功");
				String respCode = rspData.get("respCode");
				if("00".equals(respCode)){
//					String outPutDirectory ="/Users/Javen/Documents/dev/JPay";
					String outPutDirectory ="d:\\";
					// 交易成功，解析返回报文中的fileContent并落地
					String zipFilePath = AcpService.deCodeFileContent(rspData,outPutDirectory,DemoBase.encoding);
					//对落地的zip文件解压缩并解析
					List<String> fileList = DemoBase.unzip(zipFilePath, outPutDirectory);
					//解析ZM，ZME文件
					fileContentDispaly ="<br>获取到商户对账文件，并落地到"+outPutDirectory+",并解压缩 <br>";
					for(String file : fileList){
						if(file.indexOf("ZM_")!=-1){
							@SuppressWarnings("rawtypes")
							List<Map> ZmDataList = DemoBase.parseZMFile(file);
							fileContentDispaly = fileContentDispaly+DemoBase.getFileContentTable(ZmDataList,file);
						}else if(file.indexOf("ZME_")!=-1){
							DemoBase.parseZMEFile(file);
						}
					}
					//其他处理
				}else{
					//其他应答码为失败请排查原因
				}
			}else{
				log.error("验证签名失败");
				// 检查验证签名失败的原因
			}
		}else{
			//未返回正确的http状态
			log.error("未获取到返回报文或返回http状态码非200");
		}

		String reqMessage = getHtmlResult(reqData);
		String rspMessage = getHtmlResult(rspData);
		renderHtml("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+fileContentDispaly);

	}
	/**
	 * 信用卡网关还款
	 */
	public void repaymentGateway(){
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("usr_num", "6221558812340013");//信用卡卡号
			map.put("usr_num2", "6221558812340013");//重复信用卡卡号
			map.put("usr_nm", "全渠道");//信用卡持卡人姓名【无特殊要求尽量不要送】这里填写错误会出现报文格式错误 (5100030)


			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setTxnType("13") //交易类型 04-退货	31-消费撤销 13 账单支付
					.setTxnSubType("03") //交易子类型  03 信用卡还款
					.setBizType("000601")
					.setChannelType("07") //渠道类型，07-PC，08-手机
					.setBussCode("J1_9800_0000_1")
					.setMerId("777290058151764")
					.setOrderId(String.valueOf(System.currentTimeMillis()))//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
					.setTxnTime(DateKit.toStr(new Date(), DateKit.UnionTimeStampPattern))//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
					.setTxnAmt("1234")
					.setBillQueryInfo(AcpService.base64Encode(JSON.toJSONString(map), DemoBase.encoding))
					.setBackUrl(SDKConfig.getConfig().getBackUrl())
					.setFrontUrl(SDKConfig.getConfig().getFrontUrl())
//					.setReqReserved("IJPay repaymentGateway")
					.createMap();
			UnionPayApi.jfFrontConsume(getResponse(), reqData);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	/**
	 * 代收后台建立委托(商户)  若用前台 需申请正式商户 去社情
	 */
	@RequestMapping("/jlwtBack")
	public ResponseData jlwtBack(){
		try {
			//卡号
			String accNoEnc = AcpService.encryptData("6216261000000000018", "UTF-8"); 		//这里测试的时候使用的是测试卡号，正式环境请使用真实卡号

			//姓名，证件类型+证件号码至少二选一必送，手机号可选，贷记卡的cvn2,expired可选。
			Map<String,String> customerInfoMap = new HashMap<String,String>();
			customerInfoMap.put("certifTp", "01");//证件类型
			customerInfoMap.put("certifId", "341126197709218366");//证件号码
			customerInfoMap.put("customerNm", "全渠道");//姓名

			customerInfoMap.put("phoneNo", "13552535506");//手机号
			//当卡号为贷记卡的时候cvn2,expired可选上送
//			customerInfoMap.put("cvn2", "123");//卡背面的cvn2三位数字
//			customerInfoMap.put("expired", "1711");

			String customerInfoStr = AcpService.
					getCustomerInfoWithEncrypt(customerInfoMap,null,DemoBase.encoding);
//			String customerInfoStr = AcpService.
//					getCustomerInfo(customerInfoMap,null,DemoBase.encoding);


			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setTxnType("72")
					.setTxnSubType("11")
					.setBizType("000501")
					.setChannelType("08") //渠道类型，07-PC，08-手机
					.setMerId("777290058154079")
					.setAccessType("0")
					.setAccType("01")
					.setOrderId(String.valueOf(System.currentTimeMillis()))//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
					.setTxnTime(DateKit.toStr(new Date(), DateKit.UnionTimeStampPattern))//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
					.setAccNo(accNoEnc)
					.setEncryptCertId(AcpService.getEncryptCertId())//加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
					.setCustomerInfo(customerInfoStr)
					.createMap();
			Map<String, String> rspData = UnionPayApi.backRequestByMap(reqData);
			if(!rspData.isEmpty()){
				if(AcpService.validate(rspData, DemoBase.encoding)){
					LogUtil.writeLog("验证签名成功");
					String respCode = rspData.get("respCode") ;
					if(("00").equals(respCode)){
						//成功,获取tn号
						return ResponseData.success();
					}else{
						//其他应答码为失败请排查原因或做失败处理
						return ResponseData.error(rspData.get("respMsg"));
					}
				}else{
					LogUtil.writeErrorLog("验证签名失败");
					//检查验证签名失败的原因
					return ResponseData.error("验证签名失败");
				}
			}else{
				//未返回正确的http状态
				LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
				return ResponseData.error("未获取到返回报文或返回http状态码非200");
			}

//			String reqMessage = getHtmlResult(reqData);
//			String rspMessage = getHtmlResult(rspData);
//			renderHtml(rspMessage);
//			System.out.println("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
//			renderHtml("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseData.error(e.getMessage());
		}

	}


	/**
	 * 代收前台建立委托(用户)
	 */
	@RequestMapping("/jlwtFront")
	public void jlwtFront(){
		try {
			//卡号
			String accNoEnc = AcpService.encryptData("6216261000000000018", "UTF-8"); 		//这里测试的时候使用的是测试卡号，正式环境请使用真实卡号

			//姓名，证件类型+证件号码至少二选一必送，手机号可选，贷记卡的cvn2,expired可选。
			Map<String,String> customerInfoMap = new HashMap<String,String>();
			customerInfoMap.put("certifTp", "01");//证件类型
			customerInfoMap.put("certifId", "341126197709218366");//证件号码
			customerInfoMap.put("customerNm", "全渠道");//姓名

			customerInfoMap.put("phoneNo", "13552535506");//手机号
			//当卡号为贷记卡的时候cvn2,expired可选上送
//			customerInfoMap.put("cvn2", "123");//卡背面的cvn2三位数字
//			customerInfoMap.put("expired", "2311");

			String customerInfoStr = AcpService.
					getCustomerInfoWithEncrypt(customerInfoMap,null,DemoBase.encoding);
//			String customerInfoStr = AcpService.
//					getCustomerInfo(customerInfoMap,null,DemoBase.encoding);


			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setTxnType("72")
					.setTxnSubType("11")
					.setBizType("000501")
					.setChannelType("07") //渠道类型，07-PC，08-手机
					.setMerId("777290058154079")
					.setAccessType("0")
					.setAccType("01")
					.setOrderId(String.valueOf(System.currentTimeMillis()))//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
					.setTxnTime(DateKit.toStr(new Date(), DateKit.UnionTimeStampPattern))//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
					.setAccNo(accNoEnc)
					.setBackUrl(SDKConfig.getConfig().getBackUrl())
					.setFrontUrl(SDKConfig.getConfig().getFrontUrl())
					.setEncryptCertId(AcpService.getEncryptCertId())//加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
					.setCustomerInfo(customerInfoStr)
					.createMap();
			UnionPayApi.frontRequest(getResponse(), reqData);
		} catch (Exception e) {
			e.printStackTrace();
//			renderText("exception");
		}
//		renderText("OK");
	}
	/**
	 * 解除委托关系
	 */
	public void removeWt(){
		try {
			//这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
			String accNoEnc = AcpService.encryptData("6221558812340000", "UTF-8");

			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setTxnType("74")
					.setTxnSubType("04")
					.setBizType("000501")
					.setChannelType("07") //渠道类型，07-PC，08-手机
					.setMerId("777290058154079")
					.setOrderId(String.valueOf(System.currentTimeMillis()))//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
					.setTxnTime(DateKit.toStr(new Date(), DateKit.UnionTimeStampPattern))//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
					.setAccNo(accNoEnc)
					.setEncryptCertId(AcpService.getEncryptCertId())//加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
					.createMap();
			Map<String, String> rspData = UnionPayApi.backRequestByMap(reqData);
			if(!rspData.isEmpty()){
				if(AcpService.validate(rspData, DemoBase.encoding)){
					LogUtil.writeLog("验证签名成功");
					String respCode = rspData.get("respCode") ;
					if(("00").equals(respCode)){
						//成功
					}else{
						//其他应答码为失败请排查原因或做失败处理
					}
				}else{
					LogUtil.writeErrorLog("验证签名失败");
					//检查验证签名失败的原因
				}
			}else{
				//未返回正确的http状态
				LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
			}

			String reqMessage = getHtmlResult(reqData);
			String rspMessage = getHtmlResult(rspData);
			renderHtml("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 代收1102
	 */
	@RequestMapping("/ds")
	public ResponseData daiShou1102(){
		try {
			//卡号
			String accNoEnc = AcpService.encryptData("6216261000000000018", DemoBase.encoding);


			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setTxnType("11")
					.setTxnSubType("02")
					.setBizType("000501")
					.setChannelType("08") //渠道类型，07-PC，08-手机
					.setMerId("777290058154079")
					.setAccessType("0")
					.setAccType("01")
					.setTxnAmt("8888")
					.setOrderId(String.valueOf(System.currentTimeMillis()))//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
					.setTxnTime(DateKit.toStr(new Date(), DateKit.UnionTimeStampPattern))//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
					.setAccNo(accNoEnc)
					.setEncryptCertId(AcpService.getEncryptCertId())//加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
					.createMap();
			Map<String, String> rspData = UnionPayApi.backRequestByMap(reqData);

			if(!rspData.isEmpty()){
				if(AcpService.validate(rspData, DemoBase.encoding)){
					LogUtil.writeLog("验证签名成功");
					String respCode = rspData.get("respCode") ;
					if(("00").equals(respCode)){
						//交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
						//如果是配置了敏感信息加密，如果需要获取卡号的铭文，可以按以下方法解密卡号
						//String accNo1 = resmap.get("accNo");
						//String accNo2 = AcpService.decryptData(accNo1, "UTF-8");  //解密卡号使用的证书是商户签名私钥证书acpsdk.signCert.path
						//LogUtil.writeLog("解密后的卡号："+accNo2);
//						System.out.println("sucess");
						return ResponseData.success();
					}else if(("03").equals(respCode)||
							("04").equals(respCode)||
							("05").equals(respCode)){
						//后续需发起交易状态查询交易确定交易状态
						ResponseData responseData = new ResponseData();
						responseData.setStatus("3");
						responseData.setMsg(rspData.get("respMsg"));
//						return ResponseData.error("后续需发起交易状态查询交易确定交易状态");
						return responseData;
					}else{
						//其他应答码为失败请排查原因
						return ResponseData.error(rspData.get("respMsg"));
					}
				}else{
					LogUtil.writeErrorLog("验证签名失败");
					//检查验证签名失败的原因
					return ResponseData.error("验证签名失败");
				}
			}else{
				//未返回正确的http状态
				LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
				return ResponseData.error("未获取到返回报文或返回http状态码非200");
			}

//			String reqMessage = getHtmlResult(reqData);
//			String rspMessage = getHtmlResult(rspData);
//			renderHtml("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseData.error(e.getMessage());
		}

	}

	/**
	 * 无跳转开通状态查询
	 */
	@RequestMapping("/openQuery")
	public void openQuery(){
		try {
			//卡号
			String accNoEnc = AcpService.encryptData("6216261000000000018", DemoBase.encoding);

			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setTxnType("78")
					.setTxnSubType("00")
					.setBizType("000301")
					.setChannelType("07") //渠道类型，07-PC，08-手机
					.setMerId("777290058151764")
					.setAccessType("0")
					.setOrderId(String.valueOf(System.currentTimeMillis()))//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
					.setTxnTime(DateKit.toStr(new Date(), DateKit.UnionTimeStampPattern))//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
					.setAccNo(accNoEnc)
					.setEncryptCertId(AcpService.getEncryptCertId())//加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
					.createMap();
			Map<String, String> rspData = UnionPayApi.backRequestByMap(reqData);
			StringBuffer parseStr = new StringBuffer("");
			if(!rspData.isEmpty()){
				if(AcpService.validate(rspData, DemoBase.encoding)){
					LogUtil.writeLog("验证签名成功");
					String respCode = rspData.get("respCode") ;
					if(("00").equals(respCode)){
						//成功
						parseStr.append("<br>解析敏感信息加密信息如下（如果有）:<br>");
						String customerInfo = rspData.get("customerInfo");
						if(null!=customerInfo){
							Map<String,String>  cm = AcpService.parseCustomerInfo(customerInfo, "UTF-8");
							parseStr.append("customerInfo明文: " + cm+"<br>");
						}
						String an = rspData.get("accNo");
						if(null!=an){
							an = AcpService.decryptData(an, "UTF-8");
							parseStr.append("accNo明文: " + an);
						}
					}else{
						//其他应答码为失败请排查原因或做失败处理
					}
				}else{
					LogUtil.writeErrorLog("验证签名失败");
					// 检查验证签名失败的原因
				}
			}else{
				//未返回正确的http状态
				LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
			}
			String reqMessage = getHtmlResult(reqData);
			String rspMessage = getHtmlResult(rspData);
			renderHtml("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+parseStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 无跳转支付银联侧开通
	 */
	@RequestMapping("/openCardFront")
	public void openCardFront(){
		try {
			//卡号
			String accNoEnc = AcpService.encryptData("6216261000000000018", "UTF-8"); 		//这里测试的时候使用的是测试卡号，正式环境请使用真实卡号

			//姓名，证件类型+证件号码至少二选一必送，手机号可选，贷记卡的cvn2,expired可选。
			Map<String,String> customerInfoMap = new HashMap<String,String>();
			customerInfoMap.put("certifTp", "01");//证件类型
			customerInfoMap.put("certifId", "341126197709218366");//证件号码
			customerInfoMap.put("customerNm", "全渠道");//姓名
			customerInfoMap.put("phoneNo", "13552535506");//手机号

			String customerInfoStr = AcpService.
					getCustomerInfoWithEncrypt(customerInfoMap,null,DemoBase.encoding);
//			String customerInfoStr = AcpService.
//					getCustomerInfo(customerInfoMap,null,DemoBase.encoding);


			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setTxnType("79")
					.setTxnSubType("01")
					.setBizType("000301")
					.setChannelType("07") //渠道类型，07-PC，08-手机
					.setMerId("777290058151764")
					.setAccessType("0")
					.setAccType("01")
					.setOrderId(String.valueOf(System.currentTimeMillis()))//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
					.setTxnTime(DateKit.toStr(new Date(), DateKit.UnionTimeStampPattern))//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
					.setAccNo(accNoEnc)
					.setBackUrl(SDKConfig.getConfig().getBackUrl())
					.setFrontUrl(SDKConfig.getConfig().getFrontUrl())
					.setEncryptCertId(AcpService.getEncryptCertId())//加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
					.setCustomerInfo(customerInfoStr)
					.createMap();
			UnionPayApi.frontRequest(getResponse(), reqData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText("OK");
	}

	/**
	 * 无跳转支付银联测开通并消费
	 */
	@RequestMapping("/openAndConsume")
	public void openAndConsume(){
		try {
			//卡号
			String accNoEnc = AcpService.encryptData("6216261000000000018", DemoBase.encoding);


			Map<String, String> reqData = UnionPayApiConfig.builder()
					.setTxnType("01")
					.setTxnSubType("01")
					.setBizType("000301")
					.setChannelType("07") //渠道类型，07-PC，08-手机
					.setMerId("777290058151764")
					.setAccessType("0")
					.setAccType("01")
					.setTxnAmt("6363")
					.setOrderId(String.valueOf(System.currentTimeMillis()))//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
					.setTxnTime(DateKit.toStr(new Date(), DateKit.UnionTimeStampPattern))//订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
					.setAccNo(accNoEnc)
					.setEncryptCertId(AcpService.getEncryptCertId())//加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
					.setBackUrl(SDKConfig.getConfig().getBackUrl())
					.setFrontUrl(SDKConfig.getConfig().getFrontUrl())
					.createMap();
			UnionPayApi.frontRequest(getResponse(), reqData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText("OK");
	}


	/**
	 * 后台回调
	 */
	@RequestMapping("/backRcvResponse")
	public void backRcvResponse() throws Exception{
		log.info("BackRcvResponse接收后台通知开始");
//		System.out.println("BackRcvResponse接收后台通知开始");
		String encoding = "UTF-8";

		String notifyStr = HttpKit.readData(getRequest());
		// 获取银联通知服务器发送的后台通知参数
//		System.out.println("获取银联通知服务器发送的后台通知参数"+notifyStr);
		Map<String, String> reqParam = getAllRequestParamToMap(notifyStr);
//		LogUtil.printRequestLog(reqParam);
//		System.out.println(JSON.toJSONString(reqParam));
		// 重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
		if (!AcpService.validate(reqParam, encoding)) {
			System.out.println("后台验证签名结果[失败].");
			// 验签失败，需解决验签问题

		} else {
			System.out.println("后台验证签名结果[成功].");
			// 【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
			RepayInfo repayInfo = new RepayInfo();
			repayInfo.setBillNo(reqParam.get("orderId"));
			repayInfo.setUserId(reqParam.get("reqReserved"));
			repayInfo.setRepayNo(reqParam.get("queryId"));
			repayInfo.setRepayChannel(PayModelEnum.YL.getCode());
			repayInfo.setRepayAmount(new BigDecimal(Double.valueOf(reqParam.get("txnAmt"))/100));
			repayInfo.setRepayTime(DateUtils.parseDate(reqParam.get("txnTime"),"yyyyMMddHHmmss"));

			String respCode = reqParam.get("respCode");
			if("00".equals(respCode)||"A6".equals(respCode)){
				repayInfo.setRepayStatus("0");
			}else{
				repayInfo.setRepayStatus("1");
			}

//			repayContract.save(repayInfo);

			// 判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
//			System.out.println("orderId>>>" + orderId + " respCode>>" + respCode);
		}
		System.out.println("BackRcvResponse接收后台通知结束");
		// 返回给银联服务器http 200 状态码
		renderText("ok");
	}

	/**
	 * 前台回调
	 */
	@RequestMapping("/frontRcvResponse")
	public void frontRcvResponse() {
		try {
			log.info("FrontRcvResponse前台接收报文返回开始");
			System.out.println("FrontRcvResponse前台接收报文返回开始");
			String encoding = "UTF-8";
			System.out.println("返回报文中encoding=[" + encoding + "]");
			String readData = HttpKit.readData(getRequest());
			Map<String, String> respParam = getAllRequestParamToMap(readData);
			System.out.println(JSON.toJSONString(respParam));
			// 打印请求报文
			LogUtil.printRequestLog(respParam);

			Map<String, String> valideData = null;
			StringBuffer page = new StringBuffer();
			if (null != respParam && !respParam.isEmpty()) {
				Iterator<Entry<String, String>> it = respParam.entrySet().iterator();
				valideData = new HashMap<String, String>(respParam.size());
				while (it.hasNext()) {
					Entry<String, String> e = it.next();
					String key = (String) e.getKey();
					String value = (String) e.getValue();
					value = new String(value.getBytes(encoding), encoding);
					page.append("<tr><td width=\"30%\" align=\"right\">" + key + "(" + key + ")</td><td>" + value
							+ "</td></tr>");
					valideData.put(key, value);
				}
			}
			if (!AcpService.validate(valideData, encoding)) {
				page.append("<tr><td width=\"30%\" align=\"right\">前台验证签名结果</td><td>失败</td></tr>");
				System.out.println("前台验证签名结果[失败].");
				try {
					getResponse().sendRedirect("http://192.168.2.10:8080/#/repayment-tip?callback=error");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				page.append("<tr><td width=\"30%\" align=\"right\">前台验证签名结果</td><td>成功</td></tr>");
				System.out.println("前台验证签名结果[成功].");
				String orderId = valideData.get("orderId"); // 其他字段也可用类似方式获取

				String respCode = valideData.get("respCode");
				// 判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
				System.out.println("orderId>>>" + orderId + " respCode>>" + respCode);

				try {
					getResponse().sendRedirect("http://192.168.2.10:8080/#/repayment-tip?callback=success");
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
//			getRequest().setAttribute("result", page.toString());
////			render("/WEB-INF/_views/utf8_result.html");
//			renderText("前台回调result");
//			System.out.println("FrontRcvResponse前台接收报文返回结束");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		try {
//			getResponse().sendRedirect("http://www.sina.com.cn/");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

	/**
	 * 将回调参数转为Map
	 *
	 * @param notifyStr
	 * @return {Map<String, String>}
	 */
	public static Map<String, String> getAllRequestParamToMap(final String notifyStr) {
		Map<String, String> res = new HashMap<String, String>();
		try {
			log.info("收到通知报文：" + notifyStr);
			String[] kvs = notifyStr.split("&");
			for (String kv : kvs) {
				String[] tmp = kv.split("=");
				if (tmp.length >= 2) {
					String key = tmp[0];
					String value = URLDecoder.decode(tmp[1], "UTF-8");
					res.put(key, value);
				}
			}
		} catch (UnsupportedEncodingException e) {
			log.info("getAllRequestParamStream.UnsupportedEncodingException error: " + e.getClass() + ":"
					+ e.getMessage());
		}
		return res;
	}

	/**
	 * 组装请求，返回报文字符串用于显示
	 *
	 * @param data
	 * @return {String}
	 */
	public static String getHtmlResult(Map<String, String> data) {

		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			String key = en.getKey();
			String value = en.getValue();
			if ("respCode".equals(key)) {
				sf.append("<b>" + key + "=" + value + "</br></b>");
			} else
				sf.append(key + "=" + value + "</br>");
		}
		return sf.toString();
	}
}
