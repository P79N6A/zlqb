package com.nyd.user.ws.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.nyd.user.entity.RefundApply;
import com.nyd.user.model.RefundApplyInfo;
import com.nyd.user.model.UserBindCardConfirm;
import com.nyd.user.model.UserBindCardReq;
import com.nyd.user.model.enums.RefundApplyCode;
import com.nyd.user.service.RefundApplyService;
import com.tasfe.framework.support.model.ResponseData;


/**
 * 
 * @author zhangdk
 *
 */
@RestController
@RequestMapping("/user")
public class RefundApplyController {
    private static Logger LOGGER = LoggerFactory.getLogger(RefundApplyController.class);

    @Autowired
    private RefundApplyService refundApplyService;

    @RequestMapping(value = "/refund/apply/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData refundApply(@RequestBody RefundApplyInfo req) throws Throwable{
    	if(req.getTypeCode() == null) {
    		return ResponseData.error("请求参数异常，请确认！");
    	}
    	if(req.getTypeCode().equals(RefundApplyCode.COMP.getCode())) {
    		if(StringUtils.isEmpty(req.getAccountNumber()) || StringUtils.isEmpty(req.getMessage()) ) {
    			return ResponseData.error("请求参数异常，请确认！");
    		}
    		return refundApplyService.apply(req);
    	}
    	if(StringUtils.isEmpty(req.getTypeCode()) || StringUtils.isEmpty(req.getIdCard()) || StringUtils.isEmpty(req.getTrackNo()) || StringUtils.isEmpty(req.getUserName()) || StringUtils.isEmpty(req.getUserId())) {
			return ResponseData.error("请求参数异常，请确认！");
		}
        return refundApplyService.apply(req);
    }

	@RequestMapping(value = "/refund/apply/query", method = RequestMethod.POST, produces = "application/json")
	public ResponseData refundApplyNew(@RequestBody RefundApplyInfo req) throws Throwable{
		if(req.getTypeCode() == null) {
			return ResponseData.error("请求参数异常，请确认！");
		}
		if(req.getTypeCode().equals(RefundApplyCode.COMP.getCode())) {
			if(StringUtils.isEmpty(req.getAccountNumber()) || StringUtils.isEmpty(req.getMessage()) ) {
				return ResponseData.error("请求参数异常，请确认！");
			}
			return refundApplyService.apply(req);
		}
		if(StringUtils.isEmpty(req.getTypeCode()) || StringUtils.isEmpty(req.getIdCard()) || StringUtils.isEmpty(req.getTrackNo()) || StringUtils.isEmpty(req.getUserName()) || StringUtils.isEmpty(req.getUserId())) {
			return ResponseData.error("请求参数异常，请确认！");
		}
		return refundApplyService.apply(req);
	}

}
