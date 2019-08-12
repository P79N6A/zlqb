package com.nyd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2018/1/2
 * 是否可借款信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JudgeInfo implements Serializable {
    //是否可以借款，0可以借款，1不可借款
    private String whetherLoan;
    //是否借款话术
    private String whetherLoanMsg;

    //存在未处理订单  0 存在  1不存在
    private String unProcessOrderExist;

    private String orderFlag; // 1 审批中 2 待提现 3还款中 4 拒单 5 放款失败
}
