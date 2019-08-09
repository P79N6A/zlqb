package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

/**
 *
 */
@Data
public class AgreementRequest {
    //用户Id
//    private String userid;

    //商户编号
    private String accountId;

    //商户协议编号
    private String contractId;

    //用户姓名
    private String name;

    //手机号
    private String phoneNo;

    //银行卡号
    private String cardNo;

    //身份证号
    private String idCardNo;

    //子协议开始时间
    private String startDate;

    //子协议结束时间
    private String endDate;

    //mac
    private String mac;




}
