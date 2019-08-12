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
 * 2017/12/13
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_transform_report")
public class TransformReport {
    //@Id
    private Integer id;
    //日期
    private Date reportDate;
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
