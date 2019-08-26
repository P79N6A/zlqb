package com.nyd.zeus.service.impls.zzl.xunlian;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.container.Main;
import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.api.zzl.xunlian.XunlianGetDataService;
import com.nyd.zeus.api.zzl.xunlian.XunlianPayService;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.SqlHelper;
import com.nyd.zeus.model.helibao.util.chanpay.DateUtils;
import com.nyd.zeus.model.helibao.util.xunlian.ECTXmlUtil;
import com.nyd.zeus.model.helibao.util.xunlian.HttpClientUtil;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileVO;
import com.nyd.zeus.model.payment.PaychannelTempFlow;
import com.nyd.zeus.model.xunlian.req.IdentifyauthVO;
import com.nyd.zeus.model.xunlian.req.XunlianCancelBindVO;
import com.nyd.zeus.model.xunlian.req.XunlianChargeEnterVO;
import com.nyd.zeus.model.xunlian.req.XunlianChargeVO;
import com.nyd.zeus.model.xunlian.req.XunlianPaymentVO;
import com.nyd.zeus.model.xunlian.req.XunlianQueryChargeVO;
import com.nyd.zeus.model.xunlian.req.XunlianQueryPayVO;
import com.nyd.zeus.model.xunlian.req.XunlianSignVO;
import com.nyd.zeus.model.xunlian.resp.IdentifyResp;
import com.nyd.zeus.model.xunlian.resp.XunlianChargeResp;
import com.nyd.zeus.model.xunlian.resp.XunlianPayResp;
import com.nyd.zeus.model.xunlian.resp.XunlianQueryChargeResp;
import com.nyd.zeus.model.xunlian.resp.XunlianQueryPayResp;
import com.nyd.zeus.service.impls.zzl.chanpay.PayConfigFileService;

@Service(value = "xunlianPayService")
public class XunlianPayServiceimpl implements XunlianPayService {

	private Logger logger = LoggerFactory.getLogger(XunlianPayServiceimpl.class);

	private static String BINGTA_XUNLIAN_PAY = "200008-0012";
	
	private static String DAQING_XUNLIAN_PAY = "200008-0001";

	@Autowired
	private XunlianGetDataService xunlianGetDataService;

	@Autowired
	private PayConfigFileService payConfigFileService;

	@Autowired
	private ZeusSqlService zeusSqlService;

	private static String IDEN_URL = null;

	private static String SIGN_URL = null;

	private static String PAY_URL = null;

	private static String QUERY_PAY_URL = null;

	private static String CHARGE = null;

	private static String QUERY_CHARGE = null;

	private static String CANCEL_URL = null;

