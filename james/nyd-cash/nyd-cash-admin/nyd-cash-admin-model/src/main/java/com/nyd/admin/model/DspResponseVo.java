package com.nyd.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by hwei on 2017/12/19.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DspResponseVo {
    private String dspDate;//日期
    private String dspInterface;//接口名称（供应商）
    private Long callCount;//调用次数
    private Long successCallCount;//成功调用次数
    private BigDecimal dspPrice;//单价
    private BigDecimal totalCost;//费用合计
    private String successRate;//成功率
    private BigDecimal totalExpense;//费用支出
}
