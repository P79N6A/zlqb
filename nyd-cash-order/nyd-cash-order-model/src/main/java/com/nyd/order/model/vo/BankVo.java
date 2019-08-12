package com.nyd.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Dengw on 2017/11/15
 * 银行信息返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankVo {
    //银行
    private String bankName;
    //银行卡号
    private String bankAccount;
    //银行类型  1 扣款  2 放款
    private String bankType;
}
