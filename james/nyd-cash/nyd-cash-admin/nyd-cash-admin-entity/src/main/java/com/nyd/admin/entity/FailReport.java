package com.nyd.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**

/**
 * @author peng
 * @create 2017-12-14 21:54
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_fail_report")
public class FailReport {

    @Id
    private Integer id;
    //日期
    private Date reportDate;
    //失败原因
    private String failReason;
    //笔数
    private Integer sumNumber;
    //占比
    private BigDecimal proportion;


    //日期
    @Transient
    private Date startDate;
    @Transient
    private Date endDate;
}
