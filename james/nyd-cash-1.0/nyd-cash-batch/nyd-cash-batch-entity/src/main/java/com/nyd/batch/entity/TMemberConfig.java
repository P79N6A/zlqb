package com.nyd.batch.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TMemberConfig {
    /**
     * 
     */
    private Integer id;

    /**
     * 会员类型
     */
    private String type;

    /**
     * 会员类型描述
     */
    private String typeDescribe;

    /**
     * 会员实际金额
     */
    private BigDecimal realFee;

    /**
     * 会员折扣价
     */
    private BigDecimal discountFee;

    /**
     * 会员有效周期
     */
    private Integer effectTime;

    /**
     * 会员使用业务范围
     */
    private String business;

    /**
     * 是否已删除 0：正常；1：已删除
     */
    private Byte deleteFlag;

    /**
     * 添加时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 最后修改人
     */
    private String updateBy;

   }