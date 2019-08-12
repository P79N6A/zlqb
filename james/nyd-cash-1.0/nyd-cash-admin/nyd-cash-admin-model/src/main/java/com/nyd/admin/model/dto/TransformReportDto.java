package com.nyd.admin.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 2017/12/14
 **/
@Data
public class TransformReportDto {
    //日期
    private String reportDate;
    //点击量
    private Integer clickCount;
    //注册量
    private Integer registerCount;
    //借款量
    private Integer loanCount;
    //放款量
    private Integer grantCount;
    //注册转化率
    private BigDecimal registerRate;
    //借款转化率
    private BigDecimal loanRate;
    //通过率
    private BigDecimal passRate;
    //渠道
    private String source;

    //日期
    private Date startDate;

    private Date endDate;
}