	@Override
	public CommonResponse<IdentifyResp> sendMsg(IdentifyauthVO identifyauthVO) {
		CommonResponse<IdentifyResp> common = new CommonResponse<IdentifyResp>();
		IdentifyResp resp = new IdentifyResp();

		try {
			// 入流水表
			PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
			paychannelTempFlow.setBusinessType("身份认证");
			paychannelTempFlow.setPayChannelCode("xunlian");
			List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(BINGTA_XUNLIAN_PAY);
			PayConfigFileVO payConfigFileVO = list.get(0);
			String serialNo = getSerialNum();
			resp.setMerOrderId(serialNo);
			paychannelTempFlow.setSeriNo(serialNo);
			identifyauthVO.setMerOrderId(serialNo);
			Map<String, String> map = xunlianGetDataService.getIdentifyAuthData(identifyauthVO, payConfigFileVO);
			// 获取请求地址
			setUrlContent(payConfigFileVO);
			// String url =
			// "https://www.tongtongcf.com:9204/pgw-quickpay/quickpay/identifyauth";
			String url = IDEN_URL;
			logger.info(" 请求身份信息认证地址为" + url);
			// 拼装成xml请求报文，并发送post请求
			// 这里只是给出了一种写法，开发者可以自由编写，只要请求报文符合接口文档的规范
			String xmlString = ECTXmlUtil.mapToXml(map, ECTXmlUtil.CPREQ_QIAREQ);
			logger.info("商户身份认证接口测试请求报文：" + xmlString);
			paychannelTempFlow.setRequestText(xmlString);
			paychannelTempFlow.setRequestTime(new Date());
			String responseString = HttpClientUtil.postToServerByXml(xmlString, url);
			logger.info("证通返回报文：" + responseString);
			paychannelTempFlow.setResponseText(responseString);
			paychannelTempFlow.setResponseTime(new Date());
			// 保存流水表
			try {
				zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
			} catch (Exception e) {
				logger.error(" xunlian 保存身份认证流水信息异常" + e + e.getMessage());
			}
			// 将返回的xml字符串解析成map，map中包含了<CSReq>标签内的元素
			Map<String, String> resultMap = ECTXmlUtil.xmlToMap(responseString);
			// String
			resp.setProtocolId(resultMap.get("protocolId"));
			resp.setResultCode(resultMap.get("resultCode"));
			resp.setResultMsg(resultMap.get("resultMsg"));
			resp.setRespDate(resultMap.get("respDate"));
			resp.setRetFlag(resultMap.get("retFlag"));
			resp.setSmsSendNo(resultMap.get("smsSendNo"));
			common.setData(resp);
			common.setSuccess(true);
			// String result_sign = resultMap.get("sign");
			// String to_verify =
			// MerchantSignAndVerify.createLinkString(resultMap);

			// 调用CFCA方法进行验签
			// if (MerchantSignAndVerify.verify(to_verify.getBytes(),
			// result_sign.getBytes())) {
			// logger.info("验签成功");
			// } else {
			// logger.error("验签失败");
			// }
		} catch (Exception e) {
			logger.error(" 证通身份验证异常:" + e + e.getMessage());
			common.setSuccess(false);
			e.printStackTrace();
		}
		return common;
	}

	public String getSerialNum() {
		return new SimpleDateFormat("YYYYMMDDHHmmssSSS").format(new Date())
				+ String.valueOf((Math.random() * 1000000)).substring(0, 5);
	}

	@Override
	public CommonResponse<IdentifyResp> sign(XunlianSignVO xunlianSignVO) {
		CommonResponse<IdentifyResp> common = new CommonResponse<IdentifyResp>();
		IdentifyResp resp = new IdentifyResp();
		try {
			// 入流水表
			PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
			paychannelTempFlow.setBusinessType("确认绑卡");
			paychannelTempFlow.setPayChannelCode("xunlian");
			paychannelTempFlow.setSeriNo(xunlianSignVO.getMerOrderId());
			List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(BINGTA_XUNLIAN_PAY);
			PayConfigFileVO payConfigFileVO = list.get(0);
			Map<String, String> map = xunlianGetDataService.getSignData(xunlianSignVO, payConfigFileVO);

			// 获取请求地址
			setUrlContent(payConfigFileVO);
			// String url =
			// "https://www.tongtongcf.com:9204/pgw-quickpay/quickpay/sign";
			String url = SIGN_URL;
			logger.info("xunlian 请求确认绑卡地址为" + url);

			// 拼装成xml请求报文，并发送post请求
			// 这里只是给出了一种写法，开发者可以自由编写，只要请求报文符合接口文档的规范
			String xmlString = ECTXmlUtil.mapToXml(map, ECTXmlUtil.CPREQ_QIAREQ);
			paychannelTempFlow.setRequestText(xmlString);
			paychannelTempFlow.setRequestTime(new Date());
			logger.info("商户身份认证接口测试请求报文：" + xmlString);
			String responseString = HttpClientUtil.postToServerByXml(xmlString, url);
			logger.info("证通返回报文：" + responseString);
			paychannelTempFlow.setResponseText(responseString);
			paychannelTempFlow.setResponseTime(new Date());

			// 保存流水表
			try {
				zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
			} catch (Exception e) {
				logger.error(" xunlian 确认绑卡流水信息异常" + e + e.getMessage());
			}

			// 将返回的xml字符串解析成map，map中包含了<CSReq>标签内的元素
			Map<String, String> resultMap = ECTXmlUtil.xmlToMap(responseString);
			resp.setProtocolId(resultMap.get("protocolId"));
			resp.setResultCode(resultMap.get("resultCode"));
			resp.setResultMsg(resultMap.get("resultMsg"));
			resp.setRespDate(resultMap.get("respDate"));
			resp.setRetFlag(resultMap.get("retFlag"));
			resp.setSmsSendNo(resultMap.get("smsSendNo"));
			common.setData(resp);
			common.setSuccess(true);
			// String result_sign = resultMap.get("sign");
			// String to_verify =
			// MerchantSignAndVerify.createLinkString(resultMap);
			// // 调用CFCA方法进行验签
			// if (MerchantSignAndVerify.verify(to_verify.getBytes(),
			// result_sign.getBytes())) {
			// logger.info("验签成功");
			// } else {
			// logger.error("验签失败");
			// }

		} catch (Exception e) {
			logger.error(" 证通确认签约异常:" + e + e.getMessage());
			e.printStackTrace();
			common.setSuccess(false);
		}
		return common;
	}

