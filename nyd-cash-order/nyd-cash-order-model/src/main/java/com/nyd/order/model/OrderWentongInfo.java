package com.nyd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2018/4/24
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderWentongInfo implements Serializable{
    //订单id
    private String orderNo;
    //用户ID
    private String userId;
    //手机号
    private String mobile;

    private String name;
    private String loanTime;

}
