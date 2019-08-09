package com.creativearts.nyd.pay.model.response;

import com.nyd.zeus.model.PayModelEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/11/20
 **/
public @Data
class PayResponseData implements Serializable{
    //交易流水号
    private String transactionNo;
    //支付渠道
    private PayModelEnum channel;
    //账单号
    private String billNo;
//    private String

}
