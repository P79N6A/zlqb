package com.nyd.zeus.ws.controller;

import com.nyd.zeus.model.BillDetail;
import com.nyd.zeus.service.BillService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhujx on 2017/11/18.
 * 还款计划，账单接口
 */
@RestController
@RequestMapping("/zeus")
public class BillInfoController {

    private static Logger LOGGER = LoggerFactory.getLogger(BillInfoController.class);

    @Autowired
    BillService billService;

    /**
     * 还款计划
     * @param billDetail
     * @return
     */
    @RequestMapping(value = "/bill-list", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getBillInfoLs(@RequestBody BillDetail billDetail){
        ResponseData responseData = billService.getBillInfoLs(billDetail.getUserId());
        return responseData;
    }

    @RequestMapping(value = "/bill/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getBillInfoLsNew(@RequestBody BillDetail billDetail){
        ResponseData responseData = billService.getBillInfoLs(billDetail.getUserId());
        return responseData;
    }

    /**
     * 立即还款页面
     * @param billDetail
     * @return
     */
    @RequestMapping(value = "/bill", method = RequestMethod.POST, produces = "application/json")
    public ResponseData save(@RequestBody BillDetail billDetail){
        ResponseData responseData = billService.getBillDetailInfo(billDetail.getBillNo());
        return responseData;
    }

    /**
     * 合利宝还款页面
     * @param
     * @return
     */
    @RequestMapping(value = "/bill/helibao", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryHlb(@RequestBody BillDetail billDetail){
        ResponseData responseData = billService.queryHlb(billDetail);
        return responseData;
    }

}
