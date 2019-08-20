package com.nyd.zeus.service.impls.zzl.liandong;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		reqMap.put("mer_id", "");
		reqMap.put("identity_type", "1");
		reqMap.put("mer_cust_id", "13333333333");
		// UmfService instance = new UmfServiceImpl("60000100",
		// "G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
		UmfService instance = new UmfServiceImpl(memberid, priKey);
		paychannelTempFlow.setBusinessType("liandong_sms");
		paychannelTempFlow.setPayChannelCode("liandong");
		paychannelTempFlow.setRequestText(reqMap.toString());
		paychannelTempFlow.setRequestTime(new Date());
		Map respMap = instance.CommercialSignOrderMap(reqMap);
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
		Map respMap = instance.CommercialSignConfirmMap(reqMap);
		String retCode = String.valueOf(respMap.get("ret_code"));
		String retMsg =String.valueOf(respMap.get("ret_msg"));
		liandongConfirmResp.setRet_msg(retMsg);
		liandongConfirmResp.setRet_code(retCode);
		liandongConfirmResp.setUsr_busi_agreement_id(String.valueOf(respMap.get("usr_busi_agreement_id")));
		liandongConfirmResp.setUsr_pay_agreement_id(String.valueOf(respMap.get("usr_pay_agreement_id ")));
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
		Map respMap = instance.unbindMap(reqMap);
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
		reqMap.put("mer_id", memberid);
		// UmfService instance = new UmfServiceImpl("60000100",
		// "G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
		UmfService instance = new UmfServiceImpl(memberid, priKey);
		paychannelTempFlow.setRequestText(reqMap.toString());
		paychannelTempFlow.setRequestTime(new Date());
		Map respMap = instance.agreementPaymentMap(reqMap);
		//0001 处理中   0000请求成功（不代表交易成功）
		String retCode = String.valueOf(respMap.get("ret_code")); //
		String retMsg = String.valueOf(respMap.get("ret_msg"));
		//
		String tradeState = String.valueOf(respMap.get("trade_state"));
		String amount = String.valueOf(respMap.get("amount"));
		liandongPaymentResp.setAmount(amount);
		liandongPaymentResp.setRet_msg(retMsg);
		//订单号为空
		liandongPaymentResp.setOrder_id("");
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
		Map respMap = instance.queryhistoryOrderMap(reqMap);
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
		reqMap.put("mer_id", memberid);
		// UmfService instance = new UmfServiceImpl("60000100",
		// "G:/tecent/test/60000100商户签名证书/60000100_.key.p8");
		UmfService instance = new UmfServiceImpl(memberid, priKey);
		paychannelTempFlow.setRequestText(reqMap.toString());
		paychannelTempFlow.setRequestTime(new Date());
		Map respMap = instance.paymentOrderMap(reqMap);
		//0001 处理中   0000请求成功（不代表交易成功）
		String retCode = String.valueOf(respMap.get("ret_code")); //
		if("00180021".equals(retCode)||"00200014".equals(retCode)){
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
		Map respMap = instance.queryPaymentStatusMap(reqMap);
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

}