	@Override
	public CommonResponse<XunlianPayResp> pay(XunlianPaymentVO xunlianPaymentVO,String orderTime) {
		CommonResponse<XunlianPayResp> common = new CommonResponse<XunlianPayResp>();
		XunlianPayResp resp = new XunlianPayResp();
		try {
			String code = BINGTA_XUNLIAN_PAY;
			if(StringUtils.isNotEmpty(orderTime)){
				Date inDate = DateUtils.formatDate(orderTime, DateUtils.STYLE_1);
				Date sysDate = DateUtils.formatDate("2019-08-26 17:30:00", DateUtils.STYLE_1);
				//Date sysDate = DateUtils.formatDate("2019-08-23 19:00:00", DateUtils.STYLE_1);
				int compareTo = inDate.compareTo(sysDate);
				if(compareTo < 0){
					code =DAQING_XUNLIAN_PAY;
				}
			}
			logger.info(" xumlian 商户：" + code);
			// 入流水表
			PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
			paychannelTempFlow.setBusinessType("代扣");
			paychannelTempFlow.setPayChannelCode("xunlian");
			List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
			PayConfigFileVO payConfigFileVO = list.get(0);
			String serialNo = getSerialNum();
			resp.setPayOrderId(serialNo);
			paychannelTempFlow.setSeriNo(serialNo);
			xunlianPaymentVO.setPayOrderId(serialNo);
			Map<String, String> map = xunlianGetDataService.getPayData(xunlianPaymentVO, payConfigFileVO);

			// 获取请求地址
			setUrlContent(payConfigFileVO);
			// String url =
			// "https://www.tongtongcf.com:9204/pgw-quickpay/quickpay/pay";
			String url = PAY_URL;
			logger.info("xunlian 支付地址为" + url);

			// 拼装成xml请求报文，并发送post请求
			// 这里只是给出了一种写法，开发者可以自由编写，只要请求报文符合接口文档的规范
			String xmlString = ECTXmlUtil.mapToXml(map, ECTXmlUtil.CPREQ_QIAREQ);
			paychannelTempFlow.setRequestText(xmlString);
			paychannelTempFlow.setRequestTime(new Date());
			logger.info("商户身份认证接口测试请求报文：" + xmlString);
			String responseString = HttpClientUtil.postToServerByXml(xmlString, url);
			logger.info("证通返回报文：" + responseString);
			paychannelTempFlow.setResponseText(responseString);
			paychannelTempFlow.setResponseTime(new Date());

			// 保存流水表
			try {
				zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
			} catch (Exception e) {
				logger.error(" xunlian 代扣流水信息异常" + e + e.getMessage());
			}
			// 将返回的xml字符串解析成map，map中包含了<CSReq>标签内的元素
			Map<String, String> resultMap = ECTXmlUtil.xmlToMap(responseString);

			resp.setProtocolId(resultMap.get("protocolId"));
			resp.setResultCode(resultMap.get("resultCode"));
			resp.setResultMsg(resultMap.get("resultMsg"));
			resp.setRespDate(resultMap.get("respDate"));
			resp.setRetFlag(resultMap.get("retFlag"));
			resp.setSmsSendNo(resultMap.get("smsSendNo"));
			resp.setName(resultMap.get("name"));
			resp.setAccount(resultMap.get("account"));
			common.setData(resp);
			common.setSuccess(true);

			// String result_sign = resultMap.get("sign");
			// String to_verify =
			// MerchantSignAndVerify.createLinkString(resultMap);
			// // 调用CFCA方法进行验签
			// if (MerchantSignAndVerify.verify(to_verify.getBytes(),
			// result_sign.getBytes())) {
			// logger.info("验签成功");
			// } else {
			// logger.error("验签失败");
			// }

		} catch (Exception e) {
			logger.error(" 证通确认签约异常:" + e + e.getMessage());
			e.printStackTrace();
			common.setSuccess(false);

		}
		return common;
	}

