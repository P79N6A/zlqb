package com.creativearts.nyd.pay.model.helibao;


import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class WithholdBatchDetail implements Serializable{
    private String orderNum;
    private String cardNo;
    private String payerName;
    private String idCardType;
    private String idCardNo;
    private String payerPhone;
    private String orderAmount;
    private String goodsDesc;


}
