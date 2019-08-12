package com.nyd.settlement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author Peng
 * @create 2018-01-17 16:10
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    //订单号
    private String orderNo;
    //取消放款原因
    private String payFailReason;
    //订单状态
    private Integer orderStatus;
    //放款时间（逻辑查询用）
    private Date payTime;
    //失败类型
    private Integer failType;
}
