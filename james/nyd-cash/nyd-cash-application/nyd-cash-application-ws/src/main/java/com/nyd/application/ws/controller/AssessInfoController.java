package com.nyd.application.ws.controller;

import com.nyd.application.service.AssessService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by hwei on 2018/5/19
 * 评估费用
 */
@RestController
@RequestMapping("/application")
public class AssessInfoController {
    @Autowired
    AssessService assessService;
    //获取评估费
    @RequestMapping(value = "/fetch/assessFee", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getAssessFee(@RequestBody Map<String, String> map){
        ResponseData responseData = assessService.getAssessInfo(map);
        return responseData;
    }
}
