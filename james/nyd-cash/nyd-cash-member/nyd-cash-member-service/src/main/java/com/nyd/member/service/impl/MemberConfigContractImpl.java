package com.nyd.member.service.impl;

import com.nyd.member.api.MemberConfigContract;
import com.nyd.member.dao.MemberConfigDao;
import com.nyd.member.model.MemberConfigModel;
import com.nyd.member.service.consts.MemberConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dengw on 2017/12/6
 */
@Service(value = "memberConfigContract")
public class MemberConfigContractImpl implements MemberConfigContract {
    private static Logger LOGGER = LoggerFactory.getLogger(MemberConfigContractImpl.class);

    @Autowired
    private MemberConfigDao memberConfigDao;

    @Override
    public ResponseData<List<MemberConfigModel>> getMemberConfig(String business) {
        LOGGER.info("begin to get memberLog, business is " + business);
        ResponseData responseData = ResponseData.success();
        try {
            List<MemberConfigModel> list = memberConfigDao.getMermberConfigByBusiness(business);
            responseData.setData(list);
            LOGGER.info("get memberLog success !");
        } catch (Exception e) {
            LOGGER.error("getMermberConfigByBusiness has except! business is "+business,e);
            responseData = ResponseData.error(MemberConsts.DB_ERROR_MSG);
            return responseData;
        }
        return responseData;
    }

    @Override
    public ResponseData<MemberConfigModel> getAsseFee() {
        LOGGER.info("begin to get memberConfig");
        ResponseData responseData = ResponseData.success();
        try {
            MemberConfigModel memberConfigModel = memberConfigDao.getMermberConfigByType("1");
            responseData.setData(memberConfigModel);
        } catch (Exception e) {
            LOGGER.error("getMermberConfigByType has exception!",e);
            responseData = ResponseData.error(MemberConsts.DB_ERROR_MSG);
        }
        return responseData;
    }
}
