package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class WithholdBatchVo implements Serializable{
    private String P1_bizType;
    private String P2_customerNumber;
    private String P3_transDate;
    private String P4_batchNo;
    private String P5_goodsCatalog;
    private String P6_totalNum;
    private String P7_totalAmount;
    private String P8_records;
    private String P9_orderIp;
    private String P10_serverCallbackUrl;
//    private List<WithholdBatchDetail> list;


}