	@Override
	public CommonResponse<XunlianQueryPayResp> queryPay(XunlianQueryPayVO xunlianQueryPayVO,String orderTime) {
		CommonResponse<XunlianQueryPayResp> common = new CommonResponse<XunlianQueryPayResp>();
		XunlianQueryPayResp resp = new XunlianQueryPayResp();
		try {
			String code = BINGTA_XUNLIAN_PAY;
			if(StringUtils.isNotEmpty(orderTime)){
				Date inDate = DateUtils.formatDate(orderTime, DateUtils.STYLE_1);
				Date sysDate = DateUtils.formatDate("2019-08-26 17:30:00", DateUtils.STYLE_1);
				//Date sysDate = DateUtils.formatDate("2019-08-23 19:00:00", DateUtils.STYLE_1);

				int compareTo = inDate.compareTo(sysDate);
				if(compareTo < 0){
					code =DAQING_XUNLIAN_PAY;
				}
			}
			
			logger.info(" xumlian 商户：" + code);
			// 入流水表
			PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
			paychannelTempFlow.setBusinessType("代扣查询");
			paychannelTempFlow.setPayChannelCode("xunlian");
			paychannelTempFlow.setSeriNo(xunlianQueryPayVO.getMerOrderId());
			List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
			PayConfigFileVO payConfigFileVO = list.get(0);
			Map<String, String> map = xunlianGetDataService.getQueryPayData(xunlianQueryPayVO, payConfigFileVO);
			setUrlContent(payConfigFileVO);
			// String url =
			// "https://www.tongtongcf.com:9204/pgw-quickpay/quickpay/tradestatus";
			String url = QUERY_PAY_URL;
			logger.info("xunlian 支付查询请求地址为" + url);
			// 拼装成xml请求报文，并发送post请求
			// 这里只是给出了一种写法，开发者可以自由编写，只要请求报文符合接口文档的规范
			String xmlString = ECTXmlUtil.mapToXml(map, ECTXmlUtil.CPREQ_QIAREQ);
			logger.info("商户身份认证接口测试请求报文：" + xmlString);
			paychannelTempFlow.setRequestText(xmlString);
			paychannelTempFlow.setRequestTime(new Date());
			String responseString = HttpClientUtil.postToServerByXml(xmlString, url);
			logger.info("证通返回报文：" + responseString);
			paychannelTempFlow.setResponseText(responseString);
			paychannelTempFlow.setResponseTime(new Date());
			// 保存流水表
			try {
				zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
			} catch (Exception e) {
				logger.error(" xunlian 代扣查询流水信息异常" + e + e.getMessage());
			}
			// 将返回的xml字符串解析成map，map中包含了<CSReq>标签内的元素
			Map<String, String> resultMap = ECTXmlUtil.xmlToMap(responseString);
			resp.setQueryResult(resultMap.get("queryResult"));
			resp.setType(resultMap.get("type"));
			resp.setProtocolId(resultMap.get("protocolId"));
			resp.setResultCode(resultMap.get("resultCode"));
			resp.setResultMsg(resultMap.get("resultMsg"));
			resp.setRespDate(resultMap.get("respDate"));
			resp.setRetFlag(resultMap.get("retFlag"));
			resp.setSmsSendNo(resultMap.get("smsSendNo"));
			resp.setAccount(resultMap.get("account"));
			common.setData(resp);
			common.setSuccess(true);

			// String result_sign = resultMap.get("sign");
			// String to_verify =
			// MerchantSignAndVerify.createLinkString(resultMap);
			// // 调用CFCA方法进行验签
			// if (MerchantSignAndVerify.verify(to_verify.getBytes(),
			// result_sign.getBytes())) {
			// logger.info("验签成功");
			// } else {
			// logger.error("验签失败");
			// }

		} catch (Exception e) {
			logger.error(" 证通确认签约异常:" + e + e.getMessage());
			e.printStackTrace();
		}

		return common;
	}


