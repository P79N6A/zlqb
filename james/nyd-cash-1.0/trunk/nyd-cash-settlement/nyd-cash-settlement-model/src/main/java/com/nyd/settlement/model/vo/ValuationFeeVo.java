package com.nyd.settlement.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ValuationFeeVo {

    private String orderNo;//订单号
    private String userId;//客户编号
    private String productType;//产品类型
    private String label;//标签
    private String name;//姓名
    private String mobile;//手机号
    private BigDecimal repayAmount;//评估费
    private String repayChannel;//支付渠道
    private String repayTime;//支付日期
    private BigDecimal thirdProcedureFee = new BigDecimal(0); //支付手续费



    private BigDecimal refundAmount; //已退金额（该字段不需要，评估费不退款）
}
