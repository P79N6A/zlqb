package com.nyd.order.dao.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nyd.order.entity.refund.RefundApplyEntity;
import com.nyd.order.model.refund.request.RefundListRequest;
import com.nyd.order.model.refund.request.SaveRefundApplyRequest;
import com.nyd.order.model.refund.vo.RefundFlowVo;
import com.nyd.order.model.refund.vo.RefundListVo;
import com.nyd.order.model.refund.vo.SumAmountVo;

@Repository
public interface RefundApplyMapper {
	
	List<RefundListVo> queryRefundList(RefundListRequest request);
	
	Long queryRefundListCount(RefundListRequest request);
	
	List<RefundFlowVo> queryRefundFlow(@Param("orderNo")String orderNo);
	
	void save(SaveRefundApplyRequest request);
	
	Long queryIsApplay(Map<String, String> map);
	
	String queryTotalIncome();
	
	String queryRefunded();
	//查询所有处理中退款订单
	List<RefundApplyEntity> queryProcessing();
	
	Long updateInfo(RefundApplyEntity entity);
	
	SumAmountVo sumAmount(@Param("orderNo")String orderNo);
}
