package com.nyd.capital.model.kzjr;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2017/12/15
 **/
@Data
public class SubAsset implements Serializable{
    private String productCode;
    private String orderId;
    private String accountId;
    private BigDecimal amount;
    private Integer type; //1、自然资产 2、通路资产 3、过账资产，默认为资产资产
    private Integer refundDay;//当资产为过账资产或通路资产时，系统会在refundDay后自动退款，默认为1
    private Integer duration; //借款期限

}
