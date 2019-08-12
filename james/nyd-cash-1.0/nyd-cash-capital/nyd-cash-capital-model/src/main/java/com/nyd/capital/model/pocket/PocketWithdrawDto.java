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
public class PocketWithdrawDto {
    /**
     * 第三方订单流水号
     */
    private String outTradeNo;
    /**
     * 跳转页面
     */
    private String retUrl;
    /**
     * 需要url还是html:1 url 0 html 默认html
     */
    private String isUrl;
}
