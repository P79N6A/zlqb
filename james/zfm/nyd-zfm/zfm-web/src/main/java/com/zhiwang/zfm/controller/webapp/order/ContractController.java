package com.zhiwang.zfm.controller.webapp.order;

import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.nyd.order.api.zzl.OrderForZQServise;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.vo.CenerateContractVo;
import com.nyd.order.model.vo.OrderlistVo;
import com.nyd.user.api.zzl.UserForZQServise;

@RestController
@RequestMapping("/web/order")
@Api(description="放款")
public class ContractController {

	@Autowired
	private OrderForZQServise orderForZQServise;
	
	@Autowired
	private UserForZQServise userForZQServise;
	
	@PostMapping("/generateCrmPayControl")
	@ApiOperation(value = "生成还款明细")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> generateCrmPayControl(String orderNo,String userId) {
		CommonResponse<JSONObject> pageResponse = new CommonResponse<JSONObject>();
		try {
			//查询银行卡信息
			JSONObject bankInfo = userForZQServise.getAccountInfo(userId);
			pageResponse = orderForZQServise.generateCrmPayControl(orderNo,bankInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageResponse;
	}
	
	
	@PostMapping("/generateContract")
	@ApiOperation(value = "生成合同")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<Object> generateContract(CenerateContractVo vo) {
		CommonResponse<Object> pageResponse = new CommonResponse<Object>();
		try {
			pageResponse = orderForZQServise.generateContract(vo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageResponse;
	}
}
