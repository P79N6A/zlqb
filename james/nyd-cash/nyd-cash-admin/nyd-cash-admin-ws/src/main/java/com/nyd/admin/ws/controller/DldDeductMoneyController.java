package com.nyd.admin.ws.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.admin.service.UrgentDeductMoneyService;
import com.tasfe.framework.support.model.ResponseData;

@RestController
@RequestMapping(value="/admin")
public class DldDeductMoneyController {
	
	public static Logger logger =LoggerFactory.getLogger(DldDeductMoneyController.class);
	 
	@Autowired
	private UrgentDeductMoneyService  urgentDeductMoneyService;
	
	
	@RequestMapping(value="/dealDeductMoney", method = RequestMethod.GET)
	public ResponseData dealDeductMoney() {
		logger.info("多来点二次放款的数据划扣开始");
		return urgentDeductMoneyService.dldDeductMoney();
		
		
	}

}
