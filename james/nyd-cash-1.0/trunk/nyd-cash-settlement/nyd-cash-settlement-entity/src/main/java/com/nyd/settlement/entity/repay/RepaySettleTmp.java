package com.nyd.settlement.entity.repay;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * yuxiang.cong
 */
@Data
@Table(name = "t_repay_settle_tmp")
public class RepaySettleTmp {
    @Id
    private Long id;
    private String userName;
    private String mobile;
    /**
     * 用户Id，逻辑主键
     */
    private String userId;

    /**
     * 还款编号
     */
    private String repayNo;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 0 不减免 1 利息 2 罚息 3 滞纳金 4 利息加罚息 5利息加罚息加滞纳金 6 其他
     */
    private Integer derateType;

    /**
     * 减免金额
     */
    private BigDecimal derateAmount;

    /**
     * 还款时间
     */
    private Date repayTime;

    /**
     * 还款金额
     */
    private BigDecimal repayAmount;

    /**
     * 还款姓名
     */
    private String repayName;

    /**
     * 还款身份证号
     */
    private String repayIdNumber;

    /**
     * 还款账号
     */
    private String repayAccount;

    /**
     * 支付通道 0
     */
    private String repayChannel;

    /**
     * 建议金额
     */
    private BigDecimal adviceAmount;

    /**
     * 0强扣,1使用第二种方法还款,2用绑定的卡还款,3会员费,10非以上任何一种,11会员费失败,12强扣失败,13使用第二种方式还款失败,14用绑定的卡还款失败,15未知失败
     */
    private Integer repayType;

    /**
     * 0已审核 1未审核
     */
    private Integer auditStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 添加时间
     */
//    private Date createTime;
//
//    /**
//     * 修改时间
//     */
//    private Date updateTime;

    /**
     * 最后修改人
     */
    private String updateBy;

}