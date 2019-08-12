package com.nyd.settlement.model.dto.repay;

import com.nyd.settlement.model.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 还款查询
 * Cong Yuxiang
 * 2018/1/15
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RepayQueryDto extends Paging {
    private String startDate;//开始日期
    private String endDate;//结束日期
    private String mobile;//手机号
    private String orderNo;//订单号
    private String remitChannel;//放款渠道
    private String billStatus;//账单状态 1未还款，2 已还款
    private String overdueStatus;//逾期账单状态0所有 1未逾期，2已逾期
    private String repayNo;//交易流水
}
