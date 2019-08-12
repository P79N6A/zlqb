package com.nyd.zeus.api;

import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.helibao.PaymentVo;

public interface BillRepayService {
	
	/**
	 * 主动扣款接口
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse activeRepayment(PaymentVo paymentVo);
	
	/**
	 * 还款跑批接口
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse batchRepayTask(PaymentVo paymentVo);
	
	/**
	 * 逾期跑批接口
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse batchOverDueTask(PaymentVo paymentVo);
	
	/**
	 * 管理系统代扣接口
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse mannagerRepayment(PaymentVo paymentVo);
	
	/**
	 * 平账接口
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse flatAccount(PaymentVo paymentVo);
	
	
	

}
