package com.nyd.capital.ws.controller;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.qcgz.QueryLoanApplyResultRequest;
import com.nyd.capital.service.qcgz.QcgzService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuqiu
 */
@RestController
@RequestMapping("/nyd/capital/qcgz")
public class QcgzController {
    Logger logger = LoggerFactory.getLogger(QcgzController.class);
    @Autowired
    private QcgzService qcgzService;

    @RequestMapping(value = "/queryLoanApplyResult" ,produces="text/plain;charset=UTF-8")
    public ResponseData queryLoanApplyResult(@RequestBody QueryLoanApplyResultRequest request){
        request.setChannelCode("NYD");
        logger.info("查询七彩格子的放款状态："+JSON.toJSONString(request));
        return qcgzService.queryLoanApplyResult(request);
    }
}
