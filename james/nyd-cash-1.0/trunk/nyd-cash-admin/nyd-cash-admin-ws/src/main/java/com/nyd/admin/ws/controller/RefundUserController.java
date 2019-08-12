package com.nyd.admin.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.admin.model.AdminRefundUserDto;
import com.nyd.admin.service.RefundUserService;
import com.nyd.user.api.RefundUserContract;
import com.nyd.user.model.RefundUserDto;
import com.tasfe.framework.support.model.ResponseData;
@RestController
@RequestMapping("/admin")
public class RefundUserController {
	
	@Autowired
	private RefundUserContract  refundUserContract;
	@Autowired
	private RefundUserService  refundUserService;
	
	@RequestMapping(value = "/find/refundcash", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseData findRefundCash(@RequestBody RefundUserDto dto) throws Throwable{		
		ResponseData responseData  = refundUserContract.findRefundCash(dto);
		return responseData;
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseData addUser(@RequestBody RefundUserDto dto) throws Throwable{		
		ResponseData responseData  = refundUserContract.addUser(dto);
		return responseData;
	}
	
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseData updateUser(@RequestBody RefundUserDto dto) throws Throwable{		
		ResponseData responseData  = refundUserContract.updateUser(dto);
		return responseData;
	}
	
	@RequestMapping(value = "/refund/list", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseData refundUserlist(@RequestBody AdminRefundUserDto dto) throws Throwable{		
		ResponseData responseData  = refundUserService.selectRefundUser(dto);
		return responseData;
	}
	
	

}
