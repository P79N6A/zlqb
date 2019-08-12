package com.zhiwang.zfm.controller.webapp.orderApportion;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.nyd.order.api.zzl.OrderForSLHServise;
import com.nyd.order.model.JudgePeople;
import com.nyd.order.model.order.OrderListVO;
import com.nyd.user.api.zzl.UserForSLHServise;
import com.nyd.user.model.t.UserInfo;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.entity.sys.User;
import com.zhiwang.zfm.service.api.sys.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/sys/order")
@Api(description = "后台订单分配")
public class OrderController {

	
	
	@Autowired
	private OrderForSLHServise orderForSLHServise;
	@Autowired
	private UserForSLHServise userForSLHServise;
	@Autowired
	private UserService<User> userService;
	/**
     * 信审状态且未分配信审人员的订单查询
     * @param orderParam
     * @return ResponseData
     */
	@SuppressWarnings("unchecked")
	@PostMapping("/orderApportionAndUndistributed")
	@ApiOperation(value = "信审状态且未分配信审人员的订单查询")
	@Produces(value = MediaType.APPLICATION_JSON)
    public com.nyd.order.model.common.PagedResponse<List<OrderListVO>> orderApportionAndUndistributed(@RequestBody com.nyd.order.model.order.OrderParamVO orderParam) throws Throwable {
		if(null != orderParam){
			if(StringUtils.isNotBlank(orderParam.getInTimeStart())){
				orderParam.setInTimeStart(orderParam.getInTimeStart()+" 00:00:00");
			}
			if(StringUtils.isNotBlank(orderParam.getInTimeEnd())){
				orderParam.setInTimeEnd(orderParam.getInTimeEnd()+" 23:59:59");
			}
		}
		com.nyd.order.model.common.PagedResponse<List<OrderListVO>> result =orderForSLHServise.orderApportionAndUndistributed(orderParam);
        return result;
    }
	
    /**
     * 询查所有信审人员
     * @param judgePeople
     * @return ResponseData
     */
	@PostMapping("/orderjudgePeople")
	@ApiOperation(value = "询查信审人员")
	@Produces(value = MediaType.APPLICATION_JSON)
    public CommonResponse<List<JudgePeople>> orderjudgePeople() throws Throwable {
		CommonResponse<List<JudgePeople>> data = new CommonResponse<>();
		List<JSONObject> list = userService.getUserByUserRole();
		data.setSuccess(false);
		List<JudgePeople> result = new ArrayList<>();
		
		for (JSONObject jsonObject : list) {
			
			JudgePeople j = new JudgePeople();
			j.setUserId(jsonObject.getString("userId"));
			j.setUserName(jsonObject.getString("userName"));
			result.add(j);
		}
		if(result.size()>0&&result!=null) {
			data.setSuccess(true);
		}
		data.setData(result);
		
		return data;
    }
	
    /**
     * 分配信贷订单
     * @param orderAllocation
     * @return ResponseData
     */
	@PostMapping("/orderAllocationToUser")
	@ApiOperation(value = "分配信贷订单")
	@Produces(value = MediaType.APPLICATION_JSON)
    public com.nyd.order.model.common.CommonResponse orderAllocationToUser(@RequestBody com.nyd.order.model.order.OrderAllocationVO orderAllocation) throws Throwable {
		com.nyd.order.model.common.CommonResponse comm = orderForSLHServise.orderAllocationToUser(orderAllocation);
		
		return comm;
    }
	

	/**
     * 查看个人信息
     * @param orderDetials
     * @return ResponseData
     */
	@SuppressWarnings("unchecked")
	@PostMapping("/queryOrderByUser")
	@ApiOperation(value = "个人信息查看")
	@Produces(value = MediaType.APPLICATION_JSON)
    public CommonResponse<?> queryOrderByUser(@RequestBody com.nyd.order.model.order.OrderDetialsVO orderDetials) throws Throwable {
		CommonResponse data =new CommonResponse<>();
	    UserInfo info=	userForSLHServise.queryOrderByUser(orderDetials.getUserid());
	    data.setSuccess(false);
	    if(info!=null) {
			data.setData(info);
			data.setSuccess(true);	
		}
		return data;
    }
	
	/**
     * 查看认证图片
     * @param orderDetials
     * @return ResponseData
     */
	@SuppressWarnings("unchecked")
	@PostMapping("/queryUserPicture")
	@ApiOperation(value = "查看认证图片")
	@Produces(value = MediaType.APPLICATION_JSON)
    public CommonResponse<?> queryUserPicture(@RequestBody com.nyd.order.model.order.OrderDetialsVO orderDetials) throws Throwable {
		CommonResponse data =new CommonResponse<>();
		com.nyd.order.model.order.UserPicture info =orderForSLHServise.queryUserPicture(orderDetials);
		data.setData(info);
		data.setSuccess(false);
		return data;
    }
	
}
