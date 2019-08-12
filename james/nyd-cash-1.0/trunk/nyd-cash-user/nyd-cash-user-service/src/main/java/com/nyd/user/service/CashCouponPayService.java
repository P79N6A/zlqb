package com.nyd.user.service;

import com.nyd.user.model.PayByCashCouponInfo;
import com.tasfe.framework.support.model.ResponseData;

public interface CashCouponPayService {

    ResponseData handleByCashCoupon(PayByCashCouponInfo payByCashCouponInfo);
}
