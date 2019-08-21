package com.nyd.zeus.api.zzl.liandong;

import java.util.Map;

import com.nyd.zeus.model.common.CommonResponse;
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

/**
 * 联动支付
 * 
 * @author admin
 *
 */

public interface LiandongPayPaymentService {

	/**
	 * 预绑卡
	 */
	public CommonResponse<LiandongConfirmResp> contract(
			LiandongSmsBindVO liandongSmsBindVO);

	/**
	 * 绑卡
	 */
	public CommonResponse<LiandongConfirmResp> confirm(
			LiandongConfirmVO liandongConfirmVO);

	/**
	 * 解绑卡
	 */
	public CommonResponse<Map> unbind(LiandongCancelVO liandongCancelVO);


	/**
	 * 支付预处理
	 */
	public CommonResponse<LiandongPaymentResp> preTrans(LiandongPaymentVO liandongPaymentVO);

	/**
	 * 扣款
	 */
	public CommonResponse<LiandongPaymentResp> trans(LiandongPaymentVO liandongPaymentVO);

	/**
	 * 扣款 整合
	 */
	public CommonResponse<LiandongPaymentResp> summarizeTrans(LiandongPaymentVO liandongPaymentVO);

	/**
	 * 查询扣款
	 */
	public CommonResponse<LiandongPaymentResp> queryTrans(
			LiandongQueryPaymentVO liandongQueryPaymentVO);



	/**
	 * 单笔付款
	 */
	public CommonResponse<LiandongChargeResp> pay(LiandongChargeVO liandongChargeVO);

	/**
	 * 查询单笔付款
	 */
	public CommonResponse<LiandongChargeResp> queryPay(
			LiandongQueryChargeVO liandongQueryChargeVO);

}
