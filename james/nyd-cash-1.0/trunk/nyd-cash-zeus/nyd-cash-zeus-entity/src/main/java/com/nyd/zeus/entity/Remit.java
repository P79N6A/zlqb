package com.nyd.zeus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/21.
 * 放款表
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_remit")
public class Remit {

    //主键id
    @Id
    private Long id;

    //订单编号
    private String orderNo;

    //放款编号
    private String remitNo;

    //放款金额
    private BigDecimal remitAmount;

    //放款时间
    private Date remitTime;

    private String investorId;

    private String investorName;

    //资金源编码
    private String fundCode;

    private Integer channel; //0nyd 1ymt

    //放款状态
    private String remitStatus;

    //放款失败状态码
    private String errorCode;

    //客户银行卡号（到账账号）
    private String userBankNo;

    //放款银行
    private String remitBankName;

    private String contractLink;

    //支付方式
    private String payType;

    //到账方式
    private String accountType;

    //放款手续费
    private String remitProcedureFee;

    //是否已删除
    private Integer deleteFlag;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;



}
