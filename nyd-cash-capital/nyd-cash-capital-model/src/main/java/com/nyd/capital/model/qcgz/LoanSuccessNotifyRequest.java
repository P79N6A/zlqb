package com.nyd.capital.model.qcgz;


import lombok.Data;
import java.io.Serializable;
import java.util.List;


/**
 * 放款成功通知请求参数
 * @author cm
 */
@Data
public class LoanSuccessNotifyRequest implements Serializable {

    //渠道号
    private String channelCode;

    //资产编号
    private String assetId;

    //放款时间 (格式为 2018-07-19 10:01:01)
    private String loanTime;

    //放款状态        放款结果;   0:放款成功 1:放款失败
    private int loanResult;

    //投资人数组
    private List<QcgzInvestorResponse> investorList;

    //签名
    private String sign;

}
