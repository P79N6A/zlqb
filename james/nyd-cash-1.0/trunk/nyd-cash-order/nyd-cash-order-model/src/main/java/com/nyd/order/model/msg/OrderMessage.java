package com.nyd.order.model.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dengw on 2017/11/27.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderMessage implements Serializable {
    //订单号
    private String orderNo;
    //useId
    private String userId;
    //产品类型
    private String productType;
    //借款类型
    private String borrowType;

    /**
     * 0 nyd 1 ymt
     */
    private Integer channel;

    /**
     * kzjr wt
     */
    private String fundCode;
    private String bankAccount;

    /**
     * mq类型   1-发送消息生成账单并更改订单为已放款,再发消息生成放款记录
     *          2-只发送消息生成放款记录
     *          3-更改订单状态为放款失败
     *          4-修复提交借款失败的用户(临时的)
     */
    private Integer mqType;

    /**
     * 多来点用户id
     */
    private String customerId;

    private int ifTask;

    //用户手机号
    private String accountNumber;
    //会员类型
    private String memberType;//有
    //支付现金
    private BigDecimal payCash;
    //会员id
    private String memberId; //没有
    //身份证号码
    private String idNumber;
    /**
     * 代扣支付订单号
     */
    private String payOrderNo;
    private int result;

    /**
     * 渠道来源
     */
    private String appName;
}
