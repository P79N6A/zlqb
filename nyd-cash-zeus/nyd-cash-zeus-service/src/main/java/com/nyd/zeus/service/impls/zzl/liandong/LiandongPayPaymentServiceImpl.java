package com.nyd.zeus.service.impls.zzl.liandong;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.order.model.common.DateUtils;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.api.zzl.liandong.LiandongPayPaymentService;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.SqlHelper;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileVO;
import com.nyd.zeus.model.liandong.vo.LiandongCancelVO;
import com.nyd.zeus.model.liandong.vo.LiandongChargeVO;
import com.nyd.zeus.model.liandong.vo.LiandongConfirmVO;
import com.nyd.zeus.model.liandong.vo.LiandongPaymentVO;
import com.nyd.zeus.model.liandong.vo.LiandongQueryChargeVO;
import com.nyd.zeus.model.liandong.vo.LiandongQueryPaymentVO;
import com.nyd.zeus.model.liandong.vo.LiandongSmsBindVO;
import com.nyd.zeus.model.liandong.vo.resp.LiandongChargeResp;
import com.nyd.zeus.model.liandong.vo.resp.LiandongConfirmResp;
import com.nyd.zeus.model.liandong.vo.resp.LiandongPaymentResp;
import com.nyd.zeus.model.payment.PaychannelTempFlow;
import com.nyd.zeus.service.impls.zzl.chanpay.PayConfigFileService;
import com.nyd.zeus.service.impls.zzl.xunlian.XunlianGetDataServiceImpl;
import com.nyd.zeus.service.impls.zzl.xunlian.XunlianPayServiceimpl;
import com.umf.api.service.UmfService;
import com.umf.api.service.UmfServiceImpl;
import com.umf.api.util.StringUtil;

@Service(value="liandongPayPaymentService")
public class LiandongPayPaymentServiceImpl implements LiandongPayPaymentService {

	private Logger logger = LoggerFactory.getLogger(LiandongPayPaymentServiceImpl.class);

	private static final String LIANDONG_CODE = "liandong";

	@Autowired
	private PayConfigFileService payConfigFileService;

	@Autowired
	private ZeusSqlService zeusSqlService;

	@Override
	public CommonResponse<LiandongConfirmResp> contract(LiandongSmsBindVO liandongSmsBindVO) {
		CommonResponse<LiandongConfirmResp> common = new CommonResponse<LiandongConfirmResp>();
		LiandongConfirmResp liandongConfirmResp = new LiandongConfirmResp();
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(LIANDONG_CODE);
		PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
		PayConfigFileVO payConfigFileVO = list.get(0);
		String priKey = payConfigFileVO.getPrdKey();
		String memberid = payConfigFileVO.getMemberId();
		Map<String, String> reqMap = XunlianGetDataServiceImpl.convertBean(liandongSmsBindVO, Map.class);
		reqMap.put("mer_id", memberid);
		//
		//media_type 
		reqMap.put("identity_type", "1");
		//getSerialNum
		String merCustId = new XunlianPayServiceimpl().getSerialNum();
		reqMap.put("mer_cust_id", merCustId);
		// UmfService instance = new UmfServiceImpl("60000100",
		// "G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
		UmfService instance = new UmfServiceImpl(memberid, priKey);
		paychannelTempFlow.setBusinessType("liandong_sms");
		paychannelTempFlow.setPayChannelCode("liandong");
		paychannelTempFlow.setRequestText(reqMap.toString());
		paychannelTempFlow.setRequestTime(new Date());
		logger.info(" 联动发送短信  请求参数" + reqMap.toString());
		Map respMap = instance.CommercialSignOrderMap(reqMap);
		logger.info(" 联动发送短信  响应参数" + respMap.toString());
		String retCode = String.valueOf(respMap.get("ret_code"));
		String retMsg = String.valueOf(respMap.get("ret_msg"));
		liandongConfirmResp.setRet_code(retCode);
		liandongConfirmResp.setRet_msg(retMsg);
		liandongConfirmResp.setBind_id(String.valueOf(respMap.get("bind_id")));
		liandongConfirmResp.setUsr_busi_agreement_id(String.valueOf(respMap.get("usr_busi_agreement_id")));
		liandongConfirmResp.setUsr_pay_agreement_id(String.valueOf(respMap.get("usr_pay_agreement_id")));
		paychannelTempFlow.setResponseText(respMap.toString());
		paychannelTempFlow.setResponseTime(new Date());
		common.setSuccess(true);
		common.setData(liandongConfirmResp);
		// 保存流水表
		try {
			zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
		} catch (Exception e) {
			logger.error(" xunlian 保存身份认证流水信息异常" + e + e.getMessage());
		}

		return common;
	}

