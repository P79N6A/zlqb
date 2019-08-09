package com.nyd.settlement.entity.refund;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Created by hwei on 2018/1/15.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QueryRefundEntity  {
    private Date startDate;//开始日期
    private Date endDate;//结束日期
    private String mobile;//手机号
    private String orderNo;//订单号

}
