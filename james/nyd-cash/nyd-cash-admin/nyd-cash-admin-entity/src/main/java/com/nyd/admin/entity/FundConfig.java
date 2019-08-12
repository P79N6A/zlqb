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
@Table(name = "t_fund_config")
public class FundConfig {
    @Id
    private Long id;
    //投资期限类型
    private Integer termType;
    //利率
    private BigDecimal returnRate;
    //百分比利率
    private String ratePercentage;
   //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
