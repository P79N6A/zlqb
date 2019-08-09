package com.nyd.capital.model.pocket;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liuqiu
 */
@Data
@ToString
public class PocketOrderBase implements Serializable {

    /**
     * 各厂商自己平台申请单号，请各自厂
     * 商确保必须唯一，该字段也可以是int
     */
    private String out_trade_no;

    /**
     * 贷款金额，单位：分
     */
    private int money_amount;

    /**
     * 贷款方式(0:按天,1:按月,2:按年)
     */
    private int loan_method;

    /**
     * 贷款期限,需要配合loan_method来
     * 定比如贷款3个月,则loan_method
     * 为1,loan_term为3
     */
    private int loan_term;

    /**
     * 总共利息,单位：分
     */
    private int loan_interests;

    /**
     * 贷款年化率利率，最多2位小数，百分比
     * 例如7.5表示7.5%利率
     */
    private Double apr;

    /**
     * 下单时间时间戳表示
     */
    private Long order_time;

    /**
     * 手续费,单位：分
     */
    private int counter_fee;

    /**
     * 是否录入存管系统(非必传)
     */
    private int is_deposit;

    /**
     * 借款方式（1:借款人电子账户
     * 2:名义借款人）(非必传)
     */
    private int lend_pay_type;
}
