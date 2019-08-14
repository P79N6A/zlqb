package com.nyd.zeus.service.impls.zzl.hnapay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.api.zzl.hnapay.HnaPayPaymentService;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.SqlHelper;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileVO;
import com.nyd.zeus.model.hnapay.ExpConstant;
import com.nyd.zeus.model.hnapay.req.HnaPayConfirmReq;
import com.nyd.zeus.model.hnapay.req.HnaPayContractReq;
import com.nyd.zeus.model.hnapay.req.HnaPayPayReq;
import com.nyd.zeus.model.hnapay.req.HnaPayPreTransReq;
import com.nyd.zeus.model.hnapay.req.HnaPayQueryPayReq;
import com.nyd.zeus.model.hnapay.req.HnaPayQueryTransReq;
import com.nyd.zeus.model.hnapay.req.HnaPayRefundReq;
import com.nyd.zeus.model.hnapay.req.HnaPayTransReq;
import com.nyd.zeus.model.hnapay.req.HnaPayUnbindReq;
import com.nyd.zeus.model.hnapay.resp.HnaPayConfirmResp;
import com.nyd.zeus.model.hnapay.resp.HnaPayContractResp;
import com.nyd.zeus.model.hnapay.resp.HnaPayPayResp;
import com.nyd.zeus.model.hnapay.resp.HnaPayPreTransResp;
import com.nyd.zeus.model.hnapay.resp.HnaPayQueryPayResp;
import com.nyd.zeus.model.hnapay.resp.HnaPayQueryTransResp;
import com.nyd.zeus.model.hnapay.resp.HnaPayRefundResp;
import com.nyd.zeus.model.hnapay.resp.HnaPayTransResp;
import com.nyd.zeus.model.payment.PaychannelTempFlow;
import com.nyd.zeus.service.impls.zzl.chanpay.PayConfigFileService;
import com.nyd.zeus.service.util.hnapay.CommonUtil;
import com.nyd.zeus.service.util.hnapay.DateUtils;
import com.nyd.zeus.service.util.hnapay.ExpUtil;

@Service(value = "hnaPayPaymentService")
public class HnaPayPaymentServiceImpl implements HnaPayPaymentService {

	private Logger logger = LoggerFactory
			.getLogger(HnaPayPaymentServiceImpl.class);

	private static String HNAPAY_IN = "200009-0001";
	private static String HNAPAY_OUT = "200009-0002";

	private static String contractUrl = "/signSms2Step.do";// 签约下单URL
	private static String confirmUrl = "/signConfirm2Step.do";// 签约确认URL
	private static String unbindUrl = "/signCancel.do";// 解约URL
	private static String preTransUrl = "/payRequest2Step.do";// 预扣款URL
	private static String transUrl = "/payConfirm2Step.do";// 扣款URL
	private static String queryUrl = "/query.do";// 查询扣款URL
	private static String refundUrl = "/refund.do";// 退款URL

	/** 新生付款接口请求地址. */
	public static final String URL_PAY = "/singlePay.do";
	/** 新生付款查询接口请求地址. */
	public static final String URL_PAY_QUERY = "/singlePayQuery.do";

	@Autowired
	private PayConfigFileService payConfigFileService;

	@Autowired
	private ZeusSqlService<?> zeusSqlService;

	private PayConfigFileVO getPayConfigFileVO(String channelCode) {
		List<PayConfigFileVO> list = payConfigFileService
				.queryByCodeId(channelCode);
		return list.get(0);
	}

	private CommonResponse<?> getResp(JSONObject json) {
		CommonResponse<?> resp = null;
		if ("0000".equals(String.valueOf(json.get(ExpConstant.RESULTCODE))))
			resp = CommonResponse.success("操作成功");
		else
			resp = CommonResponse.error(
					String.valueOf(json.get(ExpConstant.ERRORMSG)),
					String.valueOf(json.get(ExpConstant.ERRORCODE)));
		return resp;
	}

