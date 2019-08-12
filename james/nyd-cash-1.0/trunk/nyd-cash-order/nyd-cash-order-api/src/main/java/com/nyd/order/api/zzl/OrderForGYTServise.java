package com.nyd.order.api.zzl;

import java.util.List;

import com.nyd.order.entity.refund.RefundApplyEntity;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.refund.request.RefundListRequest;
import com.nyd.order.model.refund.vo.RefundFlowVo;
import com.nyd.order.model.refund.vo.RefundListVo;
import com.nyd.order.model.refund.vo.RefundRatioVo;
import com.nyd.order.model.refund.vo.SumAmountVo;

public interface OrderForGYTServise {
	
	/**
	 * 退款申请列表查询
	 * @param request
	 * @return
	 */
	PagedResponse<List<RefundListVo>> refundList(RefundListRequest request);
	
	/**
	 * 退款比例查询
	 * @param request
	 * @return
	 */
	CommonResponse<RefundRatioVo> refundRatio();
	
	/**
	 * 退款处理记录
	 * @param request
	 * @return
	 */
	CommonResponse<List<RefundFlowVo>> queryRefundFlow(String orderNo);
	
	/**
	 * 退款处理记录金额合计
	 * @param request
	 * @return
	 */
	CommonResponse<SumAmountVo> sumAmount(String orderNo);
	
	/**
	 * 查询所有处理中退款订单
	 * @return
	 */
	List<RefundApplyEntity> queryProcessing();
	
	/**
	 * 更新退款订单状态
	 */
	Boolean updateRefundStatus(RefundApplyEntity entity);

}

