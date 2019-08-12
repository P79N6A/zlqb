package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.order.api.TestStatusContract;
import com.nyd.user.dao.*;
import com.nyd.user.entity.Account;
import com.nyd.user.entity.UnbindUser;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.InnerTestDto;
import com.nyd.user.service.AccountInfoService;
import com.nyd.user.service.InnerTestUserService;
import com.nyd.user.service.util.RandomUtil;
import com.tasfe.framework.redis.RedisService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by hwei on 2018/11/12.
 */
@Service
public class InnerTestUserServiceImpl implements InnerTestUserService {
    private static Logger LOGGER = LoggerFactory.getLogger(InnerTestUserServiceImpl.class);
    private static String PRETEST = "test";
    private static final String ACCOUNT_CACHE_PREFIX = "nyd:account";

    @Autowired(required = false)
    private TestStatusContract testStatusContract;

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private PasswordDao passwordDao;
    @Autowired
    private UserSourceDao userSourceDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDetailDao userDetailDao;
    @Autowired
    private UnbindUserDao unbindUserDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AccountInfoService accountInfoService;


    @Override
    public ResponseData removeInnerTestUser(InnerTestDto innerTestDto) {
        LOGGER.info("begin to removeInnerTestUser,request param is "+ JSON.toJSONString(innerTestDto));
        ResponseData responseData;
        if (innerTestDto==null|| StringUtils.isBlank(innerTestDto.getMobile())) {
            responseData = ResponseData.error("手机号不能为空");
            return responseData;
        }
        try {
            responseData = testStatusContract.removeInnerTest(innerTestDto.getMobile());
            LOGGER.info("removeInnerTestUser result is "+JSON.toJSONString(responseData));
        } catch (Exception e) {
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @Override
    public ResponseData unBindTestUser(InnerTestDto innerTestDto) {
        LOGGER.info("begin to unBindTestUser,request param is "+JSON.toJSONString(innerTestDto));
        ResponseData responseData;
        if (innerTestDto==null|| StringUtils.isBlank(innerTestDto.getMobile())) {
            responseData = ResponseData.error("手机号不能为空");
            return responseData;
        }
        //验证手机号是否是测试手机号
//        boolean isTest = false;
//        try {
//            ResponseData<Boolean> testResponseData = testStatusContract.judgeTestUserFlag(innerTestDto.getMobile());
//            if (testResponseData!=null && "0".equals(testResponseData.getStatus())) {
//                isTest = testResponseData.getData();
//            }
//        } catch (Exception e) {
//            LOGGER.error("judgeTestUserFlag has exception !",e);
//            responseData = ResponseData.error("服务器开小差了");
//            return responseData;
//        }
//        //不存在就保存该信息；
//        if(!isTest){
//            try {
//                ResponseData<Boolean> testResponseData1 = testStatusContract.saveInnerTest(innerTestDto.getMobile());
//                if (testResponseData1!=null) {
//                    isTest = testResponseData1.getData();
//                }
//            } catch (Exception e) {
//                LOGGER.error("saveInnerTest has exception !",e);
//            }
//        }
//        if (isTest) { //是测试手机号
            //
            try {
                List<Account> list = accountDao.getAccountsByAccountNumber(innerTestDto.getMobile());
                if (list!=null&&list.size()>0) {
                    Account account = list.get(0);
                    if (account!=null&&StringUtils.isNotBlank(account.getUserId())) {
                        //解绑操作
                        responseData = doUnBindTestUser(innerTestDto.getMobile(),account.getUserId());
                        try {
                            redisService.remove(innerTestDto.getMobile(), 3);
                            redisService.remove(ACCOUNT_CACHE_PREFIX+":"+innerTestDto.getMobile(),3);
                            removeOtherToken(innerTestDto.getMobile());
                        } catch (Exception e) {
                            LOGGER.error("login redis remove error!");
                        }
                    } else {
                        responseData = ResponseData.error("手机号是新号,不需要解绑");
                    }
                } else {
                    responseData = ResponseData.error("手机号未注册");
                }
            } catch (Exception e) {
                LOGGER.error("getAccountsByAccountNumber has exception !",e);
                responseData = ResponseData.error("服务器开小差了");
            }
//        } else { //不是测试手机号则直接返回
//            responseData = ResponseData.error("不是测试手机号,不能解绑");
//        }
        LOGGER.info("unBindTestUser result is "+JSON.toJSONString(responseData));
        return responseData;
    }

    //解绑操作
    @Transactional(rollbackFor = Exception.class)
    public ResponseData doUnBindTestUser(String mobile, String userId) throws Exception{
        if (StringUtils.isBlank(mobile)||StringUtils.isBlank(userId)) {
            ResponseData responseData = ResponseData.error("参数不能为空");
            return  responseData;
        }
        String updateMobile = PRETEST+ RandomUtil.getRandom(7);
        String updateIdNumber = PRETEST+ RandomUtil.getRandom(14);
        String idNumber = "";
        try {
            List<UserInfo> userInfoList = userDao.getUsersByUserId(userId);
            if (userInfoList!=null&&userInfoList.size()>0) {
                UserInfo userInfo = userInfoList.get(0);
                if (userInfo!=null) {
                    idNumber = userInfo.getIdNumber();
                }
            }
        } catch (Exception e) {
            LOGGER.error("getUsersByUserId",e);
            return ResponseData.error("服务器开小差了");
        }
        if (StringUtils.isBlank(idNumber)) {
            return ResponseData.error("之前无身份证");
        }
        try {
            accountDao.updateAccountTest(mobile,updateMobile);
            passwordDao.updatePassWordTest(mobile,updateMobile);
            userSourceDao.updateUserSourceTest(mobile,updateMobile);
            userDao.updateUserTest(idNumber,updateIdNumber);
            userDetailDao.updateUserDetailTest(idNumber,updateIdNumber);
            //保存更改记录至解绑记录表
            UnbindUser unbindUser = new UnbindUser();
            unbindUser.setOriginMobile(mobile);
            unbindUser.setOriginIdNumber(idNumber);
            unbindUser.setUserId(userId);
            unbindUser.setDiscardMobile(updateMobile);
            unbindUser.setDiscardIdNumber(updateIdNumber);
            unbindUserDao.save(unbindUser);
            ResponseData responseData = ResponseData.success();
            responseData.setData(unbindUser);
            return responseData;
        } catch (Exception e) {
            LOGGER.error("doUnBindTestUser has exception",e);
            throw e;
        }
    }

    private void removeOtherToken(String accountNumber) {
        String pfx = accountInfoService.getLoginPrefix(accountNumber);
        if(pfx != null && pfx != "" && accountNumber != null && accountNumber != "" && pfx.indexOf("login") >= 0) {
            Set<String> keys = redisTemplate.keys(pfx + "*");
            redisTemplate.delete(keys);
        }
    }
}
