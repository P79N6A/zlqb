package com.nyd.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransformChartVo implements Serializable {
    //日期（条件）
    private String startDate;
    //日期（条件）
    private String endDate;
    //渠道
    private String source;

    //日期
    private String reportDate;
    //注册量
    private Integer registerCount;
    //借款量
    private Integer loanCount;
    //放款量
    private Integer grantCount;
    //借款转化率
    private BigDecimal loanRate;
    //借款通过率
    private BigDecimal passRate;

}
