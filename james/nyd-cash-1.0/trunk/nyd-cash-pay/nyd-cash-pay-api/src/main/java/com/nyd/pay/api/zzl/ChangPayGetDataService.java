package com.nyd.pay.api.zzl;

import java.util.Map;

import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJieCancelCardVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJieCardBinVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJieMerchantVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJiePayVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJiePrePayVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJieQueryBindVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangJieQueryMerchantVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangPayBindCardVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangPaySendMsgVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.ChangjieQueryPayVO;
import com.creativearts.nyd.collectionPay.model.zzl.changjie.req.PayConfigFileVO;



public interface ChangPayGetDataService {

	
	/**
	 * 获取预绑卡参数
	 */
	
	public Map<String,String> getSendMsgData(ChangPaySendMsgVO changPaySendMsgVO,PayConfigFileVO PayConfigFileVO) throws Exception;
	/**
	 * 获取确认绑卡参数
	 */
	public Map<String,String> getBindCardData(ChangPayBindCardVO changPayBindCardVO,PayConfigFileVO PayConfigFileVO) throws Exception;

	/**
	 * 获取解绑卡参数
	 */
	public Map<String,String> getCancelBindData(ChangJieCancelCardVO cancelCardVO,PayConfigFileVO PayConfigFileVO) throws Exception;

	/**
	 * 获取畅捷预支付参数
	 * @param prePayVO
	 * @param PayConfigFileVO
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getPrePayData(ChangJiePrePayVO prePayVO,PayConfigFileVO PayConfigFileVO) throws Exception;
	/**
	 * 获取支付接口参数 (暂时不用 支付直接调用预支付就好)
	 */
	public Map<String,String> getPayData(ChangJiePayVO changJiePayVO,PayConfigFileVO PayConfigFileVO) throws Exception;

	/**
	 * 获取支付接口参数
	 */
	public Map<String,String> getQueryPayData(ChangjieQueryPayVO changjieQueryPayVO,PayConfigFileVO PayConfigFileVO) throws Exception;

	/**
	 * 获取支付查询参数
	 */
	public Map<String,String> getQueryBindData(ChangJieQueryBindVO queryBindVO,PayConfigFileVO PayConfigFileVO) throws Exception;
	
	/**
	 * 获取畅捷代付接口参数
	 */
	public Map<String, String> getPayAnother(ChangJieMerchantVO changJieMerchantVO,PayConfigFileVO PayConfigFileVO) throws Exception;
	
	/**
	 * 获取查询畅捷代付接口参数
	 */
	
	public Map<String,String> getQueryPayAnother(ChangJieQueryMerchantVO changJieQueryMerchantVO,PayConfigFileVO PayConfigFileVO) throws Exception;
	/**
	 * 畅捷卡bin查询接口
	 */
	public Map<String,String> getCardBinData(ChangJieCardBinVO changJieCardBinVO,PayConfigFileVO PayConfigFileVO)throws Exception;
}
