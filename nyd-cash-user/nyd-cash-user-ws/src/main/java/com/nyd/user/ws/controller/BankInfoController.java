package com.nyd.user.ws.controller;

import com.nyd.user.model.BaseInfo;
import com.nyd.user.service.BankInfoService;
import com.tasfe.framework.support.model.ResponseData;
import com.alibaba.fastjson.JSON;
import com.nyd.user.model.BankInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Dengw on 17/11/1.
 * 银行卡接口
 */
@RestController
@RequestMapping("/user")
public class BankInfoController {
    private static Logger LOGGER = LoggerFactory.getLogger(BankInfoController.class);

    @Autowired
    private BankInfoService bankInfoService;

    @RequestMapping(value = "/bank/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveBank(@RequestBody BankInfo bankInfo) throws Throwable{
        ResponseData responseData = bankInfoService.saveBankInfo(bankInfo);
        return responseData;
    }
    @RequestMapping(value = "/bank/list", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getBankList(@RequestBody BankInfo bankInfo) throws Throwable{
    	LOGGER.info("查询银行卡列表请求信息：{}",JSON.toJSONString(bankInfo));
    	ResponseData responseData = bankInfoService.getBankList(bankInfo);
    	return responseData;
    }
    
    @RequestMapping(value = "/bank/xunlianList", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getBankXunlianList(@RequestBody BankInfo bankInfo) throws Throwable{
    	LOGGER.info("查询银行卡列表请求信息：{}",JSON.toJSONString(bankInfo));
    	ResponseData responseData = bankInfoService.getXunlianBankList(bankInfo);
    	return responseData;
    }

    @RequestMapping(value = "/bankCard/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getBankListNew(@RequestBody BankInfo bankInfo) throws Throwable{
        LOGGER.info("查询银行卡列表请求信息：{}",JSON.toJSONString(bankInfo));
        ResponseData responseData = bankInfoService.getBankList(bankInfo);
        return responseData;
    }


    @RequestMapping(value = "/bank-list/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchBankList(@RequestBody BaseInfo baseInfo) throws Throwable{
        if(baseInfo.getUserId() == null){
            return ResponseData.success();
        }else{
            return bankInfoService.getBankInfos(baseInfo.getUserId());
        }
    }

}
