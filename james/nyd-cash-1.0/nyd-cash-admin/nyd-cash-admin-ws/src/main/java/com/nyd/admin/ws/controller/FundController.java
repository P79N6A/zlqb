package com.nyd.admin.ws.controller;

import com.nyd.admin.model.FundInfo;
import com.nyd.admin.model.FundInfoQueryVo;
import com.nyd.admin.service.IFundService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Cong Yuxiang
 * 2017/11/30
 **/
@RestController
@RequestMapping("/admin/fund")
public class FundController {
    @Autowired
    private IFundService fundService;

    @PostMapping("/save")
    public ResponseData save(@RequestBody FundInfo fundInfo){
        boolean flag = fundService.saveFundIno(fundInfo);
        if(flag) {
            return ResponseData.success();
        }else {
            return ResponseData.error();
        }
    }

    @PostMapping("/query")
    @ResponseBody
    public ResponseData query(@RequestBody FundInfoQueryVo vo){

        return ResponseData.success(fundService.queryByCondition(vo));
    }
}
