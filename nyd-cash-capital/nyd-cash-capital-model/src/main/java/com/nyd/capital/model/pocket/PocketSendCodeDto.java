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
public class PocketSendCodeDto {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 订单号
     */
    private String orderNo;
}
