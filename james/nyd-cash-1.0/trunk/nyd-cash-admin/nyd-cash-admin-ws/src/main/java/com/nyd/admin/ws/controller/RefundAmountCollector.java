package com.nyd.admin.ws.controller;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.admin.model.AdminRefundAmountInfo;
import com.nyd.admin.service.RefundAmountService;
import com.nyd.user.model.RefundAmountInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@RestController
@RequestMapping("/admin")
public class RefundAmountCollector {

	@Autowired
	RefundAmountService refundAmountService;

    @RequestMapping("/refund/amountQuery")
    @ResponseBody
    public ResponseData getBankList(@RequestBody AdminRefundAmountInfo req) {
    	return refundAmountService.query(req);
    }
    @RequestMapping(value="/refund/amount/updateOrSave", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData updateOrSave(@RequestBody RefundAmountInfo req) {
    	if(StringUtils.isBlank(req.getAmountCode())) {
    		return refundAmountService.save(req);
    	}else {
    		return refundAmountService.update(req);
    	}
    }
}
