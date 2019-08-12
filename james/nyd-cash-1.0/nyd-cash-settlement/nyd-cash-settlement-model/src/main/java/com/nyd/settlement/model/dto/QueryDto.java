package com.nyd.settlement.model.dto;

import com.nyd.settlement.model.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by hwei on 2018/1/15.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QueryDto extends Paging {
    private String startDate;//开始日期
    private String endDate;//结束日期
    private String mobile;//手机号
    private String orderNo;//订单号
    private String auditStatus;//审核结果
    private String fundCode;//放款渠道
    private String billStatus;//账单状态
    private String realName;//姓名
    private String orderStatus;//订单状态
    private String loanTime;//申请时间
}
