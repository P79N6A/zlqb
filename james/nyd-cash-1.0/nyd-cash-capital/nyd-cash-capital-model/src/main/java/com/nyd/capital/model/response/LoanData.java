package com.nyd.capital.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public @Data
class LoanData implements Serializable{
    //商户订单号  订单编号
    private String orderNo;
    //交易编号 放款编号 微神马只有商户订单号
    private String transactionNo;
    //资金源
    private String fundSource;

    //银行名称
    private String bank;
    //支付时间
    private String payTime;
    //协议链接地址
    private String contractLink;

    //服务费
    private String serviceCost;

    //银行利率
    private String bankRate;

    private String msg;

}
