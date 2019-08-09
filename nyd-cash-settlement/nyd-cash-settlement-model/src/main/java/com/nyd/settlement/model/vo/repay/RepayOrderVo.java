package com.nyd.settlement.model.vo.repay;

import lombok.Data;

/**
 * Cong Yuxiang
 * 2018/1/16
 **/
@Data
public class RepayOrderVo {
    private String userId;
    private String productType;
    private String label;
    private String capitalName;
    private String orderNo;
    private String memberFeeId;
    private String name;
    private String mobile;
    private String memberFee;
    private String contractAmount;//合同金额
    private String borrowTime;//借款周期
    private String remitDate;//放款日期
}
