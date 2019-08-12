package com.nyd.settlement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * @author Peng
 * @create 2018-01-27 14:10
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusLogDto {
    //订单号
    private String orderNo;
    //变更前状态
    private Integer beforeStatus;
    //变更后状态
    private Integer afterStatus;
    //最后修改人
    private String updateBy;
}
