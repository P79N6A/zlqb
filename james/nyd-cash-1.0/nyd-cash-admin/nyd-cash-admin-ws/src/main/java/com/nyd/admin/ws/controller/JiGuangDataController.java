package com.nyd.admin.ws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.model.dto.JiGuangParamDto;
import com.nyd.admin.service.JiGuangDataService;
import com.tasfe.framework.support.model.ResponseData;
/**
 * 极光推送controller
 * @author chengjf
 *
 */
@RestController
@RequestMapping("/admin")
public class JiGuangDataController {
	
    private static Logger logger = LoggerFactory.getLogger(JiGuangDataController.class);
    
    @Autowired
    private JiGuangDataService jiGuangDataService;
    
    @RequestMapping(value="/find/jiGuang",method=RequestMethod.POST,produces ="application/json")
    public ResponseData JiGuanPush(@RequestBody JiGuangParamDto dto) {
    	logger.info("极光推送技术中心传入参数{}",JSON.toJSON(dto));
    	return jiGuangDataService.gainJiGuangData(dto);  	 
    }


}
