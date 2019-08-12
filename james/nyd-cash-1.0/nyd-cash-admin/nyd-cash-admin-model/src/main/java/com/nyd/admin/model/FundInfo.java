package com.nyd.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zhujx on 2017/11/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FundInfo implements Serializable {

    //资金源名称
    private String fundName;

    //资金源编码
    private String fundCode;

    //利率
    private BigDecimal interestRate;

    //放款开始时间
    private String remitStartTime;

    //放款结束时间
    private String remitEndTime;

    //最大总放款金额
    private BigDecimal maxRemitAmount;

    //支付通道
    private String payChannel;

    //支付通道费/笔
    private BigDecimal payChannelFeePerItem;

    //资金方服务费率
    private BigDecimal fundServiceInterestRate;

    //资金是否启用
    private String isInUse;

    //垫资方服务费率
    private BigDecimal padFundServiceInterestRate;

    //是否已经删除
    private Integer deleteFlag;
}
