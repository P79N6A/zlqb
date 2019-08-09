package com.nyd.capital.model.pocket;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@ToString
public class PocketOrderVo {

    /**
     * 订单id
     */
    private String order_id;

    /**
     * 是否存管:1是 0否
     */
    private String is_deposit;
}
