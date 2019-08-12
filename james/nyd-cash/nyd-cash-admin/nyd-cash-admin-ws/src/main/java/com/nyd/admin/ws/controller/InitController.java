package com.nyd.admin.ws.controller;

import com.nyd.admin.model.InitResult;
import com.nyd.admin.model.enums.FundSourceEnum;
import com.nyd.admin.model.enums.InUseEnum;
import com.nyd.admin.model.enums.ReconciliationResultEnum;
import com.nyd.admin.model.enums.WsmReconciliationType;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Cong Yuxiang
 * 2017/11/30
 **/
@RestController
@RequestMapping("/admin/fund/init")
public class InitController {
    @RequestMapping("/fundQuery")
    @ResponseBody
    public ResponseData initFundQuery(){
        InitResult result = new InitResult();
        result.setFundSourceType(FundSourceEnum.toMap());
        result.setInUseType(InUseEnum.toMap());
        return ResponseData.success(result);
    }

    @RequestMapping("/fundCreate")
    @ResponseBody
    public ResponseData initFundCreate(){
        InitResult result = new InitResult();
        result.setInUseType(InUseEnum.toMap());
        return ResponseData.success(result);
    }

    @RequestMapping("/fundName")
    @ResponseBody
    public ResponseData initCapitalSource(){
        InitResult result = new InitResult();
        result.setFundSourceType(FundSourceEnum.toMap());
        return ResponseData.success(result);
    }

    @PostMapping("/resultStatus")
    @ResponseBody
    public ResponseData initReconciliationResult(){
        InitResult result = new InitResult();
        result.setReconciliationType(ReconciliationResultEnum.toMap());
        return ResponseData.success(result);
    }

    @PostMapping("/import")
    @ResponseBody
    public ResponseData importBill(){
        InitResult result = new InitResult();
        result.setFundSourceType(FundSourceEnum.toMap());
        result.setWsmReconciliationType(WsmReconciliationType.toMap());
        return ResponseData.success(result);
    }
}
