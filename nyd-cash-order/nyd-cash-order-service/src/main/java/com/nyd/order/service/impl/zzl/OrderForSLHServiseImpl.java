package com.nyd.order.service.impl.zzl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.order.api.zzl.OrderForSLHServise;
import com.nyd.order.api.zzl.OrderSqlService;
import com.nyd.order.dao.mapper.OrderMapper;
import com.nyd.order.entity.Order;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.enums.OrderStatus;
import com.nyd.order.model.order.OrderAllocationVO;
import com.nyd.order.model.order.OrderDetialsVO;
import com.nyd.order.model.order.OrderListVO;
import com.nyd.order.model.order.OrderParamVO;
import com.nyd.order.model.order.UserInfo;
import com.nyd.order.model.order.UserPicture;

@Service("orderForSLHServise")
public class OrderForSLHServiseImpl implements OrderForSLHServise{
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderSqlService orderSqlService;
	
	@Override
	public PagedResponse<List<OrderListVO>> orderApportionAndUndistributed(OrderParamVO orderParam) {
		PagedResponse<List<OrderListVO>> data = new PagedResponse<List<OrderListVO>>();
		List<OrderListVO> temp = orderMapper.queryOrderByUserType(orderParam);
		if(temp.size()>0&&temp!=null) {
			data.setSuccess(true);
			data.setData(temp);
			data.setTotal(orderMapper.queryOrderByUserTypeCount(orderParam));
		}else {
			data.setData(new ArrayList<OrderListVO>());
			data.setSuccess(true);
		}
		
		return data;
	}



	@Override
	public CommonResponse orderAllocationToUser(OrderAllocationVO orderAllocationVO) {
		CommonResponse common = new CommonResponse();
		List<String> orderList = orderAllocationVO.getOrderNo();
		for(String orderNo:orderList){
			String sql = "select * from t_order where order_no = '%s'";
			List<Order> list = orderSqlService.queryT(String.format(sql, orderNo), Order.class);
			if(null == list || list.size()<1){
				continue;
			}
			Order order = list.get(0);
			if(!(OrderStatus.AUDIT.getCode().equals(order.getOrderStatus())) || 1!=order.getWhoAudit()){
				continue;
			}
			if(StringUtils.isNotBlank(order.getAssignId())){
				continue;
			}
			String updateSql= "update t_order set assign_id='%s',assign_name='%s',assign_time=now() where order_no='%s' and order_status='%s' and who_audit='%s'";
			String updSql = String.format(updateSql, orderAllocationVO.getUserId(),orderAllocationVO.getUserName(),orderNo,OrderStatus.AUDIT.getCode(),1);
			orderSqlService.updateSql(updSql);
		}
		
		return common.success("订单分配成功");
	} 

	

	@Override
	public UserPicture queryUserPicture(OrderDetialsVO orderDetials) {
		return new UserPicture();
	}

	

}
