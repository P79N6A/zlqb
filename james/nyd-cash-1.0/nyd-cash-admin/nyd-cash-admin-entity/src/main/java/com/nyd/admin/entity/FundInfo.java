package com.nyd.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hwei on 2017/12/29.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_fund_info")
public class FundInfo {
    @Id
    private Long id;
    //资产id
    private String fundId;
    //姓名
    private String name;
    //身份证号码
    private String idNumber;
    //投资金额
    private BigDecimal investmentAmount;
    //入账日期
    private Date accountDate;
    //回款金额
    private BigDecimal backAmount;
    //总收益
    private BigDecimal totalProfit;
    //回款日期
    private Date backDate;
   //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;

    //日期
    private Date startDate;
    private Date endDate;
}
