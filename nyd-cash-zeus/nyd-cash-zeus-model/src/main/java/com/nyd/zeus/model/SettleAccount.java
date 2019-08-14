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
    @ApiModelProperty(value="账单编号",required=true)
    private String billNo;
    @ApiModelProperty(value="订单编号",required=true)
    private String orderNo;
    @ApiModelProperty(value="方式 1:支付宝，2:微信，3:银行卡",required=true)
    private String payType;
    @ApiModelProperty(value="还款日期",required=true)
    private String payTime;
    @ApiModelProperty(value="凭证号",required=true)
    private String voucherNo;
    @ApiModelProperty(value="是否结清 1:结清，2:未结清",required=true)
    private String settleState;
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建时间
     */
    private Date createDate;
    @ApiModelProperty(value="减免金额",required=true)
    private BigDecimal reductionAmount;
    @ApiModelProperty(value="还款金额",required=true)
    private BigDecimal repaymentAmount;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;
}
