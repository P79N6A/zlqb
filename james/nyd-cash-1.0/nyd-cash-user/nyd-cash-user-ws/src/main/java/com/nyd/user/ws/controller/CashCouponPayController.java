package com.nyd.user.ws.controller;



import com.alibaba.fastjson.JSON;
import com.nyd.user.model.PayByCashCouponInfo;
import com.nyd.user.service.CashCouponPayService;
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

/**
 * 专门处理用现金券支付的情况
 */
@RestController
@RequestMapping(value = "/user")
public class CashCouponPayController {

    private static Logger logger = LoggerFactory.getLogger("CashCouponPayController.class");

    @Autowired
    private CashCouponPayService cashCouponPayService;


    /**
     * 用现金券支付评估费（此类处理指的是账户现金券余额足够支付评估费）
     * @param PayByCashCouponInfo
     * @return
     */
    @RequestMapping(value = "/pay/cashcoupon", method = RequestMethod.POST, produces = "application/json")
    public ResponseData payByCashCoupon(@RequestBody PayByCashCouponInfo PayByCashCouponInfo){
        logger.info("用现金券支付，请求参数："+ JSON.toJSON(PayByCashCouponInfo));
        ResponseData responseData = cashCouponPayService.handleByCashCoupon(PayByCashCouponInfo);
        return  responseData;
    }
}
