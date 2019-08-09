package com.nyd.zeus.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "风控扣款排除", description = "风控扣款排除")
public class PaymentRiskExcludeInfoResult implements Serializable {
	private Long id;
	@ApiModelProperty(value = "客户姓名")
	private String userName;
	@ApiModelProperty(value = "手机号码")
    private String phone;
	@ApiModelProperty(value = "贷款编号")
    private String orderNo;
	@ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime; 
	@ApiModelProperty(value = "状态 1有效  0失效")
	private Integer status;
}
