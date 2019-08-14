package com.nyd.zeus.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by dengqingfeng on 2019/8/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value="平账参数对象")
public class SettleAccount implements Serializable {
    /**
     * id
     */
    private Integer id;
    @ApiModelProperty("账单编号")
    private String billNo;
    @ApiModelProperty("订单编号")
    private String orderNo;
    @ApiModelProperty("1:支付宝，2:微信，3:银行卡")
    private String payType;
    @ApiModelProperty("还款日期")
    private String payTime;
    @ApiModelProperty("凭证号")
    private String voucherNo;
    @ApiModelProperty("1:结清，2:未结清")
    private String settleState;
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建时间
     */
    private Date createDate;
    @ApiModelProperty("减免金额")
    private BigDecimal reductionAmount;
    @ApiModelProperty("还款金额")
    private BigDecimal repaymentAmount;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;
    @ApiModelProperty("凭据地址集合")
    private List<String> urls;
}
