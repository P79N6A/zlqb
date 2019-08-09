package com.nyd.zeus.service.impls.zzl.chanpay;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.api.zzl.chanpay.ChangJiePaymentService;
import com.nyd.zeus.api.zzl.chanpay.ChangPayGetDataService;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.helibao.util.chanpay.ChangPayUtil;
import com.nyd.zeus.model.helibao.util.chanpay.ChkUtil;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieCancelCardVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieCardBinVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieMerchantVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJiePrePayVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieQueryBindVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieQueryMerchantVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangPayBindCardVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangPaySendMsgVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangjieQueryPayVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileVO;
import com.nyd.zeus.model.helibao.vo.pay.resp.chanpay.ChangJieDFResp;
import com.nyd.zeus.model.helibao.vo.pay.resp.chanpay.ChangJiePayCommonResp;



@Service(value="changJiePaymentService")
public class ChangPayPaymentServiceImpl implements ChangJiePaymentService {

	private Logger logger = LoggerFactory.getLogger(ChangPayPaymentServiceImpl.class);

	@Autowired
	private ChangPayGetDataService changPayGetDataService;

	@Autowired
	private PayConfigFileService payConfigFileService;

	/**
	 * 编码类型
	 */
	private static String charset = "UTF-8";

	/**
	 * 
	 */
	private static String CHANG_PAY = "200007-0030";
	
	private static String CHANG_PAY_SUSPAY = "0030";

	@Override
	public CommonResponse<ChangJiePayCommonResp> sendMsg(ChangPaySendMsgVO changPaySendMsgVO) {
		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();

		try {
			String code = CHANG_PAY;
			List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
			PayConfigFileVO PayConfigFileVO = list.get(0);
			String prdKey = PayConfigFileVO.getPrdKey();
			// String urlStr =
			// "https://pay.chanpay.com/mag-unify/gateway/receiveOrder.do?";//
			// 测试环境地址，上生产后需要替换该地址
			String urlStr = PayConfigFileVO.getPayUrl();
			logger.info(" 畅捷预绑卡请求地址为" + urlStr );
			Map<String, String> map = changPayGetDataService.getSendMsgData(changPaySendMsgVO, PayConfigFileVO);
			logger.info(" 畅捷预绑卡请求参数为" + map);
			String ret = new ChangPayUtil().gatewayPost(map, charset, prdKey, urlStr);
			logger.info(" 畅捷预绑卡返回结果：" + ret);
			JSONObject responseJson = JSON.parseObject(ret);
			ChangJiePayCommonResp resp = new ChangJiePayCommonResp();
			String acceptStatus = String.valueOf(responseJson.get("AcceptStatus"));//AcceptStatus ：  S 请求成功   F  请求失败  
			String status = String.valueOf(responseJson.get("Status"));  //Status： S 交易成功   F  交易失败   P 交易处理中
			String retMsg =  String.valueOf(responseJson.get("RetMsg"));  //返回描述
			String retCode =  String.valueOf(responseJson.get("retCode"));  //返回描述
			
			resp.setAcceptStatus(acceptStatus);
			resp.setRetCode(retCode);
			resp.setStatus(status);
			resp.setRetMsg(retMsg);
			// acceptstatus==s&&status!=f 
			//这种情况认为交易成功
			if("S".equals(acceptStatus)&&!"F".equals(status)){
				logger.info("  畅捷预绑卡交易成功");
				resp.setBindStatus("0000");
			}
			common.setData(resp);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("畅捷预绑卡异常" + e.getMessage());
			common.setSuccess(false);
		}
		return common;
	}

