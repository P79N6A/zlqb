package com.nyd.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.ibatis.type.Alias;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dengw on 17/11/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_product")
@Alias("Product")
public class Product {
    @Id
    private Long id;
    //金融产品code
    private String productCode;
    //金融产品名称
    private String productName;
    //借款动作描述
    private String actionDescription;
    //产品类型
    private Integer productType;
    //产品期数
    private Integer productPeriods;
    //产品放贷最大额度
    private BigDecimal maxCredit;
    //日利率
    private BigDecimal interestRate;
    //最小额度
    private BigDecimal minPrincipal;
    //最大额度
    private BigDecimal maxPrincipal;
    //额度步长
    private BigDecimal principalStep;
    //最小借款天数
    private Integer minLoanDay;
    //最大借款天数
    private Integer maxLoanDay;
    //借款天数步长
    private Integer loanDayStep;
    //资金源编码
    private String fundCode;
    //金融产品使用的产品线
    private String business;
    //金融产品图片url
    private String fileUrl;
    //金融产品跳转H5地址
    private String linkUrl;
    //金融产品跳转H5地址Key
    private String linkKey;
    //是否推荐:0推荐，1不推荐
    private String recommendFlag;
    //是否在用
    private Integer isInUse;
    //产品提示
    private String pointContent;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
    
    //管理费
    private BigDecimal managerFee;
}