	private void saveFlow(String tranCode, Date reqTime, String req,
			String resp, String flow) {
		try {
			PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
			paychannelTempFlow.setPayChannelCode("xinsheng");
			paychannelTempFlow.setBusinessType(tranCode);
			paychannelTempFlow.setRequestTime(reqTime);
			paychannelTempFlow.setRequestText(req);
			paychannelTempFlow.setResponseTime(new Date());
			paychannelTempFlow.setResponseText(resp);
			paychannelTempFlow.setSeriNo(flow);
			zeusSqlService.insertSql(SqlHelper
					.getInsertSqlByBean(paychannelTempFlow));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static String getMerOrderId(String tranCode) {
		return tranCode + "_" + DateUtils.getCurrDate("yyyyMMddHHmmssSSS")
				+ String.valueOf((Math.random() * 1000000)).substring(0, 5);
	}

	/**
	 * 封装签约下单报文
	 * 
	 * @return
	 */
	private Map<String, String> buildContractParam(String cardNo,
			String holderName, String mobileNo, String identityCode,
			String merUserId, String merOrderId, PayConfigFileVO config) {
		String tranCode = ExpConstant.EXP10;// 交易类型(EXP10为签约下单)
		Map<String, String> param = new HashMap<String, String>();
		param.put(ExpConstant.VERSION, ExpConstant.VERSION_VALUE);// 版本号
		param.put(ExpConstant.MERID, config.getMemberId());// 商户号
		param.put(ExpConstant.TRANCODE, tranCode);// 交易类型(EXP01为签约下单)
		String now = DateUtils.getCurrDate();
		// String merOrderId = getMerOrderId(tranCode);
		param.put(ExpConstant.MERORDERID, merOrderId);// 商户订单号
		param.put(ExpConstant.SUBMITTIME, now);// 请求提交时间
		// 支付要素请根据实际使用人员添加
		param.put(ExpConstant.CARDNO, cardNo);
		param.put(ExpConstant.HOLDNAME, holderName);
		param.put(ExpConstant.CARDAVAILABLEDATE, "");
		param.put(ExpConstant.CVV2, "");
		param.put(ExpConstant.MOBILENO, mobileNo);
		param.put(ExpConstant.IDENTITYTYPE, "01");// 01代表身份证
		param.put(ExpConstant.IDENTITYCODE, identityCode);

		param.put(ExpConstant.MERUSERID, merUserId);// 商户用户id(merUserId为商户中的用户id)
		param.put(ExpConstant.MERUSERIP, ExpConstant.MERUSERIP_VALUE);// 请求ip
		param.put(ExpConstant.MSGCIPHERTEXT,
				ExpUtil.encrpt(tranCode, param, config));// 加密密文

		param.put(ExpConstant.MERATTACH, "");// 附加数据
		param.put(ExpConstant.SIGNTYPE, ExpConstant.SIGNTYPE_VALUE);// 签名类型
		param.put(ExpConstant.CHARSET, ExpConstant.CHARSET_VALUE);// 编码类型
		try {
			param.put(ExpConstant.SIGNVALUE,
					ExpUtil.sign(tranCode, param, config));// 签名
		} catch (Exception e) {
			e.printStackTrace();
			param.put(ExpConstant.SIGNVALUE, "");
		}
		return param;
	}

	@Override
	public CommonResponse<HnaPayContractResp> contract(
			HnaPayContractReq hnaPayContractReq) {
		PayConfigFileVO config = getPayConfigFileVO(HNAPAY_IN);
		Map<String, String> param = buildContractParam(
				hnaPayContractReq.getCardNo(),
				hnaPayContractReq.getHolderName(),
				hnaPayContractReq.getMobileNo(),
				hnaPayContractReq.getIdentityCode(),
				hnaPayContractReq.getMerUserId(),
				hnaPayContractReq.getMerOrderId(), config);// 签约下单参数
		String response = "";
		Date reqTime = new Date();
		try {
			response = ExpUtil.submit(param.get(ExpConstant.TRANCODE),
					contractUrl, param, config);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// Map<String, Object> map = JsonUtil.jsonToMap(response);// 解析返回的信息
		// boolean flag =
		// ExpUtil.verify(map.get(ExpConstant.TRANCODE).toString(), map,
		// config);
		// if (!flag) {
		// throw new Exception("签约确认验证码返回结果验签失败");
		// }
		saveFlow(param.get(ExpConstant.TRANCODE), reqTime,
				JSONObject.toJSONString(param), response,
				hnaPayContractReq.getMerOrderId());
		JSONObject json = JSONObject.parseObject(response);
		@SuppressWarnings("unchecked")
		CommonResponse<HnaPayContractResp> resp = (CommonResponse<HnaPayContractResp>) getResp(json);
		HnaPayContractResp rs = new HnaPayContractResp();
		rs.setHnapayOrderId(String.valueOf(json.get(ExpConstant.HNAPAYORDERID)));
		rs.setMerOrderId(String.valueOf(json.get(ExpConstant.MERORDERID)));
		resp.setData(rs);
		return resp;
	}

	/**
	 * 封装签约确认发送报文
	 * 
	 * @param smsRespose
	 * @return
	 */
	private Map<String, String> buildConfirm(String merOrderId,
			String hnapayOrderId, String smsCode, PayConfigFileVO config) {
		String tranCode = ExpConstant.EXP11;// 交易类型(EXP11为签约确认)
		Map<String, String> confirmParam = new HashMap<String, String>();
		confirmParam.put(ExpConstant.VERSION, ExpConstant.VERSION_VALUE);// 版本号
		confirmParam.put(ExpConstant.TRANCODE, tranCode);// 交易类型(EXP03为签约确认)
		confirmParam.put(ExpConstant.MERID, config.getMemberId());// 商户号
		confirmParam.put(ExpConstant.MERORDERID, merOrderId);// 商户订单号，第一步签约下单时的订单号
		confirmParam.put(ExpConstant.HNAPAYORDERID, hnapayOrderId);// 新生返回的新生签约订单号
		confirmParam.put(ExpConstant.SUBMITTIME, DateUtils.getCurrDate());
		confirmParam.put(ExpConstant.MERUSERIP, ExpConstant.MERUSERIP_VALUE);// 请求ip
		confirmParam.put(ExpConstant.SMSCODE, smsCode);// 签约短信验证码
		confirmParam.put(ExpConstant.MSGCIPHERTEXT,
				ExpUtil.encrpt(tranCode, confirmParam, config));// 加密密文
		confirmParam.put(ExpConstant.SIGNTYPE, ExpConstant.SIGNTYPE_VALUE);// 签名类型
		confirmParam.put(ExpConstant.CHARSET, ExpConstant.CHARSET_VALUE);// 编码类型
		try {
			confirmParam.put(ExpConstant.SIGNVALUE,
					ExpUtil.sign(tranCode, confirmParam, config));// 签名值
		} catch (Exception e) {
			e.printStackTrace();
			confirmParam.put(ExpConstant.SIGNVALUE, "");
		}
		return confirmParam;
	}

	@Override
	public CommonResponse<HnaPayConfirmResp> confirm(
			HnaPayConfirmReq hnaPayConfirmReq) {
		PayConfigFileVO config = getPayConfigFileVO(HNAPAY_IN);
		Map<String, String> param = buildConfirm(
				hnaPayConfirmReq.getMerOrderId(),
				hnaPayConfirmReq.getHnapayOrderId(),
				hnaPayConfirmReq.getSmsCode(), config);// 签约确认封装参数
		String response = "";
		Date reqTime = new Date();
		try {
			response = ExpUtil.submit(param.get(ExpConstant.TRANCODE),
					confirmUrl, param, config);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		saveFlow(param.get(ExpConstant.TRANCODE), reqTime,
				JSONObject.toJSONString(param), response,
				hnaPayConfirmReq.getMerOrderId());
		JSONObject json = JSONObject.parseObject(response);
		@SuppressWarnings("unchecked")
		CommonResponse<HnaPayConfirmResp> resp = (CommonResponse<HnaPayConfirmResp>) getResp(json);
		HnaPayConfirmResp rs = new HnaPayConfirmResp();
		rs.setBizProtocolNo(String.valueOf(json.get(ExpConstant.BIZPROTOCOLNO)));
		rs.setPayProtocolNo(String.valueOf(json.get(ExpConstant.PAYPROTOCOLNO)));
		rs.setBankCode(String.valueOf(json.get(ExpConstant.BANKCODE)));
		resp.setData(rs);
		return resp;
	}

	/**
	 * @Description: 商户侧调用新生快捷支付 解约接口EXP04报文组装 (处理步骤：1、按接口要求格式给参数赋值; 2、关键信息加密;
	 *               3、签名) @return Map<String,String> 返回类型 @throws
	 */
	private Map<String, String> genExp04Data(String bizProtocolNo,
			String payProtocolNo, PayConfigFileVO config) {
		Map<String, String> params = new HashMap<String, String>();
		String tranCode = ExpConstant.EXP04; // 交易码
		String now = DateUtils.getCurrDate();
		String submitTime = now; // 请求提交时间(格式：YYYYMMDDHHMMSS)
		String merOrderId = getMerOrderId(tranCode);

		// 报文密文参数
		params.put(ExpConstant.BIZPROTOCOLNO, bizProtocolNo); // 用户业务协议号
		params.put(ExpConstant.PAYPROTOCOLNO, payProtocolNo); // 支付协议号
		params.put(ExpConstant.MERUSERIP, ExpConstant.MERUSERIP_VALUE); // 商户用户IP

		// 报文明文
		params.put(ExpConstant.VERSION, ExpConstant.VERSION_VALUE);
		params.put(ExpConstant.TRANCODE, tranCode);
		params.put(ExpConstant.MERID, config.getMemberId());
		params.put(ExpConstant.MERORDERID, merOrderId);
		params.put(ExpConstant.SUBMITTIME, submitTime);
		// 报文密文加密(需要加密的字段参见接口说明)
		params.put(ExpConstant.MSGCIPHERTEXT,
				ExpUtil.encrpt(tranCode, params, config));
		params.put(ExpConstant.SIGNTYPE, ExpConstant.SIGNTYPE_VALUE);
		params.put(ExpConstant.MERATTACH, "");
		params.put(ExpConstant.CHARSET, ExpConstant.CHARSET_VALUE);
		try {
			// 签名(要签名的字段参见接口文档)
			params.put(ExpConstant.SIGNVALUE,
					ExpUtil.sign(tranCode, params, config));
		} catch (Exception e) {
			e.printStackTrace();
			params.put(ExpConstant.SIGNVALUE, "");
		}
		return params;
	}

	@Override
	public CommonResponse<?> unbind(HnaPayUnbindReq hnaPayUnbindReq) {
		PayConfigFileVO config = getPayConfigFileVO(HNAPAY_IN);
		Map<String, String> param = genExp04Data(
				hnaPayUnbindReq.getBizProtocolNo(),
				hnaPayUnbindReq.getPayProtocolNo(), config);

		String response = "";
		Date reqTime = new Date();
		try {
			response = ExpUtil.submit(param.get(ExpConstant.TRANCODE),
					unbindUrl, param, config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Map<String, Object> map = JsonUtil.jsonToMap(response);// 解析返回的信息
		// boolean flag =
		// ExpUtil.verify(map.get(ExpConstant.TRANCODE).toString(), map,
		// config);
		// if (!flag) {
		// throw new Exception("解约返回结果验签失败");
		// }
		saveFlow(param.get(ExpConstant.TRANCODE), reqTime,
				JSONObject.toJSONString(param), response,
				param.get(ExpConstant.MERORDERID));
		JSONObject json = JSONObject.parseObject(response);
		return getResp(json);
	}

	/**
	 * 生成支付下单请求报文
	 */
	private Map<String, String> genExp12Data(String amount,
			String bizProtocolNo, String payProtocolNo, String notifyUrl,
			String merUserId, String merOrderId, PayConfigFileVO config) {
		Map<String, String> params = new HashMap<String, String>();
		String tranCode = ExpConstant.EXP12; // 交易码
		String now = DateUtils.getCurrDate();
		String submitTime = now;

		// 报文密文参数
		params.put(ExpConstant.TRANAMT, amount);// 支付金额，最小2.01
		params.put(ExpConstant.PAYTYPE, "3");// 2:银行卡卡号/3:用户业务协议号+支付协议号
		// 2时必输
		params.put(ExpConstant.CARDTYPE, "");// 借记卡
		params.put(ExpConstant.BANKCODE, "");// 支付银行简码
		params.put(ExpConstant.CARDNO, "");// 支付银行卡卡号
		params.put(ExpConstant.HOLDNAME, "");// 持卡人姓名
		params.put(ExpConstant.CARDAVAILABLEDATE, "");// 信用卡有效期
		params.put(ExpConstant.CVV2, "");// 信用卡CVV2
		params.put(ExpConstant.MOBILENO, "");// 银行签约手机号
		params.put(ExpConstant.IDENTITYTYPE, "");// 证件类型，暂仅支持01:身份证
		params.put(ExpConstant.IDENTITYCODE, "");// 证件号码
		params.put(ExpConstant.BIZPROTOCOLNO, bizProtocolNo);// 用户业务协议号
		params.put(ExpConstant.PAYPROTOCOLNO, payProtocolNo);// 支付协议号
		params.put(ExpConstant.FRONTURL, "");// 商户前台跳转地址
		params.put(ExpConstant.NOTIFYURL, notifyUrl);// 商户异步通知地址
		params.put(ExpConstant.ORDEREXPIRETIME, "");// 订单过期时长
		params.put(ExpConstant.RISKEXPAND, "");// 风控扩展信息
		params.put(ExpConstant.GOODSINFO, "");// 商品信息
		params.put(ExpConstant.MERUSERID, merUserId);// 商户用户ID
		params.put(ExpConstant.MERUSERIP, ExpConstant.MERUSERIP_VALUE);// 商户用户IP
		params.put(ExpConstant.SUBMERCHANTID, "1908051747462736373");// 商户渠道进件ID

		// 报文明文参数
		params.put(ExpConstant.VERSION, ExpConstant.VERSION_VALUE);// 版本号
		params.put(ExpConstant.TRANCODE, tranCode);// 交易代码
		params.put(ExpConstant.MERID, config.getMemberId());// 商户ID
		params.put(ExpConstant.MERORDERID, merOrderId);// 商户订单号
		params.put(ExpConstant.SUBMITTIME, submitTime);// 请求提交时间
		params.put(ExpConstant.MSGCIPHERTEXT,
				ExpUtil.encrpt(tranCode, params, config));// 报文密文
		params.put(ExpConstant.SIGNTYPE, ExpConstant.SIGNTYPE_VALUE);// 签名类型:RSA
		params.put(ExpConstant.MERATTACH, "");// 附加数据
		params.put(ExpConstant.CHARSET, ExpConstant.CHARSET_VALUE);// 编码方式:UTF-8
		try {
			params.put(ExpConstant.SIGNVALUE,
					ExpUtil.sign(tranCode, params, config));// 签名密文串
		} catch (Exception e) {
			e.printStackTrace();
			params.put(ExpConstant.SIGNVALUE, "");
		}

		return params;
	}

	@Override
	public CommonResponse<HnaPayPreTransResp> preTrans(
			HnaPayPreTransReq hnaPayPreTransReq) {
		PayConfigFileVO config = getPayConfigFileVO(HNAPAY_IN);
		Map<String, String> param = genExp12Data(hnaPayPreTransReq.getAmount(),
				hnaPayPreTransReq.getBizProtocolNo(),
				hnaPayPreTransReq.getPayProtocolNo(),
				hnaPayPreTransReq.getNotifyUrl(),
				hnaPayPreTransReq.getMerUserId(),
				hnaPayPreTransReq.getMerOrderId(), config);

		String response = "";
		Date reqTime = new Date();
		try {
			response = ExpUtil.submit(param.get(ExpConstant.TRANCODE),
					preTransUrl, param, config);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 验签
		// Map<String, Object> retMap12= JsonUtil.jsonToMap(response);
		// boolean verify = ExpUtil.verify(param.get(ExpConstant.TRANCODE),
		// retMap12);
		// if(!verify) {
		// throw new Exception("调用支付请求下单返回结果验签失败");
		// }
		saveFlow(param.get(ExpConstant.TRANCODE), reqTime,
				JSONObject.toJSONString(param), response,
				hnaPayPreTransReq.getMerOrderId());
		JSONObject json = JSONObject.parseObject(response);
		@SuppressWarnings("unchecked")
		CommonResponse<HnaPayPreTransResp> resp = (CommonResponse<HnaPayPreTransResp>) getResp(json);
		HnaPayPreTransResp rs = new HnaPayPreTransResp();
		rs.setMerOrderId(String.valueOf(param.get(ExpConstant.MERORDERID)));
		rs.setHnapayOrderId(String.valueOf(json.get(ExpConstant.HNAPAYORDERID)));
		rs.setSubmitTime(param.get(ExpConstant.SUBMITTIME));
		resp.setData(rs);
		return resp;
	}

	/**
	 * 生成支付确认请求报文
	 */
	private Map<String, String> genExp13Data(String merOrderId,
			String hanpayOrderId, String smsCode, PayConfigFileVO config) {
		Map<String, String> params = new HashMap<String, String>();
		String tranCode = ExpConstant.EXP13;
		// 报文密文参数
		params.put(ExpConstant.HNAPAYORDERID, hanpayOrderId);// 新生订单号
		params.put(ExpConstant.SMSCODE, smsCode);// 短信验证码
		params.put(ExpConstant.MERUSERIP, ExpConstant.MERUSERIP_VALUE);// 商户用户IP
		params.put(ExpConstant.PAYMENT_TERMINAL_INFO, "01|10001");// 付款方终端信息
		params.put(ExpConstant.RECEIVER_TERMINAL_INFO, "01|00001|CN|110000");// 收款方终端信息
		params.put(
				ExpConstant.DEVICE_INFO,
				"127.0.0.1|0123456789AB|012345678912345|543219876543210|01234567890123456789|BA9876543210|006");// 交易设备信息

		String now = DateUtils.getCurrDate();
		// 报文明文参数
		params.put(ExpConstant.VERSION, ExpConstant.VERSION_VALUE);// 版本号
		params.put(ExpConstant.TRANCODE, tranCode);// 交易代码
		params.put(ExpConstant.MERID, config.getMemberId());// 商户ID
		params.put(ExpConstant.MERORDERID, merOrderId);// 商户订单号
		params.put(ExpConstant.SUBMITTIME, now);// 请求提交时间
		params.put(ExpConstant.MSGCIPHERTEXT,
				ExpUtil.encrpt(tranCode, params, config));// 报文密文
		params.put(ExpConstant.SIGNTYPE, ExpConstant.SIGNTYPE_VALUE);// 签名类型:RSA
		params.put(ExpConstant.MERATTACH, "");// 附加数据
		params.put(ExpConstant.CHARSET, ExpConstant.CHARSET_VALUE);// 编码方式:UTF-8
		try {
			params.put(ExpConstant.SIGNVALUE,
					ExpUtil.sign(tranCode, params, config));
		} catch (Exception e) {
			e.printStackTrace();
			params.put(ExpConstant.SIGNVALUE, "");
		}

		return params;
	}

	@Override
	public CommonResponse<HnaPayTransResp> trans(HnaPayTransReq hnaPayTransReq) {
		PayConfigFileVO config = getPayConfigFileVO(HNAPAY_IN);
		Map<String, String> param = genExp13Data(
				hnaPayTransReq.getMerOrderId(),
				hnaPayTransReq.getHanpayOrderId(), hnaPayTransReq.getSmsCode(),
				config);

		String response = "";
		Date reqTime = new Date();
		try {
			response = ExpUtil.submit(param.get(ExpConstant.TRANCODE),
					transUrl, param, config);
		} catch (Exception e) {
			e.printStackTrace();
		}

		saveFlow(param.get(ExpConstant.TRANCODE), reqTime,
				JSONObject.toJSONString(param), response,
				hnaPayTransReq.getMerOrderId());
		JSONObject json = JSONObject.parseObject(response);
		CommonResponse<HnaPayTransResp> resp = new CommonResponse<>();
		if ("0000".equals(String.valueOf(json.get(ExpConstant.RESULTCODE)))
				|| "9999".equals(String.valueOf(json
						.get(ExpConstant.RESULTCODE))))
			resp.setSuccess(true);
		else {
			resp.setSuccess(false);
			resp.setCode(String.valueOf(json.get(ExpConstant.ERRORCODE)));
			resp.setMsg(String.valueOf(json.get(ExpConstant.ERRORMSG)));
		}
		HnaPayTransResp rs = new HnaPayTransResp();
		rs.setResultCode(String.valueOf(json.get(ExpConstant.RESULTCODE)));
		rs.setErrorCode(String.valueOf(json.get(ExpConstant.ERRORCODE)));
		rs.setErrorMsg(String.valueOf(json.get(ExpConstant.ERRORMSG)));
		rs.setMerOrderId(hnaPayTransReq.getMerOrderId());
		rs.setHnapayOrderId(String.valueOf(json.get(ExpConstant.HNAPAYORDERID)));
		rs.setBizProtocolNo(String.valueOf(json.get(ExpConstant.BIZPROTOCOLNO)));
		rs.setPayProtocolNo(String.valueOf(json.get(ExpConstant.PAYPROTOCOLNO)));
		rs.setCheckDate(String.valueOf(json.get(ExpConstant.CHECKDATE)));
		rs.setSubmitTime(param.get(ExpConstant.SUBMITTIME));
		resp.setData(rs);
		return resp;

	}

	@Override
	public CommonResponse<HnaPayTransResp> transMerge(
			HnaPayPreTransReq hnaPayPreTransReq) {
		CommonResponse<HnaPayPreTransResp> preTransResp = preTrans(hnaPayPreTransReq);
		if (preTransResp.isSuccess()) {
			HnaPayTransReq hnaPayTransReq = new HnaPayTransReq();
			hnaPayTransReq.setHanpayOrderId(preTransResp.getData()
					.getHnapayOrderId());
			hnaPayTransReq
					.setMerOrderId(preTransResp.getData().getMerOrderId());
			// 整合接口把预下单的接口时间给出来
			CommonResponse<HnaPayTransResp> resp = trans(hnaPayTransReq);
			HnaPayTransResp hnaPayTransResp = resp.getData();
			hnaPayTransResp.setSubmitTime(preTransResp.getData()
					.getSubmitTime());
			resp.setData(hnaPayTransResp);
			return resp;
		} else {
			CommonResponse<HnaPayTransResp> resp = new CommonResponse<>();
			resp.setSuccess(false);
			resp.setCode(preTransResp.getCode());
			resp.setMsg(preTransResp.getMsg());
			return resp;
		}
	}

	/**
	 * @Description: 商户侧调用新生快捷支付 接口EXP08报文组装
	 * @return Map<String,String> 返回类型
	 * @throws
	 */
	private Map<String, String> genExp08Data(String merOrderId,
			String submitTime, PayConfigFileVO config) {
		Map<String, String> params = new HashMap<String, String>();
		String tranCode = ExpConstant.EXP08; // 交易码

		params.put(ExpConstant.VERSION, ExpConstant.VERSION_VALUE);
		params.put(ExpConstant.TRANCODE, tranCode);
		params.put(ExpConstant.MERID, config.getMemberId());
		params.put(ExpConstant.MERORDERID, merOrderId);
		params.put(ExpConstant.SUBMITTIME, submitTime);
		params.put(ExpConstant.SIGNTYPE, ExpConstant.SIGNTYPE_VALUE);
		params.put(ExpConstant.MERATTACH, "");
		params.put(ExpConstant.CHARSET, ExpConstant.CHARSET_VALUE);
		try {
			// 签名(要签名的字段参考新生快捷2.0网关接口文档)
			params.put(ExpConstant.SIGNVALUE,
					ExpUtil.sign(tranCode, params, config));
		} catch (Exception e) {
			e.printStackTrace();
			params.put(ExpConstant.SIGNVALUE, "");
		}
		return params;
	}

	@Override
	public CommonResponse<HnaPayQueryTransResp> queryTrans(
			HnaPayQueryTransReq hnaPayQueryTransReq) {
		PayConfigFileVO config = getPayConfigFileVO(HNAPAY_IN);
		Map<String, String> param = genExp08Data(
				hnaPayQueryTransReq.getMerOrderId(),
				hnaPayQueryTransReq.getSubmitTime(), config);

		String response = "";
		Date reqTime = new Date();
		try {
			response = ExpUtil.submit(param.get(ExpConstant.TRANCODE),
					queryUrl, param, config);
		} catch (Exception e) {
			e.printStackTrace();
		}

		saveFlow(param.get(ExpConstant.TRANCODE), reqTime,
				JSONObject.toJSONString(param), response,
				hnaPayQueryTransReq.getMerOrderId());
		JSONObject json = JSONObject.parseObject(response);
		@SuppressWarnings("unchecked")
		CommonResponse<HnaPayQueryTransResp> resp = (CommonResponse<HnaPayQueryTransResp>) getResp(json);
		HnaPayQueryTransResp rs = new HnaPayQueryTransResp();
		rs.setResultCode(String.valueOf(json.get(ExpConstant.RESULTCODE)));
		rs.setErrorCode(String.valueOf(json.get(ExpConstant.ERRORCODE)));
		rs.setErrorMsg(String.valueOf(json.get(ExpConstant.ERRORMSG)));
		rs.setHnapayOrderId(String.valueOf(json.get(ExpConstant.HNAPAYORDERID)));
		rs.setTranAmt(String.valueOf(json.get(ExpConstant.TRANAMT)));
		rs.setRefundAmt(String.valueOf(json.get(ExpConstant.REFUNDAMT)));
		rs.setOrderStatus(String.valueOf(json.get(ExpConstant.ORDERSTATUS)));
		rs.setOrderFailedCode(String.valueOf(json
				.get(ExpConstant.ORDERFAILEDCODE)));
		rs.setOrderFailedMsg(String.valueOf(json
				.get(ExpConstant.ORDERFAILEDMSG)));
		resp.setData(rs);
		return resp;
	}

	/**
	 * @Description: 商户侧调用新生快捷支付 单退款接口EXP09报文组装
	 * @return Map<String,String> 返回类型
	 * @throws
	 */
	private Map<String, String> genExp09Data(String merOrderId,
			String orgMerOrderId, String orgSubmitTime, String orderAmt,
			String refundOrderAmt, String notifyUrl, PayConfigFileVO config) {
		Map<String, String> params = new HashMap<String, String>();
		String tranCode = ExpConstant.EXP09; // 交易码
		String now = DateUtils.getCurrDate();
		String submitTime = now; // 请求提交时间(格式：YYYYMMDDHHMMSS)

		// 报文密文部分
		params.put(ExpConstant.ORDERAMT, orderAmt);
		params.put(ExpConstant.ORGMERORDERID, orgMerOrderId);
		params.put(ExpConstant.ORGSUBMITTIME, orgSubmitTime);
		params.put(ExpConstant.REFUNDORDERAMT, refundOrderAmt);
		params.put(ExpConstant.NOTIFYURL, notifyUrl); // (此处为商户接收异步通知地址)

		// 报文明文部分
		params.put(ExpConstant.VERSION, ExpConstant.VERSION_VALUE);
		params.put(ExpConstant.TRANCODE, tranCode);
		params.put(ExpConstant.MERID, config.getMemberId());
		params.put(ExpConstant.MERORDERID, merOrderId);
		params.put(ExpConstant.SUBMITTIME, submitTime);
		// 报文密文加密(需要加密的字段参见接口说明)
		params.put(ExpConstant.MSGCIPHERTEXT,
				ExpUtil.encrpt(tranCode, params, config));
		params.put(ExpConstant.SIGNTYPE, ExpConstant.SIGNTYPE_VALUE);
		params.put(ExpConstant.MERATTACH, "");
		params.put(ExpConstant.CHARSET, ExpConstant.CHARSET_VALUE);
		try {
			// 签名(要签名的字段参见网关接口文档)
			params.put(ExpConstant.SIGNVALUE,
					ExpUtil.sign(tranCode, params, config));
		} catch (Exception e) {
			e.printStackTrace();
			params.put(ExpConstant.SIGNVALUE, "");
		}

		return params;
	}

	@Override
	public CommonResponse<HnaPayRefundResp> refund(
			HnaPayRefundReq hnaPayRefundReq) {
		PayConfigFileVO config = getPayConfigFileVO(HNAPAY_IN);
		Map<String, String> param = genExp09Data(
				hnaPayRefundReq.getMerOrderId(),
				hnaPayRefundReq.getOrgMerOrderId(),
				hnaPayRefundReq.getOrgSubmitTime(),
				hnaPayRefundReq.getOrderAmt(),
				hnaPayRefundReq.getRefundOrderAmt(),
				hnaPayRefundReq.getNotifyUrl(), config);

		String response = "";
		Date reqTime = new Date();
		try {
			response = ExpUtil.submit(param.get(ExpConstant.TRANCODE),
					refundUrl, param, config);
		} catch (Exception e) {
			e.printStackTrace();
		}

		saveFlow(param.get(ExpConstant.TRANCODE), reqTime,
				JSONObject.toJSONString(param), response,
				hnaPayRefundReq.getMerOrderId());
		JSONObject json = JSONObject.parseObject(response);
		@SuppressWarnings("unchecked")
		CommonResponse<HnaPayRefundResp> resp = (CommonResponse<HnaPayRefundResp>) getResp(json);
		HnaPayRefundResp rs = new HnaPayRefundResp();
		rs.setResultCode(String.valueOf(json.get(ExpConstant.RESULTCODE)));
		rs.setErrorCode(String.valueOf(json.get(ExpConstant.ERRORCODE)));
		rs.setErrorMsg(String.valueOf(json.get(ExpConstant.ERRORMSG)));
		rs.setHnapayOrderId(String.valueOf(json.get(ExpConstant.HNAPAYORDERID)));
		rs.setOrgMerOrderId(String.valueOf(json.get(ExpConstant.ORGMERORDERID)));
		rs.setTranAmt(String.valueOf(json.get(ExpConstant.TRANAMT)));
		rs.setRefundAmt(String.valueOf(json.get(ExpConstant.REFUNDAMT)));
		rs.setOrderStatus(String.valueOf(json.get(ExpConstant.ORDERSTATUS)));
		resp.setData(rs);
		return resp;
	}

	/**
	 * 构建请求报文
	 */
	private Map<String, String> buildPayReqParams(String merOrderId,
			String tranAmt, String payeeName, String payeeAccount,
			String notifyUrl, PayConfigFileVO config) {
		Map<String, String> params = new HashMap<String, String>();
		// 版本号
		params.put(ExpConstant.VERSION, ExpConstant.VERSION_VALUE);
		String tranCode = ExpConstant.SGP01; // 交易码
		// 交易代码
		params.put(ExpConstant.TRANCODE, tranCode);
		// 商户ID
		params.put(ExpConstant.MERID, config.getMemberId());
		// 商户订单号
		params.put(ExpConstant.MERORDERID, merOrderId);
		// 请求提交时间
		String now = DateUtils.getCurrDate();
		params.put(ExpConstant.SUBMITTIME, now);
		params.put(ExpConstant.SIGNTYPE, ExpConstant.SIGNTYPE_VALUE);
		params.put(ExpConstant.MERATTACH, "");
		params.put(ExpConstant.CHARSET, ExpConstant.CHARSET_VALUE);
		// 报文密文
		params.put(
				"msgCiphertext",
				buildPayCipherParam(tranAmt, payeeName, payeeAccount,
						notifyUrl, config));
		// 签名数据
		params.put("signValue", buildPaySignParam(params, config));
		return params;
	}

	/**
	 * 构建报文密文参数
	 */
	private String buildPayCipherParam(String tranAmt, String payeeName,
			String payeeAccount, String notifyUrl, PayConfigFileVO config) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("tranAmt", tranAmt);// 支付金额
		jsonMap.put("payType", "1");// 付款类型 1：付款到银行 2：付款到账户
		jsonMap.put("auditFlag", "0"); // 是否需要复核 0：不需要 1：需要
		jsonMap.put("payeeName", payeeName);// 收款方姓名
		jsonMap.put("payeeAccount", payeeAccount);// 收款方账户
		jsonMap.put("note", "");
		jsonMap.put("remark", "");
		jsonMap.put("bankName", "");
		jsonMap.put("province", "");
		jsonMap.put("city", "");
		jsonMap.put("branch", "");
		jsonMap.put("payeeType", "1");// 收款方类型 1：个人 2：企业
		jsonMap.put("notifyUrl", notifyUrl);
		jsonMap.put("paymentTerminalInfo", "01|10001");
		jsonMap.put(
				"deviceInfo",
				"127.0.0.1|0123456789AB|012345678912345|543219876543210|01234567890123456789|BA9876543210|006");
		// 转换为JSON字符串
		String jsonStr = JSONObject.toJSONString(jsonMap);
		try {
			// 执行加密操作
			return CommonUtil.doEncryt(jsonStr, config);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 构建签名参数
	 */
	private String buildPaySignParam(Map<String, String> params,
			PayConfigFileVO config) {
		String signFormatStr = "version=[%s]tranCode=[%s]merId=[%s]merOrderId=[%s]submitTime=[%s]msgCiphertext=[%s]signType=[%s]";
		String version = params.get("version");
		String tranCode = params.get("tranCode");
		String merId = params.get("merId");
		String merOrderId = params.get("merOrderId");
		String submitTime = params.get("submitTime");
		String msgCiphertext = params.get("msgCiphertext");
		String signType = params.get("signType");
		// 签名明文串
		String signPlainStr = String.format(signFormatStr, version, tranCode,
				merId, merOrderId, submitTime, msgCiphertext, signType);
		try {
			// 执行签名操作
			return CommonUtil.doSign(signPlainStr, config);
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public CommonResponse<HnaPayPayResp> pay(HnaPayPayReq hnaPayPayReq) {
		PayConfigFileVO config = getPayConfigFileVO(HNAPAY_OUT);
		Map<String, String> param = buildPayReqParams(
				hnaPayPayReq.getMerOrderId(), hnaPayPayReq.getTranAmt(),
				hnaPayPayReq.getPayeeName(), hnaPayPayReq.getPayeeAccount(),
				hnaPayPayReq.getNotifyUrl(), config);

		String response = "";
		Date reqTime = new Date();
		try {
			response = CommonUtil.doSubmit(URL_PAY, param, config);
		} catch (Exception e) {
			e.printStackTrace();
		}

		saveFlow(param.get(ExpConstant.TRANCODE), reqTime,
				JSONObject.toJSONString(param), response,
				hnaPayPayReq.getMerOrderId());
		JSONObject json = JSONObject.parseObject(response);
		CommonResponse<HnaPayPayResp> resp = new CommonResponse<>();
		if ("0000".equals(String.valueOf(json.get(ExpConstant.RESULTCODE)))
				|| "9999".equals(String.valueOf(json
						.get(ExpConstant.RESULTCODE))))
			resp.setSuccess(true);
		else {
			resp.setSuccess(false);
			resp.setCode(String.valueOf(json.get(ExpConstant.ERRORCODE)));
			resp.setMsg(String.valueOf(json.get(ExpConstant.ERRORMSG)));
		}
		HnaPayPayResp rs = new HnaPayPayResp();
		rs.setResultCode(String.valueOf(json.get(ExpConstant.RESULTCODE)));
		rs.setErrorCode(String.valueOf(json.get(ExpConstant.ERRORCODE)));
		rs.setErrorMsg(String.valueOf(json.get(ExpConstant.ERRORMSG)));
		rs.setMerOrderId(String.valueOf(param.get(ExpConstant.MERORDERID)));
		rs.setHnapayOrderId(String.valueOf(json.get(ExpConstant.HNAPAYORDERID)));
		rs.setSubmitTime(param.get(ExpConstant.SUBMITTIME));
		resp.setData(rs);
		return resp;
	}

	/**
	 * 构建请求报文
	 */
	private static Map<String, String> buildReqParams(String merOrderId,
			String submitTime, PayConfigFileVO config) {
		Map<String, String> params = new HashMap<String, String>();
		// 版本号
		params.put(ExpConstant.VERSION, ExpConstant.VERSION_VALUE);
		String tranCode = ExpConstant.SGP02; // 交易码
		// 交易代码
		params.put(ExpConstant.TRANCODE, tranCode);
		// 商户ID
		params.put(ExpConstant.MERID, config.getMemberId());
		// 商户订单号
		params.put(ExpConstant.MERORDERID, merOrderId);
		// 请求提交时间
		params.put(ExpConstant.SUBMITTIME, submitTime);
		params.put(ExpConstant.SIGNTYPE, ExpConstant.SIGNTYPE_VALUE);
		params.put(ExpConstant.MERATTACH, "");
		params.put(ExpConstant.CHARSET, ExpConstant.CHARSET_VALUE);
		params.put("msgCiphertext", "");
		// 签名数据
		params.put("signValue", buildSignParam(params, config));
		return params;
	}

	/**
	 * 构建签名参数
	 */
	private static String buildSignParam(Map<String, String> params,
			PayConfigFileVO config) {
		String signFormatStr = "version=[%s]tranCode=[%s]merId=[%s]merOrderId=[%s]submitTime=[%s]";
		String version = params.get("version");
		String tranCode = params.get("tranCode");
		String merId = params.get("merId");
		String merOrderId = params.get("merOrderId");
		String submitTime = params.get("submitTime");
		// 签名明文串
		String signPlainStr = String.format(signFormatStr, version, tranCode,
				merId, merOrderId, submitTime);
		try {
			// 执行签名操作
			return CommonUtil.doSign(signPlainStr, config);
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public CommonResponse<HnaPayQueryPayResp> queryPay(
			HnaPayQueryPayReq hnaPayQueryPayReq) {
		PayConfigFileVO config = getPayConfigFileVO(HNAPAY_OUT);
		Map<String, String> param = buildReqParams(
				hnaPayQueryPayReq.getMerOrderId(),
				hnaPayQueryPayReq.getSubmitTime(), config);

		String response = "";
		Date reqTime = new Date();
		try {
			response = CommonUtil.doSubmit(URL_PAY_QUERY, param, config);
		} catch (Exception e) {
			e.printStackTrace();
		}

		saveFlow(param.get(ExpConstant.TRANCODE), reqTime,
				JSONObject.toJSONString(param), response,
				hnaPayQueryPayReq.getMerOrderId());
		JSONObject json = JSONObject.parseObject(response);
		CommonResponse<HnaPayQueryPayResp> resp = new CommonResponse<>();
		if ("0000".equals(String.valueOf(json.get(ExpConstant.RESULTCODE)))
				|| "9999".equals(String.valueOf(json
						.get(ExpConstant.RESULTCODE))))
			resp.setSuccess(true);
		else {
			resp.setSuccess(false);
			resp.setCode(String.valueOf(json.get(ExpConstant.ERRORCODE)));
			resp.setMsg(String.valueOf(json.get(ExpConstant.ERRORMSG)));
		}
		HnaPayQueryPayResp rs = new HnaPayQueryPayResp();
		rs.setResultCode(String.valueOf(json.get(ExpConstant.RESULTCODE)));
		rs.setErrorCode(String.valueOf(json.get(ExpConstant.ERRORCODE)));
		rs.setErrorMsg(String.valueOf(json.get(ExpConstant.ERRORMSG)));
		rs.setHnapayOrderId(String.valueOf(json.get(ExpConstant.HNAPAYORDERID)));
		rs.setTranAmt(String.valueOf(json.get(ExpConstant.TRANAMT)));
		rs.setOrderStatus(String.valueOf(json.get(ExpConstant.ORDERSTATUS)));
		rs.setOrderFailedCode(String.valueOf(json
				.get(ExpConstant.ORDERFAILEDCODE)));
		rs.setOrderFailedMsg(String.valueOf(json
				.get(ExpConstant.ORDERFAILEDMSG)));
		rs.setSuccessTime(String.valueOf(json.get(ExpConstant.SUCCESSTIME)));
		resp.setData(rs);
		return resp;
	}

}
