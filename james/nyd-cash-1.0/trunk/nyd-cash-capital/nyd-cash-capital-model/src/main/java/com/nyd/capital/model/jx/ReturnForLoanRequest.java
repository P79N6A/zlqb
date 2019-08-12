package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuqiu
 */
@Data
public class ReturnForLoanRequest implements Serializable {
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
     * 标ID
     */
    private Long loanId;
    /**
     * nyd的订单ID
     */
    private String outOrderId;
    /**
     * 放款时间
     */
    private Date createdAt;
    /**
     * 满标时间
     */
    private Date soldOutAt;
}
