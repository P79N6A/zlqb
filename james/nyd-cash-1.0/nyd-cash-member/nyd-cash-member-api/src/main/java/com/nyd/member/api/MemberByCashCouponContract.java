package com.nyd.member.api;

import com.nyd.member.model.MemberByCashCouponModel;
import com.tasfe.framework.support.model.ResponseData;

public interface MemberByCashCouponContract {

    /**
     * 每笔会员费，现金券使用情况
     */
    ResponseData saveCashCouponUserDetail(MemberByCashCouponModel memberByCashCouponModel);
}
