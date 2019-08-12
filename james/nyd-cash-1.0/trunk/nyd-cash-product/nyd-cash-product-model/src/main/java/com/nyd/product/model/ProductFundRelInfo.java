package com.nyd.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by zhujx on 2017/11/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductFundRelInfo {

    //金融产品code
    private String productCode;
    //资金源编码
    private String fundCode;
    //优先级
    private Integer priority;
}
