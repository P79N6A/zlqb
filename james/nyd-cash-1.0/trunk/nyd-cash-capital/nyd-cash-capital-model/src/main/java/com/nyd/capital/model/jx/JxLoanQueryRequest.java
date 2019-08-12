package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

/**
 *	放款查询请求实体类
 * @author cm
 */

@Data
public class JxLoanQueryRequest implements Serializable{

    //版本
    private String version;

    //备注：见接口名称括号内容
    private String txCode;

    //第三方标识(已经取消了)
//    private String appId;

    //机构号
    private String instCode;

    //渠道
    private String channel;

    //请求年月日(格式:YYYYMMDD)
    private String txDate;

    //请求时分秒(格式:HHMMSS)
    private String txTime;

    //6位随机数
    private String seqNo;

    //
    private String acqRes;

    //标Id
    private String loanId;

    //签名
    private String sign;
}
