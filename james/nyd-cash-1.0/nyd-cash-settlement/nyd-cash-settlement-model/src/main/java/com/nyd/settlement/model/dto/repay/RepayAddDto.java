package com.nyd.settlement.model.dto.repay;

import com.nyd.settlement.model.annotation.RequireField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 添加还款
 * Cong Yuxiang
 * 2018/1/16
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RepayAddDto implements Serializable{

    private String name;

    private String mobile;

    private String userId;

    @RequireField
    private String billNo;//账单号

    @RequireField
    private String repayDate;//还款日期
    @RequireField
    private BigDecimal repayAmount;//还款金额
    @RequireField
    private String repayChannel;//还款渠道
    @RequireField
    private String transactionNo;//交易流水

    private Integer derateType; // 0 不减免 1 利息 2 罚息 3 滞纳金 4 利息加罚息 5利息加罚息加滞纳金 6 其他

    private BigDecimal derateAmount;//减免金额

    private String repayName;//代还人

    private String repayBankNo;//代还人卡号

    private String remark; //备注

    private String updateBy;//修改人

}
