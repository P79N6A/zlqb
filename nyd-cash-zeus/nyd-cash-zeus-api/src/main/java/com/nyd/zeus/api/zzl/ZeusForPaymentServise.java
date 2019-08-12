package com.nyd.zeus.api.zzl;


import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.xunlian.XunlianReqPaymentVO;
public interface ZeusForPaymentServise {
	
	/**
	 * 讯联主动支付
	 * @param xunlianReqPaymentVO
	 * @return
	 */
	public CommonResponse<String> xunlianPay(XunlianReqPaymentVO xunlianReqPaymentVO);
	
	/**
	 * 
	 * @param xunlianReqPaymentVO
	 * @return
	 */
	public CommonResponse<String> judgeStatus(XunlianReqPaymentVO xunlianReqPaymentVO);

	

}
