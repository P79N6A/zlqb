package com.nyd.capital.model.kzjr;

import lombok.Data;

import java.util.List;

/**
 * @Author: zhangp
 * @Description: 批量放款请求结果
 * @Date: 20:28 2018/5/14
 */
@Data
public class BathFundCondition {

    /**nyd 订单号列表*/
    private List<String> orderNos;
}
