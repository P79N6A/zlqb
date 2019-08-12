package com.nyd.batch.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class TRemit {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 放款编号
     */
    private String remitNo;

    /**
     * 放款金额
     */
    private BigDecimal remitAmount;

    /**
     * 放款时间
     */
    private Date remitTime;

    /**
     * 资金源code
     */
    private String fundCode;

    /**
     * 放款状态 0：成功；1：失败
     */
    private String remitStatus;

    /**
     * 客户银行卡号（到账账号）
     */
    private String userBankNo;

    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 协议地址
     */
    private String contractLink;

    /**
     * 放款银行
     */
    private String remitBankName;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 到账方式
     */
    private String accountType;

    /**
     * 放款手续费
     */
    private BigDecimal remitProcedureFee;

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

    /**
     * 投资人id
     */
    private String investorId;

    /**
     * 投资人姓名
     */
    private String investorName;

    /**
     * 渠道    0 :nyd ;    1:ymt
     */
    private Integer channel;

}