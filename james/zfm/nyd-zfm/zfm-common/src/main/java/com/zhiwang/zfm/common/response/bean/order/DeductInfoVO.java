package com.zhiwang.zfm.common.response.bean.order;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单详情-扣款信息
 */
public class DeductInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "扣款列表")
	private List<DeductInfoList> deductionList;
	@ApiModelProperty(value = "合计扣款金额")
	private String totalAmount;
	
	
	public List<DeductInfoList> getDeductionList() {
		return deductionList;
	}


	public void setDeductionList(List<DeductInfoList> deductionList) {
		this.deductionList = deductionList;
	}


	public String getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}


	@SuppressWarnings("unused")
	private class DeductInfoList{
		@ApiModelProperty(value = "扣款时间")
		private String deductTime;
		@ApiModelProperty(value = "扣款金额")
		private String deductAmount;
		@ApiModelProperty(value = "扣款结果")
		private String deductResults;
		public String getDeductTime() {
			return deductTime;
		}
		public void setDeductTime(String deductTime) {
			this.deductTime = deductTime;
		}
		public String getDeductAmount() {
			return deductAmount;
		}
		public void setDeductAmount(String deductAmount) {
			this.deductAmount = deductAmount;
		}
		public String getDeductResults() {
			return deductResults;
		}
		public void setDeductResults(String deductResults) {
			this.deductResults = deductResults;
		}
	}

}
