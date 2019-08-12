package com.nyd.admin.service;

import com.nyd.admin.model.Info.RechargePaymentRecordInfo;
import com.nyd.admin.model.dto.RechargePaymentRecordDto;

import java.util.List;

/**
 * @Author: wucx
 * @Date: 2018/10/18 16:46
 */
public interface CustomerHandleService {

    /**
     * 查询充值付费总记录
     * @param rechargePaymentRecordDto
     * @return
     */
    List<RechargePaymentRecordInfo> findRechargePaymentDetails(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 查询充值付费记录总个数
     * @param rechargePaymentRecordDto
     * @return
     */
    Integer findPayAssessCount(RechargePaymentRecordDto rechargePaymentRecordDto);

   // List<RechargePaymentRecordInfo> findWithHoldDetail(RechargePaymentRecordDto rechargePaymentRecordDto);
}
