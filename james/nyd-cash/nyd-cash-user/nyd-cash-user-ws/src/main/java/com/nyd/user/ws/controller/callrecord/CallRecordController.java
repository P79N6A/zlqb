package com.nyd.user.ws.controller.callrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.application.api.call.CallRecordService;
import com.nyd.application.model.call.CarryBasicVO;
import com.nyd.application.model.call.CustInfoQuery;

@RestController
@RequestMapping("/testController")
public class CallRecordController {
	@Autowired
	private CallRecordService callRecordService;
	
	@RequestMapping(value = "/account/testQuery", method = RequestMethod.POST, produces = "application/json")
	public void test(){
		CustInfoQuery custInfoQuery = new CustInfoQuery();
		try {
			callRecordService.getCallRecord(custInfoQuery);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/account/saveCarryCalls", method = RequestMethod.POST, produces = "application/json")
	public void saveCarryCalls(@RequestBody CarryBasicVO carryBasicVO ){
		try {
			callRecordService.saveCarryCalls(carryBasicVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
