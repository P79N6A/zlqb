package com.nyd.order.service;


import com.nyd.order.api.TestStatusContract;
import com.nyd.order.dao.TestListDao;
import com.nyd.order.entity.InnerTest;
import com.nyd.order.model.InnerTestInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by hwei on 2018/11/26.
 */
@Service(value = "testStatusContractNyd")
public class TestStatusContractImpl implements TestStatusContract {
    private static Logger LOGGER = LoggerFactory.getLogger(TestStatusContractImpl.class);
    @Autowired
    private TestListDao testListDao;

    @Override
    public ResponseData<Boolean> judgeTestUserFlag(String mobile) {
        ResponseData responseData = ResponseData.success();
        LOGGER.info("begin to judge testUser Flag, mobile is " + mobile);
        boolean testFlag = false;
        try {
            List<InnerTest> list = testListDao.getObjectsByMobile(mobile);
            if(list != null && list.size()>0){
                testFlag = true;
            }
        } catch (Exception e) {
            LOGGER.error("get innerTest list error! mobile = " + mobile, e);
        }
        LOGGER.info("judge testUser Flag result is" + testFlag);
        responseData.setData(testFlag);
        return responseData;
    }

    @Override
    public ResponseData removeInnerTest(String mobile) {
        ResponseData responseData = ResponseData.success();
        if (StringUtils.isBlank(mobile)) {
            responseData = ResponseData.error("手机号不能为空");
            return responseData;
        }
        try {
            List<InnerTest> list = testListDao.getObjectsByMobile(mobile);
            if (list==null||list.size()==0) {
                responseData.setData("手机号已移除");
                return responseData;
            }
            //手机号还在则进行更新操作
            InnerTestInfo innerTestInfo = new InnerTestInfo();
            innerTestInfo.setMobile(mobile);
            innerTestInfo.setDeleteFlag(1);
            innerTestInfo.setIsInUse(1);
            testListDao.update(innerTestInfo);
        } catch (Exception e) {
            LOGGER.error("get innerTest list error! mobile = " + mobile, e);
            responseData = ResponseData.error("服务开小差了");
        }
        return responseData;
    }

    @Override
    public ResponseData<Boolean> saveInnerTest(String mobile) {
        ResponseData responseData = ResponseData.success();
        LOGGER.info("begin to saveInnerTest, mobile is " + mobile);
        InnerTestInfo innerTestInfo = new InnerTestInfo();
        innerTestInfo.setMobile(mobile);
        innerTestInfo.setRealName(mobile);
        boolean testFlag = true;
        try {
            testListDao.save(innerTestInfo);
        } catch (Exception e) {
            testFlag = false;
            LOGGER.error("begin to saveInnerTest, mobile = " + mobile, e);
        }
        LOGGER.info("begin to saveInnerTest, is" + testFlag);
        responseData.setData(testFlag);
        return responseData;
    }
}
