package com.nyd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 
 * @author zhangdk
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class OrderUpdatInfo implements Serializable{
    //订单id
    private String orderNo;
    //用户ID
    private String userId;
    //是否通知
    private Integer ifNotice;
}
