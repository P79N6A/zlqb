package com.nyd.admin.ws.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.admin.model.Info.RefundAppCountRequest;
import com.nyd.admin.service.RefundAppCountService;
import com.nyd.admin.service.RefundAppService;
import com.nyd.user.model.RefundAmountInfo;
import com.nyd.user.model.RefundAppInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@RestController
@RequestMapping("/admin")
public class RefundAppCollector {

	@Autowired
	RefundAppService refundAppService;

	@RequestMapping(value = "/refund/app/updateOrSave", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseData updateOrSave(@RequestBody RefundAppInfo req) {
		if (StringUtils.isBlank(req.getAppCode())) {
			return refundAppService.save(req);
		} else {
			return refundAppService.update(req);
		}
	}

	@RequestMapping(value = "/refund/appQuery", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData refundAppQuery(@RequestBody RefundAppInfo req) {
		return refundAppService.query(req);
	}
}
