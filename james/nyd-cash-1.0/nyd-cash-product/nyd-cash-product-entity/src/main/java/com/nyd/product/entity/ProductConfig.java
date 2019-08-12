package com.nyd.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@Table(name = "t_product_config")
public class ProductConfig {
    @Id
    private Long id;
    //金融产品code
    private String productCode;
    //是否有砍头息
    private Integer isBeheadInterest;
    //砍头息比例
    private BigDecimal beheadPercentage;
    //综合费用
    private BigDecimal syntheticalFeeRate;
    //快速信审费
    private BigDecimal fastAuditFeeRate;
    //账户管理费
    private BigDecimal accountManageFeeRate;
    //身份验证费
    private BigDecimal identityVerifyFeeRate;
    //手机验证费
    private BigDecimal mobileVerifyFeeRate;
    //银行验证费
    private BigDecimal bankVerifyFeeRate;
    //征信服务费
    private BigDecimal creditServiceFeeRate;
    //信息发布费
    private BigDecimal informationPushFeeRate;
    //浮动服务费
    private BigDecimal slidingFeeRate;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
