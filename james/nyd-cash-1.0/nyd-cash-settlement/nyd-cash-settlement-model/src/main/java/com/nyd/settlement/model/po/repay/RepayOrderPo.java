package com.nyd.settlement.model.po.repay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dengw on 2018/1/17
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RepayOrderPo {
    private String userId;
    private Integer productType;
    private Integer testStatus;
    private String fundCode;
    private String orderNo;
    private String memberFeeId;
    private String name;
    private String mobile;
    private String memberFee;
    private BigDecimal contractAmount;//合同金额
    private String borrowTime;//借款周期
    private Date payTime;//放款日期
}