	/**
	 * 2.3 鉴权绑卡确认 api nmg_api_auth_sms
	 */
	@Override
	public CommonResponse<ChangJiePayCommonResp> bindCard(ChangPayBindCardVO changPayBindCardVO) {
		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();
		String code = CHANG_PAY;
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
		PayConfigFileVO PayConfigFileVO = list.get(0);
		String prdKey = PayConfigFileVO.getPrdKey();
		String urlStr = PayConfigFileVO.getPayUrl();
		logger.info(" 畅捷确认绑卡请求地址为" + urlStr );
		ChangJiePayCommonResp resp = new ChangJiePayCommonResp();
		Map<String, String> map;
		try {
			map = changPayGetDataService.getBindCardData(changPayBindCardVO, PayConfigFileVO);
			logger.info(" 畅捷确认绑卡请求参数为" + map );
			resp.setProto(map.get("OriAuthTrxId"));
			String ret = new ChangPayUtil().gatewayPost(map, charset, prdKey, urlStr);
			logger.info(" 畅捷确认绑卡返回结果：" + ret);
			JSONObject responseJson = JSON.parseObject(ret);
			
			String acceptStatus = String.valueOf(responseJson.get("AcceptStatus"));//AcceptStatus ：  S 请求成功   F  请求失败  
			String status = String.valueOf(responseJson.get("Status"));  //Status： S 交易成功   F  交易失败   P 交易处理中
			String retMsg =  String.valueOf(responseJson.get("RetMsg"));  //返回描述
			String retCode =  String.valueOf(responseJson.get("retCode"));  //返回描述
			
			resp.setAcceptStatus(acceptStatus);
			resp.setRetCode(retCode);
			resp.setStatus(status);
			resp.setRetMsg(retMsg);
			if("S".equals(acceptStatus)&&!"F".equals(status)){
				logger.info("  畅捷确认绑卡交易成功");
				resp.setBindStatus("0000");
			}
			common.setData(resp);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("畅捷确认绑卡异常" + e.getMessage());
			common.setSuccess(false);
		}

		return common;
	}

	@Override
	public CommonResponse<ChangJiePayCommonResp> cancelBindCard(ChangJieCancelCardVO cancelCardVO) {
		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();
		String code = CHANG_PAY;
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
		PayConfigFileVO PayConfigFileVO = list.get(0);
		String prdKey = PayConfigFileVO.getPrdKey();
		String urlStr = PayConfigFileVO.getPayUrl();
		logger.info(" 畅捷取消绑卡请求地址为" + urlStr );
		ChangJiePayCommonResp resp = new ChangJiePayCommonResp();
		Map<String, String> map;
		try {
			map = changPayGetDataService.getCancelBindData(cancelCardVO, PayConfigFileVO);
			logger.info(" 畅捷取消绑卡请求参数为" + map );
			String ret = new ChangPayUtil().gatewayPost(map, charset, prdKey, urlStr);
			logger.info(" 畅捷 取消绑卡返回结果：" + ret);
			JSONObject responseJson = JSON.parseObject(ret);
			String acceptStatus = String.valueOf(responseJson.get("AcceptStatus"));//AcceptStatus ：  S 请求成功   F  请求失败  
			String status = String.valueOf(responseJson.get("Status"));  //Status： S 交易成功   F  交易失败   P 交易处理中
			String retMsg =  String.valueOf(responseJson.get("RetMsg"));  //返回描述
			String retCode =  String.valueOf(responseJson.get("retCode"));  //返回描述
			
			resp.setAcceptStatus(acceptStatus);
			resp.setRetCode(retCode);
			resp.setStatus(status);
			resp.setRetMsg(retMsg);
			if("S".equals(acceptStatus)&&!"F".equals(status)){
				logger.info("  畅捷 取消绑卡交易成功");
				resp.setBindStatus("0000");
			}
			common.setData(resp);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("畅捷取消绑卡异常" + e.getMessage());
			common.setSuccess(false);
		}

		return common;
	}
	@Override
	public CommonResponse<ChangJiePayCommonResp> prePay(ChangJiePrePayVO changJiePrePayVO) {
		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();
		String code = CHANG_PAY;
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
		PayConfigFileVO PayConfigFileVO = list.get(0);
		String prdKey = PayConfigFileVO.getPrdKey();
		String urlStr = PayConfigFileVO.getPayUrl();
		logger.info(" 畅捷支付请求地址为" + urlStr );
		ChangJiePayCommonResp resp = new ChangJiePayCommonResp();
		Map<String, String> map;
		try {
			map = changPayGetDataService.getPrePayData(changJiePrePayVO, PayConfigFileVO);
			resp.setTrxId(map.get("TrxId"));
			logger.info(" 畅捷支付请求参数为" + map );
			String ret = new ChangPayUtil().gatewayPost(map, charset, prdKey, urlStr);
			logger.info(" 畅捷预支付返回结果：" + ret);
			if(ChkUtil.isEmpty(ret)){
				logger.error("畅捷支付返回异常为null 给处理中状态，做下次查询使用");
				resp.setAcceptStatus("S");
				resp.setRetCode("");
				resp.setStatus("P");
				resp.setRetMsg("未收到请求结果");
				common.setData(resp);
				common.setSuccess(true);
				return common;
			}
			JSONObject responseJson = JSON.parseObject(ret);
			
			String acceptStatus = String.valueOf(responseJson.get("AcceptStatus"));//AcceptStatus ：  S 请求成功   F  请求失败  
			String status = String.valueOf(responseJson.get("Status"));  //Status： S 交易成功   F  交易失败   P 交易处理中
			String retMsg =  String.valueOf(responseJson.get("RetMsg"));  //返回描述
			String retCode =  String.valueOf(responseJson.get("RetCode"));  //返回描述
			
			resp.setAcceptStatus(acceptStatus);
			resp.setRetCode(retCode);
			resp.setStatus(status);
			resp.setRetMsg(retMsg);
			common.setData(resp);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("畅捷预支付异常" + e.getMessage());
			common.setSuccess(false);
		}

		return common;
	}
//	//暂时不用
//	@Override
//	public CommonResponse<JSONObject> pay(ChangJiePayVO changJiePayVO) {
//		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
//		String code = CHANG_PAY;
//		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
//		PayConfigFileVO PayConfigFileVO = list.get(0);
//		String prdKey = PayConfigFileVO.getPrdKey();
//		String urlStr = PayConfigFileVO.getPayUrl();
//		Map<String, String> map;
//		try {
//			map = changPayGetDataService.getPayData(changJiePayVO, PayConfigFileVO);
//			String ret = new ChangPayUtil().gatewayPost(map, charset, prdKey, urlStr);
//			logger.info(" 畅捷确认支付返回结果：" + ret);
//			JSONObject responseJson = JSON.parseObject(ret);
//			common.setData(responseJson);
//			common.setSuccess(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("畅捷确认支付异常" + e.getMessage());
//			common.setSuccess(false);
//		}
//
//		return common;
//	}


