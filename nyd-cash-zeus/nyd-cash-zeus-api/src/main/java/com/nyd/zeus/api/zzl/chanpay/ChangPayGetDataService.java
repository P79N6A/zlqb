package com.nyd.zeus.api.zzl.chanpay;

import java.util.Map;

import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieCancelCardVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieCardBinVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieMerchantVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJiePayVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJiePrePayVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieQueryBindVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieQueryMerchantVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangPayBindCardVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangPaySendMsgVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangjieQueryPayVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileVO;




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
