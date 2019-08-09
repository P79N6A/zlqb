package com.nyd.settlement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_order")
public class YmtOrder {

    //主键id
    @Id
    private Long Id;
    //订单编号
    private String orderNo;
    //用户ID
    private String userId;
    //推荐费id
    private String recommendId;
    //推荐费流水码
    private String repayNo;
    //身份证号码
    private String idNumber;
    //姓名
    private String realName;
    //手机号
    private String mobile;
    //用户来源
    private String source;
    //审核费用
    private BigDecimal auditFee;
    //借款用途
    private String loanPurpose;
    //借款金额
    private BigDecimal loanAmount;
    //借款时间（天数）
    private Integer borrowTime;
    //借款期数
    private Integer borrowPeriods;
    //用户银行账户
    private String bankAccount;
    //银行名称
    private String bankName;
    //订单状态
    private Integer orderStatus;
    //审核结果
    private String auditStatus;
    //审核详细原因
    private String auditReason;
    //借款申请时间
    private Date loanTime;
    //放款时间
    private Date payTime;
    //业务类型
    private String business;
    //是否测试申请
    private Integer testStatus;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