	@Override
	public CommonResponse<ChangJiePayCommonResp> queryPay(ChangjieQueryPayVO changjieQueryPayVO) throws Exception {
		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();
		String code = CHANG_PAY;
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
		PayConfigFileVO PayConfigFileVO = list.get(0);
		String prdKey = PayConfigFileVO.getPrdKey();
		String urlStr = PayConfigFileVO.getPayUrl();
		logger.info(" 畅捷支付查询请求地址为：" + urlStr);
		ChangJiePayCommonResp resp = new ChangJiePayCommonResp();
		Map<String, String> map;
		try {
			map = changPayGetDataService.getQueryPayData(changjieQueryPayVO, PayConfigFileVO);
			logger.info(" 畅捷支付查询请求参数为：" + map);
			String ret = new ChangPayUtil().gatewayPost(map, charset, prdKey, urlStr);
			logger.info(" 畅捷支付查询返回结果：" + ret);
			JSONObject responseJson = JSON.parseObject(ret);
			
			String acceptStatus = String.valueOf(responseJson.get("AcceptStatus"));//AcceptStatus ：  S 请求成功   F  请求失败  
			String status = String.valueOf(responseJson.get("Status"));  //Status： S 交易成功   F  交易失败   P 交易处理中
			String retMsg =  String.valueOf(responseJson.get("RetMsg"));  //返回描述
			String retCode =  String.valueOf(responseJson.get("RetCode"));  //返回描述
			String trxId = String.valueOf(responseJson.get("TrxId")); 
			resp.setTrxId(trxId);
			resp.setAcceptStatus(acceptStatus);
			resp.setRetCode(retCode);
			resp.setStatus(status);
			resp.setRetMsg(retMsg);
			common.setData(resp);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("畅捷支付查询异常" + e.getMessage());
			common.setSuccess(false);
		}

		return common;
	}

