package com.nyd.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/** Peng
 * 2017/12/13
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_business_report")
public class BusinessReport {
    @Id
    private Integer id;
    //日期
    private Date reportDate;
    //总注册客户数
    private Integer sumAccount;
    //身份认证完成客户数
    private Integer sumUser;
    //总授信额度（万）
    private BigDecimal totalAcctAmt;
    //总应收账款
    private BigDecimal sumAmt;
    //当天授信笔数
    private Integer dailyCredit;
    //当天总授信额度
    private BigDecimal dailyAmt;
    //户均授信额度
    private BigDecimal avgCredit;
    //户均应收账款
    private BigDecimal avgAmt;
    //当天申请笔数
    private Integer dailyApply;
    //通过率
    private BigDecimal passRate;

    //当天放款成功笔数
    private Integer dailySuccessCount;
    //当天放款成功额度
    private BigDecimal dailySuccessAmt;
    //总的放款笔数
    private Integer successCount;
    //总的放款金额
    private BigDecimal successAmt;

    //总的应收客户
    private Integer sumDebtUser;
    //当日放款成功率
    private BigDecimal loanRate;
    //总授信客户数
    private Integer sumCreditUser;

    //当日注册客户数
    private Integer dailyRegister;
    //当日认证客户数
    private Integer dailyViaUser;
    //日期
    @Transient
    private Date startDate;
    @Transient
    private Date endDate;

}