	@Override
	public CommonResponse<LiandongConfirmResp> confirm(LiandongConfirmVO liandongConfirmVO) {
		CommonResponse<LiandongConfirmResp> common = new CommonResponse<LiandongConfirmResp>();
		LiandongConfirmResp liandongConfirmResp = new LiandongConfirmResp();
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(LIANDONG_CODE);
		PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
		paychannelTempFlow.setBusinessType("confirm");
		paychannelTempFlow.setPayChannelCode("liandong");
		PayConfigFileVO payConfigFileVO = list.get(0);
		String priKey = payConfigFileVO.getPrdKey();
		String memberid = payConfigFileVO.getMemberId();
		Map<String, String> reqMap = XunlianGetDataServiceImpl.convertBean(liandongConfirmVO, Map.class);
		reqMap.put("mer_id", memberid);
		// UmfService instance = new UmfServiceImpl("60000100",
		// "G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
		UmfService instance = new UmfServiceImpl(memberid, priKey);
		paychannelTempFlow.setRequestText(reqMap.toString());
		paychannelTempFlow.setRequestTime(new Date());	
		logger.info(" 联动确认绑卡  请求参数" + reqMap.toString());
		Map respMap = instance.CommercialSignConfirmMap(reqMap);
		logger.info(" 联动确认绑卡  响应参数" + respMap.toString());
		String retCode = String.valueOf(respMap.get("ret_code"));
		String retMsg =String.valueOf(respMap.get("ret_msg"));
		liandongConfirmResp.setRet_msg(retMsg);
		liandongConfirmResp.setRet_code(retCode);
		liandongConfirmResp.setUsr_busi_agreement_id(String.valueOf(respMap.get("usr_busi_agreement_id")));
		liandongConfirmResp.setUsr_pay_agreement_id(String.valueOf(respMap.get("usr_pay_agreement_id")));
		paychannelTempFlow.setResponseText(respMap.toString());
		paychannelTempFlow.setResponseTime(new Date());
		common.setSuccess(true);
		common.setData(liandongConfirmResp);
		// 保存流水表
		try {
			zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
		} catch (Exception e) {
			logger.error(" liandong 保存身份认证流水信息异常" + e + e.getMessage());
		}

		return common;

	}

	@Override
	public CommonResponse<Map> unbind(LiandongCancelVO liandongCancelVO) {
		CommonResponse<Map> common = new CommonResponse<Map>();
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(LIANDONG_CODE);
		PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
		paychannelTempFlow.setBusinessType("cancel");
		paychannelTempFlow.setPayChannelCode("liandong");
		PayConfigFileVO payConfigFileVO = list.get(0);
		String priKey = payConfigFileVO.getPrdKey();
		String memberid = payConfigFileVO.getMemberId();
		Map<String, String> reqMap = XunlianGetDataServiceImpl.convertBean(liandongCancelVO, Map.class);
		reqMap.put("mer_id", memberid);
		// UmfService instance = new UmfServiceImpl("60000100",
		// "G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
		UmfService instance = new UmfServiceImpl(memberid, priKey);
		paychannelTempFlow.setRequestText(reqMap.toString());
		paychannelTempFlow.setRequestTime(new Date());
		logger.info(" 联动取消绑卡  请求参数" + reqMap.toString());
		Map respMap = instance.unbindMap(reqMap);
		logger.info(" 联动取消绑卡  响应参数" + respMap.toString());
		paychannelTempFlow.setResponseText(respMap.toString());
		paychannelTempFlow.setResponseTime(new Date());
		common.setSuccess(true);
		common.setData(respMap);
		// 保存流水表
		try {
			zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
		} catch (Exception e) {
			logger.error(" xunlian 保存身份认证流水信息异常" + e + e.getMessage());
		}

		return common;

	}

