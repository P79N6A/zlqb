package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiu
 */
@Data
public class ReturnForPushAuditRequest implements Serializable {
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
     * 借款单ID
     */
    private Long loanOrderId;
    /**
     * 审核结果
     */
    private Integer status;
    /**
     * 审核意见
     */
    private String auditDescription;
}
