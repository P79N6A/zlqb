package com.nyd.order.api.zzl;

import java.util.List;

import com.nyd.order.model.JudgePeople;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.order.*;


public interface OrderForSLHServise {
	/**
	 * 订单分配列表查询
	 * @param orderParam
	 * @return
	 */
	PagedResponse<List<OrderListVO>>  orderApportionAndUndistributed(OrderParamVO orderParam);
	CommonResponse  orderAllocationToUser(OrderAllocationVO orderAllocationVO);
     UserPicture  queryUserPicture(OrderDetialsVO orderDetials);
	/**
	 * 获取最新待审的分配的订单
	 * @return
	 */
	OrderListVO queryNewAutoAllocatnOrder();
	/**
	 * 自动分配待审订单
	 * @param list
	 */
	CommonResponse autoOrderAllocationToUser(List<OrderAllocationVO> list);
	/**
	 * 待信审订单 分配撤回
	 * @param withdrawOrderVo
	 * @return
	 */
	CommonResponse withdrawOrder(WithdrawOrderVo withdrawOrderVo);
	/**
	 * 待信审订单 分配全部撤回
	 * @return
	 */
	CommonResponse withdrawAllOrder();
}
