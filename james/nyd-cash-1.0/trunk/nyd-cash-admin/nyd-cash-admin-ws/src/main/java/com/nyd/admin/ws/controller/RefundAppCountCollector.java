package com.nyd.admin.ws.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.admin.model.Info.RefundAppCountRequest;
import com.nyd.admin.service.RefundAppCountService;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@RestController
@RequestMapping("/admin")
public class RefundAppCountCollector {

	@Autowired
	RefundAppCountService refundAppCountService;

    @RequestMapping(value="/refund/appCountQuery", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData channelConfigQuery(@RequestBody RefundAppCountRequest req ) {
    	return refundAppCountService.query(req);
    }
}
