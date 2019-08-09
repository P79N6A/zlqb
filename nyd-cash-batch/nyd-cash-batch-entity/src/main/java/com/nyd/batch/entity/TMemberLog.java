package com.nyd.batch.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TMemberLog {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户id
     */
    private String userId;

    private String orderNo;
    private String memberId;

    /**
     * 会员类型
     */
    private String memberType;

    /**
     * 会员类型描述
     */
    private String memberTypeDescribe;

    /**
     * 金额
     */
    private BigDecimal memberFee;

    private String debitChannel;

    /**
     * 会员购买时间
     */
    private Date startTime;

    /**
     * 是否代扣成功  1：扣款成功
     */
    private String debitFlag;

    /**
     * 是否已删除 0：正常；1：已删除
     */
    private Byte deleteFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 最后修改人
     */
    private String updateBy;


}