package com.nyd.capital.service.pocket.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.model.enums.PocketTxCodeEnum;
import com.nyd.capital.model.pocket.*;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.pocket.service.Pocket2Service;
import com.nyd.capital.service.pocket.util.DesHelper;
import com.nyd.capital.service.pocket.util.KdaiSignUtils;
import com.nyd.capital.service.pocket.util.PocketConfig;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author liuqiu
 */
@Service
public class Pocket2ServiceImpl implements Pocket2Service {

    private static Logger logger = LoggerFactory.getLogger(Pocket2ServiceImpl.class);


    @Autowired
    private PocketConfig pocketConfig;

    @Override
    public ResponseData queryAccountOpenDetailByMobile(PocketQueryAccountOpenDetailByMobileDto dto) {
        logger.info("begin query account open detail by mobile,and param is:" + dto.toString());
        try {
            return getResponseData(JSON.toJSONString(dto), PocketTxCodeEnum.queryAccountOpenDetailByMobile.getCode());
        } catch (Exception e) {
            logger.info("query account open detail by mobile has exception,and param is:" + dto.toString(), e);
            return ResponseData.error("query account open detail by mobile has exception");
        }
    }

    @Override
    public ResponseData queryAccountOpenDetail(PocketQueryAccountOpenDetailDto dto) {
        logger.info("begin query account open detail by idNumber,and param is:" + dto.toString());
        try {
            return getResponseData(JSON.toJSONString(dto), PocketTxCodeEnum.queryAccountOpenDetail.getCode());
        } catch (Exception e) {
            logger.info("query account open detail by idNumber has exception,and param is:" + dto.toString(), e);
            return ResponseData.error("query account open detail by idNumber has exception");
        }
    }

    @Override
    public ResponseData complianceBorrowPage(PocketComplianceBorrowPageDto dto) {
        logger.info("begin compliance borrow page,and param is:" + dto.toString());
        try {
            return getResponseData(JSON.toJSONString(dto), PocketTxCodeEnum.complianceBorrowPage.getCode());
        } catch (Exception e) {
            logger.info("compliance borrow page has exception,and param is:" + dto.toString(), e);
            return ResponseData.error("compliance borrow page has exception");
        }
    }

    @Override
    public ResponseData passwordResetPage(PocketPasswordResetPageDto dto) {
        logger.info("begin password reset page,and param is:" + dto.toString());
        try {
            return getResponseData(JSON.toJSONString(dto), PocketTxCodeEnum.passwordResetPage.getCode());
        } catch (Exception e) {
            logger.info("password reset page has exception,and param is:" + dto.toString(), e);
            return ResponseData.error("password reset page has exception");
        }
    }

    @Override
    public ResponseData accountOpenEncryptPage(PocketAccountOpenEncryptPageDto dto) {
        logger.info("begin account open encrypt page,and param is:" + dto.toString());
        try {
            return getResponseData(JSON.toJSONString(dto), PocketTxCodeEnum.accountOpenEncryptPage.getCode());
        } catch (Exception e) {
            logger.info("account open encrypt page has exception,and param is:" + dto.toString(), e);
            return ResponseData.error("account open encrypt page has exception");
        }
    }

    @Override
    public ResponseData termsAuthPage(PocketTermsAuthPageDto dto) {
        logger.info("begin terms auth page,and param is:" + dto.toString());
        try {
            return getResponseData(JSON.toJSONString(dto), PocketTxCodeEnum.termsAuthPage.getCode());
        } catch (Exception e) {
            logger.info("terms auth page has exception,and param is:" + dto.toString(), e);
            return ResponseData.error("terms auth page has exception");
        }
    }

    @Override
    public ResponseData withdraw(PocketWithdrawDto dto) {
        logger.info("begin withdraw,and param is:" + dto.toString());
        try {
            return getResponseData(JSON.toJSONString(dto), PocketTxCodeEnum.withdraw.getCode());
        } catch (Exception e) {
            logger.info("withdraw has exception,and param is:" + dto.toString(), e);
            return ResponseData.error("withdraw has exception");
        }
    }

    @Override
    public ResponseData queryOrderWithdrawStatus(PocketQueryOrderWithdrawStatusDto dto) {
        logger.info("begin queryOrderWithdrawStatus,and param is:" + dto.toString());
        try {
            return getResponseData(JSON.toJSONString(dto), PocketTxCodeEnum.queryOrderWithdrawStatus.getCode());
        } catch (Exception e) {
            logger.info("queryOrderWithdrawStatus has exception,and param is:" + dto.toString(), e);
            return ResponseData.error("queryOrderWithdrawStatus has exception");
        }
    }

    @Override
    public ResponseData createOrderLendPay(PocketCreateOrderLendPayDto dto) {
        logger.info("begin create order lend pay,and param is:" + dto.toString());
        try {
            return getResponseData(JSON.toJSONString(dto), PocketTxCodeEnum.createOrderLendPay.getCode());
        } catch (Exception e) {
            logger.info("create order lend pay has exception,and param is:" + dto.toString(), e);
            return ResponseData.error("create order lend pay has exception");
        }
    }

    @Override
    public ResponseData pushAssetRepaymentPeriod(PocketPushAssetRepaymentPeriodDto dto) {
        logger.info("begin push asset repayment period,and param is:" + dto.toString());
        try {
            return getResponseData(JSON.toJSONString(dto), PocketTxCodeEnum.pushAssetRepaymentPeriod.getCode());
        } catch (Exception e) {
            logger.info("push asset repayment period has exception,and param is:" + dto.toString(), e);
            return ResponseData.error("push asset repayment period has exception");
        }
    }

    private ResponseData getResponseData(String param, String code) throws Exception {
        PocketParentDto parentDto = new PocketParentDto();
        parentDto.setTxCode(code);
        parentDto.setVersion(pocketConfig.getPocketVersion());
        parentDto.setTxTime(String.valueOf(System.currentTimeMillis() / 1000));
        parentDto.setPlatNo(pocketConfig.getPocketPlatNo());
        String string = param;
        String pack = DesHelper.desEncrypt(pocketConfig.getPocketDesKey(), string);
        parentDto.setPack(pack);
        JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(parentDto));
        Map<String, String> resultMap = (Map) params;
        String sign = KdaiSignUtils.createSign(resultMap, pocketConfig.getPocketSignKey());
        parentDto.setSign(sign);
        String json = KdaiSignUtils.httpPostWithJSON(pocketConfig.getPocketUrl(), JSONObject.toJSONString(parentDto));
        PocketParentResult result = JSONObject.parseObject(json, PocketParentResult.class);
        return ResponseData.success(result);

    }
}
