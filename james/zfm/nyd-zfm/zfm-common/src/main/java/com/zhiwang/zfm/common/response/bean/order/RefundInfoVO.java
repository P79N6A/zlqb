package com.zhiwang.zfm.common.response.bean.order;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情-退款处理记录
 */
public class RefundInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "退款记录")
	private List<RefundInfoList> refundInfoList;
	@ApiModelProperty(value = "期望退款金额")
	private String expectedAmount;
	@ApiModelProperty(value = "已退款金额")
	private String alreadyAmount;
	
	
	
	public List<RefundInfoList> getRefundInfoList() {
		return refundInfoList;
	}



	public void setRefundInfoList(List<RefundInfoList> refundInfoList) {
		this.refundInfoList = refundInfoList;
	}



	public String getExpectedAmount() {
		return expectedAmount;
	}



	public void setExpectedAmount(String expectedAmount) {
		this.expectedAmount = expectedAmount;
	}



	public String getAlreadyAmount() {
		return alreadyAmount;
	}



	public void setAlreadyAmount(String alreadyAmount) {
		this.alreadyAmount = alreadyAmount;
	}



	public class RefundInfoList{
		@ApiModelProperty(value = "期望退款时间")
		private String expectedTime;
		@ApiModelProperty(value = "退款结果")
		private String refundResult;
		@ApiModelProperty(value = "退款金额")
		private String refundAmount;
		@ApiModelProperty(value = "备注")
		private String remark;
		@ApiModelProperty(value = "实际退款时间")
		private String actualTime;
		public String getExpectedTime() {
			return expectedTime;
		}
		public void setExpectedTime(String expectedTime) {
			this.expectedTime = expectedTime;
		}
		public String getRefundResult() {
			return refundResult;
		}
		public void setRefundResult(String refundResult) {
			this.refundResult = refundResult;
		}
		public String getRefundAmount() {
			return refundAmount;
		}
		public void setRefundAmount(String refundAmount) {
			this.refundAmount = refundAmount;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getActualTime() {
			return actualTime;
		}
		public void setActualTime(String actualTime) {
			this.actualTime = actualTime;
		}
	}
}
