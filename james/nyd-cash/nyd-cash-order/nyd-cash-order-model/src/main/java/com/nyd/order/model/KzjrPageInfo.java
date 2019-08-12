package com.nyd.order.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2018/4/27
 **/
@Data
public class KzjrPageInfo implements Serializable{
    private String userId;
    private String orderNo;
    private String borrowTime;
    private String loanAmount;

    private Integer channel;
}
