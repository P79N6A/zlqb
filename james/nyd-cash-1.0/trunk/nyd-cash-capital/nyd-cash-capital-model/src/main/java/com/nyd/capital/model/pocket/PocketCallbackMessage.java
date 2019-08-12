package com.nyd.capital.model.pocket;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PocketCallbackMessage {
    /**
     * 接口类型
     */
    private String txCode;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 状态:1 成功 2 失败
     */
    private String status;
    /**
     * 状态码解释
     */
    private String retMsg;
    /**
     * 请求是否失败:true-是,false-否
     */
    private  boolean fail;
}
