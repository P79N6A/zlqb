package com.nyd.zeus.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.nyd.zeus.model.common.PageCommon;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * Created by dengqingfeng on 2019/8/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRiskExcludeInfoRequest extends PageCommon implements Serializable {
	private static final long serialVersionUID = -1876290077342797035L;
	@ApiModelProperty("客户姓名")
	private String userName;
	@ApiModelProperty("手机号码")
    private String phone;
	@ApiModelProperty("贷款编号")
	private String orderNo;
	//贷款编号集合
	private List<String> orderNoList;

}