	@Override
	public CommonResponse<JSONObject> cancelBind(XunlianCancelBindVO xunlianCancelBindVO) {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();

		try {
			// 入流水表
			PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
			paychannelTempFlow.setBusinessType("取消绑卡");
			paychannelTempFlow.setPayChannelCode("xunlian");
			List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(BINGTA_XUNLIAN_PAY);
			PayConfigFileVO payConfigFileVO = list.get(0);
			Map<String, String> map = xunlianGetDataService.getCancelBindData(xunlianCancelBindVO, payConfigFileVO);

			// 获取请求地址
			setUrlContent(payConfigFileVO);
			// String url =
			// "https://www.tongtongcf.com:9204/pgw-quickpay/quickpay/tradestatus";
			String url = CANCEL_URL;
			logger.info("xunlian 取消绑卡请求地址为" + url);
			// 拼装成xml请求报文，并发送post请求
			// 这里只是给出了一种写法，开发者可以自由编写，只要请求报文符合接口文档的规范
			String xmlString = ECTXmlUtil.mapToXml(map, ECTXmlUtil.CPREQ_QIAREQ);
			logger.info("商户身份认证接口测试请求报文：" + xmlString);
			paychannelTempFlow.setRequestText(xmlString);
			paychannelTempFlow.setRequestTime(new Date());
			String responseString = HttpClientUtil.postToServerByXml(xmlString, url);
			logger.info("证通返回报文：" + responseString);
			paychannelTempFlow.setResponseText(responseString);
			paychannelTempFlow.setResponseTime(new Date());

			// 保存流水表
			try {
				zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
			} catch (Exception e) {
				logger.error(" xunlian 取消绑卡流水信息异常" + e + e.getMessage());
			}
			// 将返回的xml字符串解析成map，map中包含了<CSReq>标签内的元素
			Map<String, String> resultMap = ECTXmlUtil.xmlToMap(responseString);
			String result_sign = resultMap.get("sign");
			String to_verify = MerchantSignAndVerify.createLinkString(resultMap);
			// 调用CFCA方法进行验签
			if (MerchantSignAndVerify.verify(to_verify.getBytes(), result_sign.getBytes())) {
				logger.info("验签成功");
			} else {
				logger.error("验签失败");
			}

		} catch (Exception e) {
			logger.error(" 证通确认签约异常:" + e + e.getMessage());
			e.printStackTrace();

		}

		return common;
	}

