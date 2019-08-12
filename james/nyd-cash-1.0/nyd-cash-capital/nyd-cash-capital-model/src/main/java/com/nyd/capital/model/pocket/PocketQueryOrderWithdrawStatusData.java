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
public class PocketQueryOrderWithdrawStatusData {
    /**
     * 状态码:1放款中 2放款成功(钱放到电子账户)
     * 4 提现冲正 5放款成功(受托支付) 6提现成功(钱到银行卡)
     * 7提现失败(可以再次发起提现) 8不存在,9 提现失败（不需要再次发起提现 风控拒绝订单）
     * 10 提现中 11 订单取消（包含自动取消和主动取消）
     */
    private Integer status;
    /**
     * 下单时间(时间戳)
     */
    private Long orderTime;
    /**
     * 放款时间(时间戳)
     */
    private Long loanTime;
    /**
     * 提现时间(时间戳)
     */
    private Long withdrawTime;
    /**
     * 	状态为7(为可以再次提现的失败 msg是具体失败说明) 对应 密码错误 密码次数超限 提现异常需重新发起提现 交易重复
     */
    private String msg;
}
