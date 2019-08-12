package com.nyd.settlement.service.impl;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.helibao.CreateOrderVo;
import com.nyd.pay.api.enums.WithHoldType;
import com.nyd.pay.api.service.PayService;
import com.nyd.settlement.model.vo.NydHlbVo;
import com.nyd.settlement.service.BuckleService;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.UserInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cong Yuxiang
 * 2018/2/1
 **/
@Service
public class BuckleServiceImpl implements BuckleService {
    Logger logger = LoggerFactory.getLogger(BuckleServiceImpl.class);
    @Autowired
    private UserBankContract userBankContract;

    @Autowired
    private UserIdentityContract userIdentityContract;

    @Autowired
    private PayService payService;

    @Override
    public List<String> queryBanks(String userId) {

        List<String> list = new ArrayList<>();

        int loopCount = 10;
        List<BankInfo> result = userBankContract.getBankInfos(userId).getData();
        while (result == null && loopCount > 0) {
            loopCount--;
            result = userBankContract.getBankInfos(userId).getData();
        }
        if (result == null) {
            logger.info("userBankContract查询为null:" + userId);
            return new ArrayList<>();
        }

        for (BankInfo bankInfo : result) {
            list.add(bankInfo.getBankAccount());
        }
        return list;
    }

    @Override
    public ResponseData withHold(NydHlbVo vo) throws Exception {


        CreateOrderVo createOrderVo = new CreateOrderVo();


        createOrderVo.setP3_orderId(vo.getBillNo() + "-" + System.currentTimeMillis() + "-q");
        createOrderVo.setP4_timestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        createOrderVo.setP8_cardNo(vo.getBankNo());

        UserInfo userInfo = userIdentityContract.getUserInfo(vo.getUserId()).getData();
        if (userInfo == null) {
            logger.error("withHold查的userInfo为空");
        }
        if (StringUtils.isBlank(vo.getIdCard())) {
            createOrderVo.setP7_idCardNo(userInfo.getIdNumber());
        } else {
            createOrderVo.setP7_idCardNo(vo.getIdCard());
        }
        if (StringUtils.isBlank(vo.getName())) {
            createOrderVo.setP5_payerName(userInfo.getRealName());
        } else {
            createOrderVo.setP5_payerName(vo.getName());
        }

        createOrderVo.setP11_orderAmount(vo.getAmount());

        logger.info("开始代扣"+JSON.toJSONString(createOrderVo));
        ResponseData responseData = payService.withHold(createOrderVo, WithHoldType.WITH_HOLD);

        if ("0".equals(responseData.getStatus())) {

            logger.info(vo.getBillNo() + "代扣还款回写mq" + JSON.toJSONString(responseData));
            ResponseData result = ResponseData.success();
            result.setMsg("代扣成功"+vo.getAmount().toString()+"元");
            return result;

        } else if (JSON.toJSONString(responseData).contains("余额不足")) {
            logger.info(vo.getBillNo() + "余额不足" + JSON.toJSONString(responseData));
            return ResponseData.error("卡内余额不足");
        } else {
            logger.info(vo.getBillNo() + "错误：" + JSON.toJSONString(responseData));
            ResponseData result = ResponseData.error();
            result.setMsg(responseData.getMsg());
            return result;
//            return ResponseData.error("异常错误");
        }


    }
}
