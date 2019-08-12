package com.nyd.capital.ws.controller;


import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.qcgz.LoanFailListRequest;
import com.nyd.capital.model.qcgz.LoanSuccessNotifyRequest;
import com.nyd.capital.service.qcgz.QcgzService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuqiu
 */
@RestController
@RequestMapping("/capital/qcgz")
public class QcgzCallbackController {
    Logger logger = LoggerFactory.getLogger(QcgzCallbackController.class);

    @Autowired
    private QcgzService qcgzService;

    @RequestMapping(value = "/callback", produces = "text/plain;charset=UTF-8")
    public String callback(@RequestBody LoanSuccessNotifyRequest request) {
        logger.info("QCGZ回调信息:" + JSON.toJSON(request));
        try {
            ResponseData responseData = qcgzService.loanSucceesNotify(request);
            if ("0".equals(responseData.getStatus())) {
                return (String) responseData.getData();
            } else {
                return responseData.getMsg();
            }
        } catch (Exception e) {
            logger.error("回调出错", e);
            return "fail";
        }

    }

    @RequestMapping(value = "/callbackForFail", produces = "text/plain;charset=UTF-8")
    public ResponseData callbackForFail(@RequestBody LoanFailListRequest request) {
        logger.info("七彩格子手动修复数据信息:" + JSON.toJSON(request));
        try {
            List<String> assetIds = request.getAssetIds();
            if (assetIds.isEmpty()) {
                return ResponseData.error("传入的参数为空");
            }
            return qcgzService.callbackForFail(request);

        } catch (Exception e) {
            logger.error("七彩格子手动修复数据", e);
            return ResponseData.error("七彩格子手动修复数据发生异常");
        }
    }

}