	@Override
	public CommonResponse<XunlianChargeResp> charge(XunlianChargeVO xunlianChargeVO,String orderTime) {
		CommonResponse<XunlianChargeResp> common = new CommonResponse<XunlianChargeResp>();
		XunlianChargeResp resp = new XunlianChargeResp();
		try {
			String code = BINGTA_XUNLIAN_PAY;
			if(StringUtils.isNotEmpty(orderTime)){
				Date inDate = new Date(Long.valueOf(orderTime));
				Date sysDate = DateUtils.formatDate("2019-08-26 17:30:00", DateUtils.STYLE_1);
				//Date sysDate = DateUtils.formatDate("2019-08-23 19:00:00", DateUtils.STYLE_1);
				int compareTo = inDate.compareTo(sysDate);
				if(compareTo < 0){
					code =DAQING_XUNLIAN_PAY;
				}
			}
			logger.info(" xumlian 商户：" + code);
			// 入流水表
			PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
			paychannelTempFlow.setBusinessType("代付");
			paychannelTempFlow.setPayChannelCode("xunlian");
			List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
			PayConfigFileVO payConfigFileVO = list.get(0);
			String orderId = getSerialNum();
			xunlianChargeVO.setOrderId(orderId);
			paychannelTempFlow.setSeriNo(orderId);
			resp.setOrderId(orderId);
			Map<String, String> map = xunlianGetDataService.getChargeData(xunlianChargeVO, payConfigFileVO);

			// 获取请求地址
				setUrlContent(payConfigFileVO);
			// String url = "https://pay.zhengtongcf.com/pgw-pay/charge";
			String url = CHARGE;
			logger.info("xunlian 代付请求地址为" + url);
			// 拼装成xml请求报文，并发送post请求
			// 这里只是给出了一种写法，开发者可以自由编写，只要请求报文符合接口文档的规范
			String xmlString = ECTXmlUtil.mapToXml(map, ECTXmlUtil.CPREQ_QIAREQ);
			logger.info("商户身份认证接口测试请求报文：" + xmlString);
			paychannelTempFlow.setRequestText(xmlString);
			paychannelTempFlow.setRequestTime(new Date());
			String responseString = HttpClientUtil.postToServerByXml(xmlString, url);
			logger.info("证通返回报文：" + responseString);
			paychannelTempFlow.setResponseText(responseString);
			paychannelTempFlow.setResponseTime(new Date());

			// 保存流水表
			try {
				zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
			} catch (Exception e) {
				logger.error(" xunlian 代付流水信息异常" + e + e.getMessage());
			}
			// 将返回的xml字符串解析成map，map中包含了<CSReq>标签内的元素
			Map<String, String> resultMap = ECTXmlUtil.xmlToMap(responseString);

			//resp.setOrderId(resultMap.get("orderId"));
			resp.setSerialNo(resultMap.get("serialNo"));
			resp.setRespDate(resultMap.get("respDate"));
			resp.setResultCode(resultMap.get("resultCode"));
			resp.setResultMsg(resultMap.get("resultMsg"));
			resp.setRetFlag(resultMap.get("retFlag"));
			resp.setAmount(resultMap.get("amount"));
			resp.setAccount(resultMap.get("account"));
			common.setData(resp);
			common.setSuccess(true);
			// String result_sign = resultMap.get("sign");
			// String to_verify =
			// MerchantSignAndVerify.createLinkString(resultMap);
			// // 调用CFCA方法进行验签
			// if (MerchantSignAndVerify.verify(to_verify.getBytes(),
			// result_sign.getBytes())) {
			// logger.info("验签成功");
			// } else {
			// logger.error("验签失败");
			// }

		} catch (Exception e) {
			logger.error(" 证通确认签约异常:" + e + e.getMessage());
			e.printStackTrace();

		}

		return common;
	}

