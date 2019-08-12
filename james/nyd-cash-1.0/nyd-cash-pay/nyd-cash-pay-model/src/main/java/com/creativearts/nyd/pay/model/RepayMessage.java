package com.creativearts.nyd.pay.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/12/4.
 * 还款消息体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RepayMessage implements Serializable {

    //账单编号
    private String billNo;   //如果是侬要贷账单时  传的是侬要贷账单号，当时银码头账单时  传的是 银码头 订单号。

    //还款金额
    private BigDecimal repayAmount;

    //还款状态
    private String repayStatus;

    private Date repayTime;

}
