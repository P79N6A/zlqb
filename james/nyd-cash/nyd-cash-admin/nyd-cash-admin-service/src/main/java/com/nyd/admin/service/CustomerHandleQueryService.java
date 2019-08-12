package com.nyd.admin.service;

import com.nyd.admin.model.dto.CustomerQueryDto;
import com.nyd.admin.model.dto.RechargePaymentRecordDto;
import com.tasfe.framework.support.model.ResponseData;

public interface CustomerHandleQueryService {
    /**
     * 客户处理查询
     * @param customerQueryDto
     * @return
     */
    ResponseData findAllCustomer(CustomerQueryDto customerQueryDto);

    /**
     * 充值付费记录
     * @param rechargePaymentRecordDto
     * @return
     */
    ResponseData findrechargePaymentRecordsByUserId(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 风险速查报告
     * @param rechargePaymentRecordDto
     * @return
     */
    ResponseData findRiskInspectionReport(RechargePaymentRecordDto rechargePaymentRecordDto);

    /**
     * 订单详情
     * @param rechargePaymentRecordDto
     * @return
     */
    ResponseData findorderdetailsByUserId(RechargePaymentRecordDto rechargePaymentRecordDto);


    /**
     * 根据userId查询代扣协议
     * @param userId
     * @return
     */
    ResponseData queryWithHoldAgreement(String userId);
}
