package com.nyd.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hwei on 2017/12/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountDto implements Serializable{
    //账号
    private String accountNumber;
    //账号类型
    private Integer accountType;
    //用户ID
    private String userId;

    private String iBankUserId;
    //信用额度
    private BigDecimal limitCredit;
    //可用额度
    private BigDecimal usableCredit;
    //已用额度
    private BigDecimal usedCredit;
    //产品线
    private String productCode;
    //账号状态
    private String status;
    //用户来源
    private String source;
    //最后活跃时间
    private Date lastActiveTime;
    /**用户渠道（0 侬要贷 1银码头）*/
    private Integer channel;

    /**
     * 用户账户充值余额
     */
    private BigDecimal balance;

    /**
     * 用户返回余额
     */
    private BigDecimal returnBalance;

    /**
     * 账户余额
     */
    private BigDecimal totalBalance;
    
    private String faceFlag;
    
    
}
