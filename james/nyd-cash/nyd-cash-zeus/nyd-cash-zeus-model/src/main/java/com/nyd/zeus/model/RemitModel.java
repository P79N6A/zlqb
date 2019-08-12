package com.nyd.zeus.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 16:01 2018/9/4
 */
@Data
public class RemitModel implements Serializable{
    //订单编号
    private String orderNo;

    //放款编号
    private String remitNo;

    //放款金额
    private BigDecimal remitAmount;

    //放款时间
    private Date remitTime;

    //资金源编码
    private String fundCode;

    //放款状态
    private String remitStatus;

    //放款失败状态码
    private String errorCode;

    //客户银行卡号（到账账号）
    private String userBankNo;

    //放款银行
    private String remitBankName;

    //协议地址
    private String contractLink;
    //支付方式
    private String payType;

    //到账方式
    private String accountType;

    //放款手续费
    private BigDecimal remitProcedureFee;

    private String investorId;

    private String investorName;

    private Integer channel;
}
