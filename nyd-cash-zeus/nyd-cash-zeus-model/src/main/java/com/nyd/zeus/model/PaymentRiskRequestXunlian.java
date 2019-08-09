package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


/**
 * Created by zhujx on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRiskRequestXunlian implements Serializable{
     
    // 银行卡号
    private String account;
    // 姓名
    private String name;
    // 协议号
    private String protocolId;
    
    
}
