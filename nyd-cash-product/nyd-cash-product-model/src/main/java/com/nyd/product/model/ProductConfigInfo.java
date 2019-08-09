package com.nyd.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Dengw on 17/11/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductConfigInfo implements Serializable {
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
}
