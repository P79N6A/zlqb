package com.nyd.capital.model.qcgz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 申请放款请求参数
 * @author cm
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanApplyRequest implements Serializable{

    //渠道号
    private String channelCode;

    //资产编号
    private String assetId;

    //订单编号
    private String orderNo;

    //放款银行名称
    private String bankName;

    //放款银行卡号
    private String bankCardNo;

    //签名
    private String sign;



}