	@Override
	public CommonResponse<LiandongPaymentResp> trans(LiandongPaymentVO liandongPaymentVO) {
		CommonResponse<LiandongPaymentResp> common = new CommonResponse<LiandongPaymentResp>();
		LiandongPaymentResp liandongPaymentResp = new LiandongPaymentResp();

		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(LIANDONG_CODE);
		PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
		paychannelTempFlow.setBusinessType("payment");
		paychannelTempFlow.setPayChannelCode("liandong");
		PayConfigFileVO payConfigFileVO = list.get(0);
		String priKey = payConfigFileVO.getPrdKey();
		String memberid = payConfigFileVO.getMemberId();
		Map<String, String> reqMap = XunlianGetDataServiceImpl.convertBean(liandongPaymentVO, Map.class);
		reqMap.remove("amount");
		reqMap.remove("order_no");
		reqMap.remove("mer_date");
		reqMap.put("mer_id", memberid);
		// UmfService instance = new UmfServiceImpl("60000100",
		// "G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
		UmfService instance = new UmfServiceImpl(memberid, priKey);
		paychannelTempFlow.setRequestText(reqMap.toString());
		paychannelTempFlow.setRequestTime(new Date());
		logger.info(" 联动支付  请求参数" + reqMap.toString());
		Map respMap = instance.agreementPaymentMap(reqMap);
		logger.info(" 联动支付  响应参数" + respMap.toString());
		//0001 处理中   0000请求成功（不代表交易成功）
		String retCode = String.valueOf(respMap.get("ret_code")); //
		String retMsg = String.valueOf(respMap.get("ret_msg"));
		//
		String tradeState = String.valueOf(respMap.get("trade_state"));
		String amount = String.valueOf(respMap.get("amount"));
		String order_id = String.valueOf(respMap.get("order_id"));
		String mer_date = String.valueOf(respMap.get("mer_date"));
		liandongPaymentResp.setAmount(amount);
		liandongPaymentResp.setRet_msg(retMsg);
		liandongPaymentResp.setMer_date(mer_date);
		//订单号为空
		liandongPaymentResp.setOrder_id(order_id);
		if ("00200014".equals(retCode) || "00060780".equals(retCode) || "00060761".equals(retCode)
				|| "00080730".equals(retCode)) {
			retCode = "0001";
		}
		liandongPaymentResp.setRet_code(retCode);
		liandongPaymentResp.setTrade_state(tradeState);
		paychannelTempFlow.setResponseText(respMap.toString());
		paychannelTempFlow.setResponseTime(new Date());
		common.setData(liandongPaymentResp);
		common.setSuccess(true);
		// 保存流水表
		try {
			zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
		} catch (Exception e) {
			logger.error(" xunlian 保存身份认证流水信息异常" + e + e.getMessage());
		}
		return common;

	}

