package com.nyd.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Peng
 * @create 2017-12-28 19:21
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusinessChartVo implements Serializable{

    //当天申请笔数
    private Integer dailyApply;
    //当天授信笔数
    private Integer dailyCredit;
    //通过率
    private BigDecimal passRate;

    private String reportDate;

    //日期
    private String startDate;

    private String endDate;
}
