package com.nyd.zeus.api.zzl;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.nyd.zeus.model.UrgeOverdueReq;
import com.nyd.zeus.model.UrgeOverdueRespVo;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.nyd.zeus.model.common.req.BillInfoTask;
import com.nyd.zeus.model.common.req.BillProductVO;
import com.nyd.zeus.model.common.response.BillInfoVO;
import com.nyd.zeus.model.common.response.BillRepayVo;
import com.nyd.zeus.model.helibao.PaymentVo;

public interface ZeusForWHServise {
	
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
	public CommonResponse manageRepayment(PaymentVo paymentVo);
	
	
	/**
	 * 需还款列表
	 * @param task
	 * @return
	 */
	public List<BillInfoVO> queryPayList(BillInfoTask task);
	
	/**
	 * 逾期列表
	 * @param task
	 * @return
	 */
	public List<BillInfoVO> queryOverDueList(BillInfoTask task);
	
	/**
	 * 还款处理查询列表
	 * @param task
	 * @return
	 */
	public List<BillRepayVo> queryPayProceList(BillInfoTask task);
	
	/**
	 * 保存账单对应产品表
	 * @param pro
	 * @return
	 */
	public boolean saveBillProduct(BillProductVO pro);
	
	
	/**
	 * 贷后管理 贷后分配列表
	 * @param req
	 * @return
	 */
	public PagedResponse<List<UrgeOverdueRespVo>> getOverdueList(UrgeOverdueReq req);
	
	
	/**
	 * 贷后管理 贷后提醒列表
	 * @param req
	 * @return
	 */
	public PagedResponse<List<UrgeOverdueRespVo>> getOverdueAssignList(UrgeOverdueReq req);
	

}
