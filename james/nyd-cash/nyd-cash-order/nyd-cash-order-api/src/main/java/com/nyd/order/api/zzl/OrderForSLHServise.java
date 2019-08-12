package com.nyd.order.api.zzl;

import java.util.List;

import com.nyd.order.model.JudgePeople;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.order.OrderAllocationVO;
import com.nyd.order.model.order.OrderDetialsVO;
import com.nyd.order.model.order.OrderListVO;
import com.nyd.order.model.order.OrderParamVO;
import com.nyd.order.model.order.UserInfo;
import com.nyd.order.model.order.UserPicture;




public interface OrderForSLHServise {
	
	PagedResponse<List<OrderListVO>>  orderApportionAndUndistributed(OrderParamVO orderParam);
	CommonResponse  orderAllocationToUser(OrderAllocationVO orderAllocationVO);
     UserPicture  queryUserPicture(OrderDetialsVO orderDetials);
     
}
