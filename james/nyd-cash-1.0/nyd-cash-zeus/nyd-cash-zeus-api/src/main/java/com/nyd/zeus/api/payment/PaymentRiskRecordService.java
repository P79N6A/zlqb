package com.nyd.zeus.api.payment;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.model.PaymentRiskFlowVo;
import com.nyd.zeus.model.PaymentRiskRecordExtendVo;
import com.nyd.zeus.model.PaymentRiskRecordVo;
import com.nyd.zeus.model.PaymentRiskRequestChangjie;
import com.nyd.zeus.model.PaymentRiskRequestCommon;
import com.nyd.zeus.model.common.CommonResponse;

public interface PaymentRiskRecordService {
	
	/**
	 * 主动扣款接口
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse activeRepayment(PaymentRiskRequestCommon vo);
	/**
	 * 存储其他信息
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse saveRepaymentExtend(PaymentRiskRecordExtendVo vo);
	
	/**
	 *   更新扣款接口
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse updatePaymentRisk(PaymentRiskRequestCommon vo);
	
	/**
	 * 还款跑批接口(风控  4 点)
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse batchRepayTask(PaymentRiskRecordVo paymentRiskRecord);
	/**
	 * 还款处理中跑批接口(风控  )
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse batchQueryRepayTask(PaymentRiskRecordVo paymentRiskRecord);
	
	/**
	 * 查询订单的扣款资金
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse<JSONObject> queryOrderCostMoney(String orderNo);
	
	/**
	 *    当日扣款总金额
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse<JSONObject> allCostMoney();
	
	/**
	 *    扣款成功流水
	 * @param paymentVo
	 * @return
	 */
	public CommonResponse<List<PaymentRiskFlowVo>> successFlow(String orderNo);
	
	/**
	 * 查询处理中数据
	 * @param paymentVo
	 * @return
	 */
	public List<PaymentRiskRecordVo> getProcessData();
	/**
	 * 查询准备数据
	 * @param paymentVo
	 * @return
	 */
	public List<PaymentRiskRecordVo> getReadyData();
	/**
	 * 查询需要数据
	 * @param paymentVo
	 * @return
	 */
	public List<PaymentRiskRecordVo> getNotNoticeData();
	
	/**
	 * 失效数据
	 * @param paymentVo
	 * @return
	 */
	public void doInvalid();
	/**
	 * 更新
	 * @param paymentVo
	 * @return
	 */
	public void update(PaymentRiskRecordVo vo);
	
	/**
	 * 更新
	 * @param paymentVo
	 * @return
	 */
	public void testXunlian(String one, String two, String three, String four, String five, String p,int times);
	void testChangjie(String one, String two, String three, String four, String five, String six, String seven,
			String eight, String nine, String p, int times);
	
	

}
