package com.nyd.user.ws.controller;

import com.alibaba.fastjson.JSON;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.model.BaseInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.service.CashCouponService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chaiming
 */

@RestController
@RequestMapping(value = "/user")
public class CashCouponController {

    private static Logger logger = LoggerFactory.getLogger(CashCouponController.class);

    @Autowired
    private UserAccountContract userAccountContract;

    @Autowired
    private CashCouponService cashCouponService;



    /**
     * 我的账户接口(获取所有现金券类型和账户总余额)
     */
    @RequestMapping(value = "/myaccount/cashcoupon/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getAllCashCoupon(@RequestBody BaseInfo baseInfo) throws Throwable {
        logger.info("我的账户，请求参数："+ JSON.toJSON(baseInfo));
        ResponseData responseData = cashCouponService.getAllInformation(baseInfo.getAccountNumber());
        logger.info("获取到的所有现金券类型和账户余额："+JSON.toJSON(responseData));
        return responseData;


    }



    /**
     * 现金券余额（根据手机号码去查找）
     */
    @RequestMapping(value = "/cashcoupon/balance", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryCashCouponBalance(@RequestBody BaseInfo baseInfo){
        logger.info("获取现金券余额请求参数："+ JSON.toJSON(baseInfo));

        if (baseInfo != null){
            ResponseData<AccountDto> responseData = userAccountContract.queryBalance(baseInfo.getAccountNumber());
            logger.info("请求查询现金券余额返回结果："+JSON.toJSON(responseData));
            return responseData;
        }
        return ResponseData.error("参数不能为空");
    }

}
