package com.nyd.capital.model.pocket;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author liuqiu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Pocket2CallbackDataVo {


    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 状态:1 成功 2 失败
     */
    private String status;
}