	@Override
	public CommonResponse<XunlianQueryChargeResp> queryCharge(XunlianQueryChargeVO xunlianQueryChargeVO,String orderTime) {
		CommonResponse<XunlianQueryChargeResp> common = new CommonResponse<XunlianQueryChargeResp>();
		XunlianQueryChargeResp resp = new XunlianQueryChargeResp();
		try {
			String code = BINGTA_XUNLIAN_PAY;
			if(StringUtils.isNotEmpty(orderTime)){
				Date inDate = new Date(Long.valueOf(orderTime));
				Date sysDate = DateUtils.formatDate("2019-08-26 17:30:00", DateUtils.STYLE_1);
				//Date sysDate = DateUtils.formatDate("2019-08-23 19:00:00", DateUtils.STYLE_1);
				int compareTo = inDate.compareTo(sysDate);
				if(compareTo < 0){
					code =DAQING_XUNLIAN_PAY;
				}
			}
			logger.info(" xumlian 商户：" + code);
			// 入流水表
			PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
			paychannelTempFlow.setBusinessType("代付查询");
			paychannelTempFlow.setPayChannelCode("xunlian");
			List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
			PayConfigFileVO payConfigFileVO = list.get(0);
			Map<String, String> map = xunlianGetDataService.getQueryChargeData(xunlianQueryChargeVO, payConfigFileVO);
			paychannelTempFlow.setSeriNo(xunlianQueryChargeVO.getOrderId());
			// 获取请求地址
			setUrlContent(payConfigFileVO);
			// String url =
			// "https://www.tongtongcf.com:9204/pgw-pay/queryorder";
			String url = QUERY_CHARGE;
			logger.info("xunlian 代付查询请求地址为" + url);

			// 拼装成xml请求报文，并发送post请求
			// 这里只是给出了一种写法，开发者可以自由编写，只要请求报文符合接口文档的规范
			String xmlString = ECTXmlUtil.mapToXml(map, ECTXmlUtil.CPREQ_QIAREQ);
			logger.info("商户身份认证接口测试请求报文：" + xmlString);
			paychannelTempFlow.setRequestText(xmlString);
			paychannelTempFlow.setRequestTime(new Date());
			String responseString = HttpClientUtil.postToServerByXml(xmlString, url);
			logger.info("证通返回报文：" + responseString);
			paychannelTempFlow.setResponseText(responseString);
			paychannelTempFlow.setResponseTime(new Date());

			// 保存流水表
			try {
				zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
			} catch (Exception e) {
				logger.error(" xunlian 保存身份认证流水信息异常" + e + e.getMessage());
			}
			// 将返回的xml字符串解析成map，map中包含了<CSReq>标签内的元素
			Map<String, String> resultMap = ECTXmlUtil.xmlToMap(responseString);

			resp.setPayOrderId(resultMap.get("payOrderId"));
			resp.setQueryResult(resultMap.get("queryResult"));
			resp.setRespDate(resultMap.get("respDate"));
			resp.setResultCode(resultMap.get("resultCode"));
			resp.setResultMsg(resultMap.get("resultMsg"));
			resp.setRetFlag(resultMap.get("retFlag"));
			resp.setAmount(resultMap.get("amount"));
			resp.setAccount(resultMap.get("account"));
			common.setData(resp);
			common.setSuccess(true);
			// String result_sign = resultMap.get("sign");
			// String to_verify =
			// MerchantSignAndVerify.createLinkString(resultMap);
			// // 调用CFCA方法进行验签
			// if (MerchantSignAndVerify.verify(to_verify.getBytes(),
			// result_sign.getBytes())) {
			// logger.info("验签成功");
			// } else {
			// logger.error("验签失败");
			// }
		} catch (Exception e) {
			logger.error(" 证通确认签约异常:" + e + e.getMessage());
			e.printStackTrace();

		}

		return common;
	}

	public void setUrlContent(PayConfigFileVO payConfigFileVO) {
		String urlList = payConfigFileVO.getPayUrl();
		JSONObject json = JSONObject.parseObject(urlList);
		IDEN_URL = String.valueOf(json.get("idenUrl"));
		SIGN_URL = String.valueOf(json.get("signUrl"));
		PAY_URL = String.valueOf(json.get("payUrl"));
		QUERY_PAY_URL = String.valueOf(json.get("QueryPayUrl"));
		CHARGE = String.valueOf(json.get("chargeUrl"));
		QUERY_CHARGE = String.valueOf(json.get("queryCharge"));
		CANCEL_URL = String.valueOf(json.get("cancelUrl"));
		
	}