	@Override
	public CommonResponse<ChangJiePayCommonResp> queryBind(ChangJieQueryBindVO queryBindVO) {
		CommonResponse<ChangJiePayCommonResp> common = new CommonResponse<ChangJiePayCommonResp>();
		String code = CHANG_PAY;
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
		PayConfigFileVO PayConfigFileVO = list.get(0);
		String prdKey = PayConfigFileVO.getPrdKey();
		String urlStr = PayConfigFileVO.getPayUrl();
		ChangJiePayCommonResp resp = new ChangJiePayCommonResp();
		Map<String, String> map;
		try {
			map = changPayGetDataService.getQueryBindData(queryBindVO, PayConfigFileVO);
			logger.info(" 畅捷绑卡查询请求参数为"  + map);
			String ret = new ChangPayUtil().gatewayPost(map, charset, prdKey, urlStr);
			logger.info(" 畅捷绑卡查询返回结果：" + ret);
			JSONObject responseJson = JSON.parseObject(ret);
			
			String acceptStatus = String.valueOf(responseJson.get("AcceptStatus"));//AcceptStatus ：  S 请求成功   F  请求失败  
			String status = String.valueOf(responseJson.get("Status"));  //Status： S 交易成功   F  交易失败   P 交易处理中
			String retMsg =  String.valueOf(responseJson.get("RetMsg"));  //返回描述
			String retCode =  String.valueOf(responseJson.get("retCode"));  //返回描述
			if("S".equals(acceptStatus)&&!"F".equals(status)){
				logger.info("  畅捷绑卡查询成功");
				resp.setBindStatus("0000");
			}
			resp.setAcceptStatus(acceptStatus);
			resp.setRetCode(retCode);
			resp.setStatus(status);
			resp.setRetMsg(retMsg);
			common.setData(resp);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("畅捷绑卡查询异常" + e.getMessage());
			common.setSuccess(false);
		}
		return common;
	}

	@Override
	public CommonResponse<ChangJieDFResp> payForAnother(ChangJieMerchantVO changJieMerchantVO) {
		CommonResponse<ChangJieDFResp> common = new CommonResponse<ChangJieDFResp>();
		String code = CHANG_PAY_SUSPAY;
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
		PayConfigFileVO PayConfigFileVO = list.get(0);
		String prdKey = PayConfigFileVO.getPrdKey();
		String urlStr = PayConfigFileVO.getPayUrl();
		logger.info(" 畅捷代付请求地址为"  + urlStr);
		ChangJieDFResp cjResp = new ChangJieDFResp();
		Map<String, String> map;
		try {
			//调用卡bin查询接口查询银行名称
			ChangJieCardBinVO changJieCardBinVO = new ChangJieCardBinVO();
			changJieCardBinVO.setCardNum(changJieMerchantVO.getAccntno());
			CommonResponse<JSONObject> cardBinCommon  = queryCardBin(changJieCardBinVO);
			JSONObject json = cardBinCommon.getData();
			String bankCommonName = String.valueOf(json.get("BankCommonName"));
			changJieMerchantVO.setBankName(bankCommonName);
			map = changPayGetDataService.getPayAnother(changJieMerchantVO, PayConfigFileVO);
			logger.info(" 畅捷代付请求参数为"  + map);
			String ret = new ChangPayUtil().gatewayPost(map, charset, prdKey, urlStr);
			logger.info(" 畅捷代付返回结果：" + ret);
			JSONObject responseJson = JSON.parseObject(ret);
			//返回参数解析
			String acceptStatus = String.valueOf(responseJson.get("AcceptStatus"));
			String originalRetCode =String.valueOf(responseJson.get("OriginalRetCode"));
			String platformRetCode = String.valueOf(responseJson.get("PlatformRetCode"));
			String appretCode = String.valueOf(responseJson.get("AppRetcode"));
			if("S".equals(acceptStatus)&&"0000".equals(platformRetCode)&&"000000".equals(originalRetCode)){
				cjResp.setFinalCode("0000");
			}
			if("2000".equals(platformRetCode)||"01019999".equals(appretCode)||
					"000001".equals(originalRetCode)||"900001".equals(originalRetCode)
					||"900002".equals(originalRetCode)||"900003".equals(originalRetCode)){
				cjResp.setFinalCode("0001");
			}
			cjResp.setOriginalErrorMessage(String.valueOf(responseJson.get("OriginalErrorMessage")));
			cjResp.setPlatformErrorMessage(String.valueOf(responseJson.get("PlatformErrorMessage")));
			cjResp.setOutTradeNo(String.valueOf(responseJson.get("OutTradeNo")));
			cjResp.setFee(String.valueOf(responseJson.get("Fee")));
			cjResp.setTransAmt(String.valueOf(responseJson.get("TransAmt")));
			cjResp.setAppRetcode(appretCode);
			cjResp.setAcceptStatus(acceptStatus);
			cjResp.setOriginalRetCode(originalRetCode);
			cjResp.setPlatformRetCode(platformRetCode);
			cjResp.setTimeStamp(String.valueOf(responseJson.get("TimeStamp")));
			cjResp.setAppRetMsg(String.valueOf(responseJson.get("AppRetMsg")));
			common.setData(cjResp);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("畅捷代付异常" + e.getMessage());
			common.setSuccess(false);
		}

		return common;
	}

