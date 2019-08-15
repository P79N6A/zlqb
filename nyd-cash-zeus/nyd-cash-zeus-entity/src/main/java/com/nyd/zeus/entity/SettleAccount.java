package com.nyd.zeus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by dengqingfeng on 2019/8/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="t_settle_account")
public class SettleAccount implements Serializable {
    /**
     * id
     */
    @Id
    private Integer id;
    private String billNo;
    private String orderNo;
    private String payType;
    private String payTime;
    private String voucherNo;
    private String settleState;
    private String remark;

    /**
     * 创建时间
     */
    private Date createDate;
    private BigDecimal reductionAmount;
    private BigDecimal repaymentAmount;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;
}