	@Override
	public CommonResponse<XunlianChargeResp> chargeEnterprise(XunlianChargeEnterVO xunlianChargeVO) {
		CommonResponse<XunlianChargeResp> common = new CommonResponse<XunlianChargeResp>();
		XunlianChargeResp resp = new XunlianChargeResp();
		try {
			// 入流水表
			PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
			paychannelTempFlow.setBusinessType("代付对公");
			paychannelTempFlow.setPayChannelCode("xunlian");
			List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(BINGTA_XUNLIAN_PAY);
			PayConfigFileVO payConfigFileVO = list.get(0);
			String orderId = getSerialNum();
			xunlianChargeVO.setMerOrderId(orderId);
			paychannelTempFlow.setSeriNo(orderId);
			resp.setOrderId(orderId);
			Map<String, String> map = xunlianGetDataService.getChargeEnter(xunlianChargeVO, payConfigFileVO);

			//获得需要进行加签的字符串（通过拼接元素）
			String preSignStr = MerchantSignAndVerify.createLinkString(map);
			logger.info(preSignStr);
			//调用CFCA方法得到加签sign
			String signedString = new String(MerchantSignAndVerify.sign(preSignStr, payConfigFileVO.getMemberId(),payConfigFileVO));
			//加签sign放入map
			map.put("sign", signedString);
			//============
			// 获取请求地址
			// String url = "https://pay.zhengtongcf.com/pgw-pay/charge";
			String url = "https://pay.zhengtongcf.com/pgw-pay/enterprisepay";
			logger.info("xunlian 代付请求地址为" + url);
			// 拼装成xml请求报文，并发送post请求
			// 这里只是给出了一种写法，开发者可以自由编写，只要请求报文符合接口文档的规范
			String xmlString = ECTXmlUtil.mapToXml(map, ECTXmlUtil.CPREQ_QIAREQ);
			logger.info("商户身份认证接口测试请求报文：" + xmlString);
			paychannelTempFlow.setRequestText(xmlString);
			paychannelTempFlow.setRequestTime(new Date());
			String responseString = HttpClientUtil.postToServerByXml(xmlString, url);
			logger.info("证通返回报文：" + responseString);
			paychannelTempFlow.setResponseText(responseString);
			paychannelTempFlow.setResponseTime(new Date());

			// 保存流水表
			try {
				zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
			} catch (Exception e) {
				logger.error(" xunlian 代付流水信息异常" + e + e.getMessage());
			}
			// 将返回的xml字符串解析成map，map中包含了<CSReq>标签内的元素
			Map<String, String> resultMap = ECTXmlUtil.xmlToMap(responseString);

			//resp.setOrderId(resultMap.get("orderId"));
			resp.setSerialNo(resultMap.get("serialNo"));
			resp.setRespDate(resultMap.get("respDate"));
			resp.setResultCode(resultMap.get("resultCode"));
			resp.setResultMsg(resultMap.get("resultMsg"));
			resp.setRetFlag(resultMap.get("retFlag"));
			resp.setAmount(resultMap.get("amount"));
			resp.setAccount(resultMap.get("account"));
			common.setData(resp);
			common.setSuccess(true);
			// String result_sign = resultMap.get("sign");
			// String to_verify =
			// MerchantSignAndVerify.createLinkString(resultMap);
			// // 调用CFCA方法进行验签
			// if (MerchantSignAndVerify.verify(to_verify.getBytes(),
			// result_sign.getBytes())) {
			// logger.info("验签成功");
			// } else {
			// logger.error("验签失败");
			// }

		} catch (Exception e) {
			logger.error(" 证通确认签约异常:" + e + e.getMessage());
			e.printStackTrace();

		}

		return common;
	}
	
	public static void main(String[] args) throws ParseException {

		Date inDate = new Date(Long.valueOf("1566792015000"));
		System.out.println(inDate);
		Date sysDate = DateUtils.formatDate("2019-08-26 20:00:00", DateUtils.STYLE_1);
		//Date sysDate = DateUtils.formatDate("2019-08-23 19:00:00", DateUtils.STYLE_1);
		int compareTo = inDate.compareTo(sysDate);
		System.out.println(compareTo);	
	}

}
