package com.nyd.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.nyd.activity.api.CouponContract;
import com.nyd.activity.model.vo.CouponLogInfoVo;
import com.nyd.application.model.AssessModel;
import com.nyd.application.service.AssessService;
import com.nyd.application.service.consts.ApplicationConsts;
import com.nyd.member.api.MemberConfigContract;
import com.nyd.member.model.MemberConfigModel;
import com.nyd.member.model.WZResponse;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserStepContract;
import com.nyd.user.model.StepInfo;
import com.nyd.user.model.dto.AccountDto;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dengw on 2018/2/25
 */
@Service
public class AssessServiceImpl implements AssessService {
    private static Logger LOGGER = LoggerFactory.getLogger(AssessServiceImpl.class);

    @Autowired(required = false)
    MemberConfigContract memberConfigContract;
    @Autowired(required = false)
    private CouponContract couponContract;
    @Autowired(required = false)
    UserStepContract userStepContractNyd;
    @Autowired
    private UserAccountContract userAccountContract;

    @Override
    public ResponseData getAssessInfo(Map<String, String> map) {
        ResponseData responseData = ResponseData.success();
        LOGGER.info("begin to get assessInfo !");
        try {
            AssessModel model = new AssessModel();
            //获取评估费信息
            ResponseData<MemberConfigModel> assResponse = memberConfigContract.getAsseFee();
            if (assResponse!=null&&"0".equals(assResponse.getStatus())) {
                MemberConfigModel memberConfigModel = assResponse.getData();
                if (memberConfigModel!=null) {
                    model.setAssessFee(memberConfigModel.getDiscountFee());
                    model.setRealFee(memberConfigModel.getRealFee());
                    model.setType(memberConfigModel.getType());
                }
            }
            //获取评估分等级
            ResponseData<StepInfo> stepInfoResponseData = null;
            String preAuditLevel = null;
            try{
                StepInfo stepInfo;
                stepInfoResponseData = userStepContractNyd.getUserStep(map.get("userId") );
                if("0".equals(stepInfoResponseData.getStatus())){
                    stepInfo = stepInfoResponseData.getData();
                    preAuditLevel = stepInfo.getPreAuditLevel();
                }
            }catch (Exception e){
                    LOGGER.error("dobbo get preAuditLevel info error",e);
            }

            //优惠券列表
            ResponseData<Integer> couponResponse = couponContract.getCouponNumberByAccountNumber(map.get("accountNumber"));
            if ("0".equals(couponResponse.getStatus())) {
                model.setCouponCount(couponResponse.getData());
            }
            model.setPreAuditLevel(preAuditLevel);
            model.setMobile(map.get("accountNumber"));


            /**
             * 账户总余额
             */
            try {
                ResponseData<AccountDto> responseResult = userAccountContract.queryBalance(map.get("accountNumber"));
                LOGGER.info("用户account相关参数："+ JSON.toJSON(responseResult));
                String totalBalance = "";
                Gson gson = new Gson();
                if ("success".equals(responseResult.getMsg())){
                    WZResponse response = gson.fromJson(gson.toJson(responseResult), WZResponse.class);
                    WZResponse.Data data = response.getData();
                    totalBalance = data.getTotalBalance();
                    LOGGER.info("账户总余额:"+totalBalance);
                }
                model.setTotalBalance(totalBalance);

            }catch (Exception e){
                LOGGER.error("查询账户余额出错！",e);
                responseData = ResponseData.error();
                return responseData;
            }


            responseData.setData(model);
            LOGGER.info("get assessInfo success !" + model);
        } catch (Exception e) {
            LOGGER.error("get assess info error",e);
            return ResponseData.error(ApplicationConsts.ERROR_MSG);
        }
        return responseData;
    }
}
