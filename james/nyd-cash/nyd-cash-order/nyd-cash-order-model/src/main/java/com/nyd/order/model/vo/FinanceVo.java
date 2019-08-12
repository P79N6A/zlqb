package com.nyd.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Dengw on 2017/12/7
 * 借款金额，天数描述
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class FinanceVo {
    //值
    private String value;
    //单位
    private String text;
}
