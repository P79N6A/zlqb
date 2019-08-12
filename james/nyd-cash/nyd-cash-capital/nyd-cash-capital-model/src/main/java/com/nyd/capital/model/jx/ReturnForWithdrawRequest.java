package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuqiu
 */
@Data
public class ReturnForWithdrawRequest implements Serializable {
    /**
     * 时间戳
     */
    private Integer timestamp;
    /**
     * 消息类型
     */
    private String messageType;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 即信用户ID
     */
    private String memberId;
    /**
     * 提现金额
     */
    private BigDecimal amount;
    /**
     * 提现完成时间
     */
    private Date completeddAt;
    /**
     * 手续费
     */
    private BigDecimal fee;
    /**
     * 标ID
     */
    private String loanId;
}
