package com.nyd.zeus.model.collection;

import java.io.Serializable;
import com.nyd.zeus.model.common.PageCommon;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "催收记录入参", description = "催收记录入参")
public class CollectionRecordRequest extends PageCommon implements Serializable {

	private static final long serialVersionUID = 395409991209286853L;
	
	@ApiModelProperty(value = "催收用户手机号")
    private String phone;
	
	@ApiModelProperty(value = "开始日期")
    private String beginTime;
	
	@ApiModelProperty(value = "结束日期")
    private String endTime;
	
	@ApiModelProperty(value = "订单号")
    private String orderNo;
	
	@ApiModelProperty(value = "用户Id")
    private String userId;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
