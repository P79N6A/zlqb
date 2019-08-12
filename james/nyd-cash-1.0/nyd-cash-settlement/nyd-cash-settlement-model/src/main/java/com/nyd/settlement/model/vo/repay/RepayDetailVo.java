package com.nyd.settlement.model.vo.repay;

import lombok.Data;

/**
 * 还款详情
 * Cong Yuxiang
 * 2018/1/16
 **/
@Data
public class RepayDetailVo {
    private Integer repayId;
    private String repayChannel;
    private String repayAmount;
    private String derateAmount;
    private String derateType;
    private String repayDateTime;
    private String remark;
    private String operator;
}
