package com.nyd.admin.ws.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.model.AdminRefundInfo;
import com.nyd.admin.service.RefundService;
import com.nyd.user.model.RefundInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@RestController
@RequestMapping("/admin")
public class RefundCollector {
    private static Logger LOGGER = LoggerFactory.getLogger(RefundCollector.class);

	@Autowired
	RefundService refundService;

	@RequestMapping(value = "/refund/audit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData audit(@RequestBody RefundInfo req, HttpServletRequest request) {
		String operatorPerson = request.getHeader("accountNo");
		req.setUpdateBy(operatorPerson);
		LOGGER.info("退款审批请求信息：" + JSON.toJSONString(req));
		return refundService.audit(req);
	}

	@RequestMapping(value = "/refund/Query", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData refundQuery(@RequestBody AdminRefundInfo req) {
		return refundService.query(req);
	}
	@RequestMapping(value = "/refund/detail", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData refundDetail(@RequestBody RefundInfo req) {
		return refundService.getRefundDetail(req);
	}
}
