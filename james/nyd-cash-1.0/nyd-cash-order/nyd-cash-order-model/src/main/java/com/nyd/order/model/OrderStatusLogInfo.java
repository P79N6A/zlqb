package com.nyd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/9.
 * 订单流水信息
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class OrderStatusLogInfo implements Serializable{
    //订单id
    private String orderNo;
    //订单变更前状态
    private Integer beforeStatus;
    //订单变更后状态
    private Integer afterStatus;
    //插入时间
    private Date createTime;
}
