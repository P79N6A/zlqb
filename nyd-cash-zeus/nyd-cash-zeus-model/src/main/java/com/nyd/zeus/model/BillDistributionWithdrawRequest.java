package com.nyd.zeus.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 撤回
 * 
 * @author Administrator
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillDistributionWithdrawRequest implements Serializable {

	@ApiModelProperty(value = "orderNo集合")
	private List<String> orderNoList;

}
