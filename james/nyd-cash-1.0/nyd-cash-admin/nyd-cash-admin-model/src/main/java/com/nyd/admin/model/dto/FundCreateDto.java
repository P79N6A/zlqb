package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by hwei on 2018/1/2.
 */
@Data
public class FundCreateDto implements Serializable{
    //姓名
    private String name;
    //身份证号
    private String idNumber;
    //投资金额
    private BigDecimal investmentAmount;
    //回款日期
    private Date backDate;
    //入账日期
    private Date accountDate;
    //回款金额
    private BigDecimal backAmount;
    //总收益
    private BigDecimal totalProfit;
    //创建人
    private String updateBy;
    //投资详情
    private List<FundDetailDto> fundDetailDtoList;
    //投资期限
    private Integer month;
}
