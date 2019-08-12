package com.nyd.order.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 提交代扣
 * @author huangj
 *
 */
@Data
@ToString
public class SubmitWithholdDto {
	private String payOrderNo;
	private Double withholdAmount;
	private String source;
}
