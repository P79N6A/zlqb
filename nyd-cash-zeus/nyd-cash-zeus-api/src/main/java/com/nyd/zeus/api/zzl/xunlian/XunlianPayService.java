package com.nyd.zeus.api.zzl.xunlian;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.model.common.CommonResponse;
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

/**
 * 讯联支付
 * @author admin
 *
 */

public interface XunlianPayService {

	/**
	 * 身份认证
	 * @param identifyauthVO
	 * @return
	 */
	public CommonResponse<IdentifyResp> sendMsg(IdentifyauthVO identifyauthVO);
	
	/**
	 * 确认签约
	 * @param identifyauthVO
	 * @return
	 */
	public CommonResponse<IdentifyResp> sign(XunlianSignVO xunlianSignVO);
	
	/**
	 * 支付
	 * @param identifyauthVO
	 * @return
	 */
	public CommonResponse<XunlianPayResp> pay(XunlianPaymentVO xunlianPaymentVO,String orderTime);
	
	
	/**
	 * 支付查询
	 * @param identifyauthVO
	 * @return
	 */
	public CommonResponse<XunlianQueryPayResp> queryPay(XunlianQueryPayVO xunlianQueryPayVO,String orderTime);
	
	/**
	 * 支付查询
	 * @param identifyauthVO
	 * @return
	 */
	public CommonResponse<JSONObject> cancelBind(XunlianCancelBindVO xunlianCancelBindVO);
	
	
	/**
	 * 代付接口
	 * @param identifyauthVO
	 * @return
	 */
	public CommonResponse<XunlianChargeResp> charge(XunlianChargeVO xunlianChargeVO,String orderTime);
	
	/**
	 * 代付接口
	 * @param identifyauthVO
	 * @return
	 */
	public CommonResponse<XunlianQueryChargeResp> queryCharge(XunlianQueryChargeVO xunlianQueryChargeVO,String orderTime);
	
	/**
	 * 代付接口
	 * @param identifyauthVO
	 * @return
	 */
	public CommonResponse<XunlianChargeResp> chargeEnterprise(XunlianChargeEnterVO xunlianChargeVO);
	
	
	
	
	

}