	@Override
	public CommonResponse<LiandongPaymentResp> queryTrans(LiandongQueryPaymentVO liandongQueryPaymentVO) {
		CommonResponse<LiandongPaymentResp> common = new CommonResponse<LiandongPaymentResp>();
		LiandongPaymentResp liandongPaymentResp = new LiandongPaymentResp();
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(LIANDONG_CODE);
		PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
		paychannelTempFlow.setBusinessType("query_payment");
		paychannelTempFlow.setPayChannelCode("liandong");
		PayConfigFileVO payConfigFileVO = list.get(0);
		String priKey = payConfigFileVO.getPrdKey();
		String memberid = payConfigFileVO.getMemberId();
		Map<String, String> reqMap = XunlianGetDataServiceImpl.convertBean(liandongQueryPaymentVO, Map.class);
		reqMap.put("mer_id", memberid);
		// UmfService instance = new UmfServiceImpl("60000100",
		// "G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
		UmfService instance = new UmfServiceImpl(memberid, priKey);
		paychannelTempFlow.setRequestText(reqMap.toString());
		paychannelTempFlow.setRequestTime(new Date());
		logger.info(" 联动支付查询  请求参数" + reqMap.toString());
		Map respMap = instance.queryhistoryOrderMap(reqMap);
		logger.info("联动支付查询  响应参数" + respMap.toString());
		//0001 处理中   0000请求成功（不代表交易成功）
		String retCode = String.valueOf(respMap.get("ret_code")); //
		if("00060761".equals(retCode)||"00200014".equals(retCode)){
			retCode="0001";
		}
		String retMsg = String.valueOf(respMap.get("ret_msg"));
		//
		String tradeState = String.valueOf(respMap.get("trade_state"));
		String amount = String.valueOf(respMap.get("amount"));
		liandongPaymentResp.setTrade_state(tradeState);
		liandongPaymentResp.setAmount(amount);
		liandongPaymentResp.setRet_msg(retMsg);
		liandongPaymentResp.setRet_code(retCode);
		liandongPaymentResp.setOrder_id(String.valueOf(respMap.get("order_id")));
		liandongPaymentResp.setTrade_no(String.valueOf(respMap.get("trade_no")));
		liandongPaymentResp.setMer_date(String.valueOf(respMap.get("mer_date")));
		paychannelTempFlow.setResponseText(respMap.toString());
		paychannelTempFlow.setResponseTime(new Date());
		common.setData(liandongPaymentResp);
		common.setSuccess(true);
		// 保存流水表
		try {
			zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
		} catch (Exception e) {
			logger.error(" 联动 保存身份认证流水信息异常" + e + e.getMessage());
		}
		return common;

	}

