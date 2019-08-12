package com.nyd.pay.api.zzl;

import com.alibaba.fastjson.JSONObject;
import com.creativearts.nyd.collectionPay.model.zzl.CommonResponse;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJieCancelCardVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJieCardBinVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJieMerchantVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJiePrePayVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJieQueryBindVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJieQueryMerchantVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangPayBindCardVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangPaySendMsgVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangjieQueryPayVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.resp.ChangJieDFResp;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.resp.ChangJiePayCommonResp;


/**
 * 畅捷协议支付接口
 * @author admin
 *
 */
public interface ChangJiePaymentService {

	/**
	 * 发送短信验证码（预签约）
	 * @param 
	 * @return
	 */
	public CommonResponse<ChangJiePayCommonResp> sendMsg(ChangPaySendMsgVO changPaySendMsgVO);
	/**
	 * 确认绑卡
	 * @param 
	 * @return
	 */
	public CommonResponse<ChangJiePayCommonResp> bindCard(ChangPayBindCardVO changPayBindCardVO);
	/**
	 * 取消绑卡
	 * @param sendMsgVO
	 * @return
	 */
	public CommonResponse<ChangJiePayCommonResp> cancelBindCard(ChangJieCancelCardVO cancelCardVO);
	
	/**
	 * 支付 
	 * @param changJiePrePayVO
	 * @return
	 */
	public CommonResponse<ChangJiePayCommonResp> prePay(ChangJiePrePayVO changJiePrePayVO);
	/**
	 * 	暂时不用
	 * 支付 客户-公司
	 * @param sendMsgVO
	 * @return
	 */
	//public CommonResponse<JSONObject> pay(ChangJiePayVO changJiePayVO);
	/**
	 * 支付结果查询
	 * @param sendMsgVO
	 * @return
	 */
	public CommonResponse<ChangJiePayCommonResp> queryPay(ChangjieQueryPayVO changjieQueryPayVO) throws Exception;
	
	
	/**
	 * 绑定查询接口
	 * @param sendMsgVO
	 * @return
	 */
	public CommonResponse<ChangJiePayCommonResp> queryBind(ChangJieQueryBindVO queryBindVO);
	
	/**
	 * 畅捷代付接口（转账给客户）
	 * @param 
	 * @return
	 */
	public CommonResponse<ChangJieDFResp> payForAnother(ChangJieMerchantVO changJieMerchantVO);
	
	/**
	 * 畅捷代付查询接口
	 * @param 
	 * @return
	 */
	public CommonResponse<ChangJieDFResp> queryPayForAnother(ChangJieQueryMerchantVO changJieQueryMerchantVO);
	/**
	 * 畅捷卡bin查询接口
	 */
	public CommonResponse<JSONObject> queryCardBin(ChangJieCardBinVO changJieCardBinVO);
	
	
	
	

}
