package com.nyd.zeus.api.zzl.hnapay;

import com.nyd.zeus.model.common.CommonResponse;
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

/**
 * 新生支付
 * 
 * @author admin
 *
 */

public interface HnaPayPaymentService {

	/**
	 * 预绑卡
	 */
	public CommonResponse<HnaPayContractResp> contract(
			HnaPayContractReq hnaPayContractReq);

	/**
	 * 绑卡
	 */
	public CommonResponse<HnaPayConfirmResp> confirm(
			HnaPayConfirmReq hnaPayConfirmReq);

	/**
	 * 解绑卡
	 */
	public CommonResponse<?> unbind(HnaPayUnbindReq hnaPayUnbindReq);

	/**
	 * 预扣款
	 */
	public CommonResponse<HnaPayPreTransResp> preTrans(
			HnaPayPreTransReq hnaPayPreTransReq);

	/**
	 * 扣款
	 */
	public CommonResponse<HnaPayTransResp> trans(HnaPayTransReq hnaPayTransReq);

	/**
	 * 扣款合并
	 */
	public CommonResponse<HnaPayTransResp> transMerge(
			HnaPayPreTransReq hnaPayPreTransReq);

	/**
	 * 查询扣款
	 */
	public CommonResponse<HnaPayQueryTransResp> queryTrans(
			HnaPayQueryTransReq hnaPayQueryTransReq);

	/**
	 * 退款
	 */
	public CommonResponse<HnaPayRefundResp> refund(
			HnaPayRefundReq hnaPayRefundReq);

	/**
	 * 单笔付款
	 */
	public CommonResponse<HnaPayPayResp> pay(HnaPayPayReq hnaPayPayReq);

	/**
	 * 查询单笔付款
	 */
	public CommonResponse<HnaPayQueryPayResp> queryPay(
			HnaPayQueryPayReq hnaPayQueryPayReq);

}
