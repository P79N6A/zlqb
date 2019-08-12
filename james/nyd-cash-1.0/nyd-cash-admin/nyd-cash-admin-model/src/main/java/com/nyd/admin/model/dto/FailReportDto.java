package com.nyd.admin.model.dto;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 2017/12/14
 **/
@Data
public class FailReportDto {

    //日期
    private String reportDate;
    //失败原因
    private String failReason;
    //笔数
    private Integer sumNumber;
    //占比
    private BigDecimal proportion;

    private String startDate;

    private String endDate;

}
