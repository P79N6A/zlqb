package com.nyd.settlement.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Peng
 * @create 2018-01-16 11:03
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelVo {
    //订单号
    private String orderNo;
    //备注
    private String remark;
    //创建人
    private String updateBy;
}
