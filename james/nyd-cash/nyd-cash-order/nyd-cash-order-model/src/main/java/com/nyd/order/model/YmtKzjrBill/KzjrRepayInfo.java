package com.nyd.order.model.YmtKzjrBill;

import lombok.Data;

import java.io.Serializable;

@Data
public class KzjrRepayInfo implements Serializable{


    //子订单号
    private String orderSno;

    //资产编号
    private String assetCode;
}
