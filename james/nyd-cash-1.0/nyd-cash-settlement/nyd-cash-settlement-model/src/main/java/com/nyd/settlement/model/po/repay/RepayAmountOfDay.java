package com.nyd.settlement.model.po.repay;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dengw on 2018/1/19
 */
@Data
public class RepayAmountOfDay {
    //每天强扣还款金额
    private BigDecimal repayAmountOfDay;

    //jianmianjine
    private BigDecimal derateAmountOfDay;
    //强扣还款时间
    private Date repayTime;
}
