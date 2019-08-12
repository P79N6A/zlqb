package com.nyd.application.ws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.nyd.application.model.request.AgreeMentQueryModel;
import com.nyd.application.model.request.AgreementTemplateModel;
import com.nyd.application.service.AgreeMentService;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2017/11/25.
 * 协议
 */
@RestController
@RequestMapping("/application")
public class AgreeMentController {
    private static Logger LOGGER = LoggerFactory.getLogger(AgreeMentController.class);

    @Autowired
    AgreeMentService agreeMentService;
    
    //上传模板
    @RequestMapping(value = "/uploadAgreement", method = RequestMethod.POST, produces = "application/json")
    public ResponseData uploadAgreement(@RequestBody AgreementTemplateModel agreementTemplateModel){
        try {
            return agreeMentService.uploadAgreemengt(agreementTemplateModel);
        } catch (Exception e) {
            LOGGER.error("AgreeMentController uploadAgreement has except!",e);
            return ResponseData.error();
        }
    }

    //查看协议
    @RequestMapping(value = "/agreement", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryAgreement(@RequestBody AgreeMentQueryModel agreeMentQueryModel){
        LOGGER.info("begin to queryAgreement! request model is "+agreeMentQueryModel.toString());
        if (agreeMentQueryModel==null) {
            return ResponseData.success();
        }
        try {
            return agreeMentService.getSignAgreement(agreeMentQueryModel.getUserId(),agreeMentQueryModel.getOrderId());
        } catch (Exception e) {
            LOGGER.error(String.format("AgreeMentController queryAgreement has except!userId is {} , orderId is {}",agreeMentQueryModel.getUserId(),agreeMentQueryModel.getOrderId()),e);
            return ResponseData.error();
        }
    }

    //查看协议
    @RequestMapping(value = "/agreement/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryAgreementNew(@RequestBody AgreeMentQueryModel agreeMentQueryModel){
        LOGGER.info("begin to queryAgreement! request model is "+agreeMentQueryModel.toString());
        if (agreeMentQueryModel==null) {
            return ResponseData.success();
        }
        try {
            return agreeMentService.getSignAgreement(agreeMentQueryModel.getUserId(),agreeMentQueryModel.getOrderId());
        } catch (Exception e) {
            LOGGER.error(String.format("AgreeMentController queryAgreement has except!userId is {} , orderId is {}",agreeMentQueryModel.getUserId(),agreeMentQueryModel.getOrderId()),e);
            return ResponseData.error();
        }
    }
    @RequestMapping(value = "/signRegisterAgreeMent/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getSignRegisterAgreeMent(JSONObject json) {
    	ResponseData data =new ResponseData<>();
    	try {
    		 data = agreeMentService.signRegisterAgreeMent(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
    	
    }
}