	@Override
	public CommonResponse<ChangJieDFResp> queryPayForAnother(ChangJieQueryMerchantVO changJieQueryMerchantVO) {
		CommonResponse<ChangJieDFResp> common = new CommonResponse<ChangJieDFResp>();
		String code = CHANG_PAY_SUSPAY;
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
		PayConfigFileVO PayConfigFileVO = list.get(0);
		String prdKey = PayConfigFileVO.getPrdKey();
		String urlStr = PayConfigFileVO.getPayUrl();
		logger.info(" 畅捷代付查询请求地址为"  + urlStr);
		Map<String, String> map;
		try {
			ChangJieDFResp cjResp = new ChangJieDFResp();
			map = changPayGetDataService.getQueryPayAnother(changJieQueryMerchantVO, PayConfigFileVO);
			logger.info(" 畅捷代付查询请求参数为"  + map);
			String ret = new ChangPayUtil().gatewayPost(map, charset, prdKey, urlStr);
			logger.info(" 畅捷代付查询返回结果：" + ret);
			JSONObject responseJson = JSON.parseObject(ret);
			//返回参数解析
			String acceptStatus = String.valueOf(responseJson.get("AcceptStatus"));
			String originalRetCode =String.valueOf(responseJson.get("OriginalRetCode"));
			String platformRetCode = String.valueOf(responseJson.get("PlatformRetCode"));
			String appretCode = String.valueOf(responseJson.get("AppRetcode"));
			if("S".equals(acceptStatus)&&"0000".equals(platformRetCode)&&"000000".equals(originalRetCode)){
				cjResp.setFinalCode("0000");
			}
			if("2000".equals(platformRetCode)||"01019999".equals(appretCode)||
					"000001".equals(originalRetCode)||"900001".equals(originalRetCode)
					||"900002".equals(originalRetCode)||"900003".equals(originalRetCode)){
				cjResp.setFinalCode("0001");
			}
			cjResp.setOriginalErrorMessage(String.valueOf(responseJson.get("OriginalErrorMessage")));
			cjResp.setPlatformErrorMessage(String.valueOf(responseJson.get("PlatformErrorMessage")));
			cjResp.setOutTradeNo(String.valueOf(responseJson.get("OutTradeNo")));
			cjResp.setFee(String.valueOf(responseJson.get("Fee")));
			cjResp.setTransAmt(String.valueOf(responseJson.get("TransAmt")));
			cjResp.setAppRetcode(appretCode);
			cjResp.setAcceptStatus(acceptStatus);
			cjResp.setOriginalRetCode(originalRetCode);
			cjResp.setPlatformRetCode(platformRetCode);
			cjResp.setTimeStamp(String.valueOf(responseJson.get("TimeStamp")));
			cjResp.setAppRetMsg(String.valueOf(responseJson.get("AppRetMsg")));
			common.setData(cjResp);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("畅捷代付查询异常" + e.getMessage());
			common.setSuccess(false);
		}

		return common;
	}

	@Override
	public CommonResponse<JSONObject> queryCardBin(ChangJieCardBinVO changJieCardBinVO) {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		String code = CHANG_PAY;
		List<PayConfigFileVO> list = payConfigFileService.queryByCodeId(code);
		PayConfigFileVO PayConfigFileVO = list.get(0);
		String prdKey = PayConfigFileVO.getPrdKey();
		String urlStr = PayConfigFileVO.getPayUrl();
		logger.info(" 卡bin查询请求地址为"  + urlStr);
		Map<String, String> map;
		try {
			map = changPayGetDataService.getCardBinData(changJieCardBinVO, PayConfigFileVO);
			logger.info(" 卡bin查询请求参数为"  + map);
			String ret = new ChangPayUtil().gatewayPost(map, charset, prdKey, urlStr);
			logger.info(" 畅捷卡bin查询返回结果：" + ret);
			JSONObject responseJson = JSON.parseObject(ret);
			common.setData(responseJson);
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("畅捷卡bin查询异常" + e.getMessage());
			common.setSuccess(false);
		}
		return common;
	}
	
	public static void main(String[] args) {
		String i = "1500.00";
		String a = String.format("%.2f", Double.parseDouble(i) / 100);
		System.out.println(a);
	}



}
