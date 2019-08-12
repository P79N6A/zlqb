package com.nyd.member.service.impl;

import com.nyd.member.api.MemberByCashCouponContract;
import com.nyd.member.dao.MemberByCashCouponDao;
import com.nyd.member.entity.MemberByCashCoupon;
import com.nyd.member.model.MemberByCashCouponModel;
import com.nyd.member.service.consts.MemberConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberByCashCouponContractImpl implements MemberByCashCouponContract {
    private static Logger LOGGER = LoggerFactory.getLogger(MemberByCashCouponContractImpl.class);

    @Autowired
    private MemberByCashCouponDao memberByCashCouponDao;

    @Override
    public ResponseData saveCashCouponUserDetail(MemberByCashCouponModel memberByCashCouponModel) {
        LOGGER.info("begin to save memberByCashCouponModel, userId is " + memberByCashCouponModel.getUserId());
        ResponseData responseData = ResponseData.success();
        MemberByCashCoupon memberByCashCoupon = new MemberByCashCoupon();
        BeanUtils.copyProperties(memberByCashCouponModel,memberByCashCoupon);
        memberByCashCoupon.setMobile(null);
        try {
            memberByCashCouponDao.save(memberByCashCoupon);
            LOGGER.info("save memberByCashCoupon success !");
        } catch (Exception e) {
            LOGGER.error("save memberByCashCoupon has exception! userId is"+memberByCashCouponModel.getUserId(),e);
            return ResponseData.error(MemberConsts.DB_ERROR_MSG);
        }
        return responseData;
    }
}