	// 打印最终返回商户的map
	public static void printResult(Object obj) {
		if (obj instanceof Map) {
			Map respMap = (Map) obj;
			Iterator iter = respMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = StringUtil.trim(entry.getKey());
				String val = StringUtil.trim(entry.getValue());
				System.out.println("[UMF SDK] 返回商户最终的结果列表-------->key:" + key + "    val:" + val);
				/*
				 * Set key = respMap.keySet(); // respMap.entrySet(); Iterator
				 * it = key.iterator(); while(it.hasNext()){ String k =
				 * it.next().toString(); String val = respMap.get(k).toString();
				 * log_.info("[UMF SDK] 返回商户最终的结果列表-------->k:"+k+"    val:"+val
				 * ); }
				 */
			}
		}
	}

	@Override
	public CommonResponse<LiandongChargeResp> pay(LiandongChargeVO liandongChargeVO) {
		CommonResponse<LiandongChargeResp> common = new CommonResponse<LiandongChargeResp>();
		LiandongChargeResp liandongPaymentResp = new LiandongChargeResp();
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(LIANDONG_CODE);
		PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
		paychannelTempFlow.setBusinessType("charge");
		paychannelTempFlow.setPayChannelCode("liandong");
		PayConfigFileVO payConfigFileVO = list.get(0);
		String priKey = payConfigFileVO.getPrdKey();
		String memberid = payConfigFileVO.getMemberId();
		Map<String, String> reqMap = XunlianGetDataServiceImpl.convertBean(liandongChargeVO, Map.class);
		reqMap.put("checkFlag", "0");
		reqMap.put("mer_id", memberid);
		reqMap.put("purpose", "purpose");
	    reqMap.put("recv_account_type","00");//00:银行卡，02：U付账号
	    reqMap.put("recv_bank_acc_pro","0");//0:对私，1：对公
		// UmfService instance = new UmfServiceImpl("60000100",
		// "G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
		UmfService instance = new UmfServiceImpl(memberid, priKey);
		paychannelTempFlow.setRequestText(reqMap.toString());
		paychannelTempFlow.setRequestTime(new Date());
		logger.info(" 联动代付  请求参数" + reqMap.toString());
		Map respMap = instance.paymentOrderMap(reqMap);
		logger.info(" 联动代付  响应参数" + respMap.toString());
		//0001 处理中   0000请求成功（不代表交易成功）
		String retCode = String.valueOf(respMap.get("ret_code")); //
		if("00180021".equals(retCode)||"00200014".equals(retCode)){
			retCode="0001";
		}
		String retMsg = String.valueOf(respMap.get("ret_msg"));
		//
		String tradeState = String.valueOf(respMap.get("trade_state"));
		String amount = String.valueOf(respMap.get("amount"));
		liandongPaymentResp.setFee(String.valueOf(respMap.get("fee")));
		liandongPaymentResp.setMer_date(String.valueOf(respMap.get("mer_date")));
		liandongPaymentResp.setOrder_id(String.valueOf(respMap.get("order_id")));
		liandongPaymentResp.setTrade_state(tradeState);
		liandongPaymentResp.setAmount(amount);
		liandongPaymentResp.setRet_msg(retMsg);
		liandongPaymentResp.setRet_code(retCode);
		paychannelTempFlow.setResponseText(respMap.toString());
		paychannelTempFlow.setResponseTime(new Date());
		common.setData(liandongPaymentResp);
		common.setSuccess(true);
		// 保存流水表
		try {
			zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
		} catch (Exception e) {
			logger.error(" 联动 代付流水信息异常" + e + e.getMessage());
		}
		return common;

	}

	@Override
	public CommonResponse<LiandongChargeResp> queryPay(LiandongQueryChargeVO liandongQueryChargeVO) {
		CommonResponse<LiandongChargeResp> common = new CommonResponse<LiandongChargeResp>();
		LiandongChargeResp liandongPaymentResp = new LiandongChargeResp();
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(LIANDONG_CODE);
		PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
		paychannelTempFlow.setBusinessType("query_charge");
		paychannelTempFlow.setPayChannelCode("liandong");
		PayConfigFileVO payConfigFileVO = list.get(0);
		String priKey = payConfigFileVO.getPrdKey();
		String memberid = payConfigFileVO.getMemberId();
		Map<String, String> reqMap = XunlianGetDataServiceImpl.convertBean(liandongQueryChargeVO, Map.class);
		reqMap.put("mer_id", memberid);
		// UmfService instance = new UmfServiceImpl("60000100",
		// "G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
		UmfService instance = new UmfServiceImpl(memberid, priKey);
		paychannelTempFlow.setRequestText(reqMap.toString());
		paychannelTempFlow.setRequestTime(new Date());
		logger.info(" 联动代付查询  请求参数" + reqMap.toString());
		Map respMap = instance.queryPaymentStatusMap(reqMap);
		logger.info(" 联动代付查询  响应参数" + respMap.toString());
		//0001 处理中   0000请求成功（不代表交易成功）
		String retCode = String.valueOf(respMap.get("ret_code")); //
		if("00131013".equals(retCode)||"00200014".equals(retCode)){
			retCode="0001";
		}
		String retMsg = String.valueOf(respMap.get("ret_msg"));
		//3失败 4成功  其他处理中
		String tradeState = String.valueOf(respMap.get("trade_state"));
		//封装处理中状态为1
		if(StringUtils.isNotEmpty(tradeState)&&!"3".equals(tradeState)&&!"4".equals(tradeState)){
			tradeState = "1";
		}
		String amount = String.valueOf(respMap.get("amount"));
		liandongPaymentResp.setTrade_state(tradeState);
		liandongPaymentResp.setAmount(amount);
		liandongPaymentResp.setRet_msg(retMsg);
		liandongPaymentResp.setRet_code(retCode);
		liandongPaymentResp.setTrade_desc(String.valueOf(respMap.get("trade_desc")));
		liandongPaymentResp.setOrder_id(String.valueOf(respMap.get("order_id")));
		paychannelTempFlow.setResponseText(respMap.toString());
		paychannelTempFlow.setResponseTime(new Date());
		common.setData(liandongPaymentResp);
		common.setSuccess(true);
		// 保存流水表
		try {
			zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
		} catch (Exception e) {
			logger.error(" 联动 代付查询流水信息异常" + e + e.getMessage());
		}
		return common;

	}

	@Override
	public CommonResponse<LiandongPaymentResp> preTrans(LiandongPaymentVO liandongPaymentVO) {
		CommonResponse<LiandongPaymentResp> common = new CommonResponse<LiandongPaymentResp>();
		LiandongPaymentResp liandongPaymentResp = new LiandongPaymentResp();

		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(LIANDONG_CODE);
		PaychannelTempFlow paychannelTempFlow = new PaychannelTempFlow();
		paychannelTempFlow.setBusinessType("pre_payment");
		paychannelTempFlow.setPayChannelCode("liandong");
		PayConfigFileVO payConfigFileVO = list.get(0);
		String priKey = payConfigFileVO.getPrdKey();
		String memberid = payConfigFileVO.getMemberId();
		Map<String, String> reqMap = new HashMap();
		//String orderId = new XunlianPayServiceimpl().getSerialNum();
		reqMap.put("mer_id", memberid);
	    reqMap.put("order_id",liandongPaymentVO.getOrder_no());
	    reqMap.put("mer_date",liandongPaymentVO.getMer_date());
	    reqMap.put("amount",liandongPaymentVO.getAmount());
		// UmfService instance = new UmfServiceImpl("60000100",
		// "G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
		UmfService instance = new UmfServiceImpl(memberid, priKey);
		paychannelTempFlow.setRequestText(reqMap.toString());
		paychannelTempFlow.setRequestTime(new Date());
		logger.info(" 联动支付预处理  请求参数" + reqMap.toString());
		Map respMap = instance.quickOrderMap(reqMap);
		logger.info(" 联动支付预处理  响应参数" + respMap.toString());
		//   0000请求成功
		String retCode = String.valueOf(respMap.get("ret_code")); //
		String retMsg = String.valueOf(respMap.get("ret_msg"));
		// 0000成功会有流水号
		String trade_no = String.valueOf(respMap.get("trade_no"));
		liandongPaymentResp.setRet_msg(retMsg);
		liandongPaymentResp.setTrade_no(trade_no);
		liandongPaymentResp.setOrder_id(liandongPaymentVO.getOrder_no());
		liandongPaymentResp.setRet_code(retCode);
		paychannelTempFlow.setResponseText(respMap.toString());
		paychannelTempFlow.setResponseTime(new Date());
		common.setData(liandongPaymentResp);
		common.setSuccess(true);
		// 保存流水表
		try {
			zeusSqlService.insertSql(SqlHelper.getInsertSqlByBean(paychannelTempFlow));
		} catch (Exception e) {
			logger.error(" xunlian 保存身份认证流水信息异常" + e + e.getMessage());
		}
		return common;

	}

	@Override
	public CommonResponse<LiandongPaymentResp> summarizeTrans(LiandongPaymentVO liandongPaymentVO) {
		CommonResponse<LiandongPaymentResp> common  = preTrans(liandongPaymentVO);
		if("0000".equals(common.getData().getRet_code())){
			liandongPaymentVO.setTrade_no(common.getData().getTrade_no());
			CommonResponse<LiandongPaymentResp> realCommon = trans(liandongPaymentVO);
			return realCommon;
		}else{
			return common;
		}
	}

}
