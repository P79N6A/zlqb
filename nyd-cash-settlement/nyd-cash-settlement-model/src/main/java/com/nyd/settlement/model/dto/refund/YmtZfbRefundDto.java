package com.nyd.settlement.model.dto.refund;

import lombok.Data;

import java.io.Serializable;

@Data
public class YmtZfbRefundDto implements Serializable{
    private String repayNo;//第三方流水号
    private String tradeNo;//本地单号
    private String refundReason;//退款原因
    private String amount;//退款金额
}
