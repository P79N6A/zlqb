package com.nyd.capital.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
@Data
public class WsmResponse implements Serializable{
    private String bank; //签约银行
    private String contract_link; //电子协议地址
    private String shddh; //商户订单号
    private String sign; //签名
    private String service_cost; // 服务费
    //状态 success
    private String state; //状态
    private String bank_rate;//银行利率
    private String errorcode;//错误码
    private String pay_time;//支付时间
    private String msg;//消息
    private String total_interest;//总利息

}
