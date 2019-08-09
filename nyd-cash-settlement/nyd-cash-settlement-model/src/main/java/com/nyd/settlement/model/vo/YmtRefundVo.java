package com.nyd.settlement.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class YmtRefundVo {
    private String label;//标签

    private String productType; //产品类型

    private String name;//姓名

    private String mobile;//手机号

    private String orderNo;//订单号

    private Integer refundType;//退款类型（1 评估费 ，2 推荐费，3 其他）

    private BigDecimal refundAmount;//退款金额

    private String refundChannel;//退款时间

    private String refundTime;//退款时间

    private String refundReason;//退款原因

    private String updateBy; //修改人

    private BigDecimal refundFee; //退款手续费
}
