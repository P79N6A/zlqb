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
public class Pocket2JobDto {
    /**
     * 1-放款跑批(查询到放款成功后进行提现),2-提现跑批(查询到提现成功后进行放款记录通知并修改订单状态)
     */
    private String taskType;
}
