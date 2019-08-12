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
@Table(name = "t_fund_detail")
public class FundDetail {
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
    //投资期限
    private Integer investmentTerm;
    //利率
    private BigDecimal returnRate;
    //到期日
    private Date expiryDate;
    //到期收益
    private BigDecimal expiryProfit;
    //是否续投
    private Integer continueFlag;
    //续投方式
    private Integer continueType;
   //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
