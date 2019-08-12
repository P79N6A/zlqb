package com.nyd.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * Created by Dengw on 2017/11/20
 * 订单流水信息返回结果
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class OrderLogVo {
    //订单状态
    private Integer statusCode;
    //订单状态时间
    private String statusTime;
}
