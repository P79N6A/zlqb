package com.nyd.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liuqiu
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_balance_order")
public class BalanceOrder implements Serializable {
    //主键id
    @Id
    private Long id;

    //订单id
    private String orderNo;
    //手机号
    private String mobile;

    /**
     * 申请人
     */
    private String name;
    /**
     * 申请时间
     */
    private Date loanTime;
    /**
     * 资产渠道
     */
    private String fundCode;
    /**
     * 资产编号
     */
    private String assetNo;
    /**
     * 投资人id
     */
    private String investorId;
    /**
     * 投资人姓名
     */
    private String investorName;
    /**
     * 投资人姓名
     */
    private int ifSuccess;

    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
