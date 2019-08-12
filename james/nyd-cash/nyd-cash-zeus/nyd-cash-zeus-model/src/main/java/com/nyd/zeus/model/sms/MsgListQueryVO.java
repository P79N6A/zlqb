package com.nyd.zeus.model.sms;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nyd.zeus.model.common.PageCommon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "afterLoan", description = "贷后短信列表查询")
public class MsgListQueryVO extends PageCommon implements Serializable {
	
	private static final long serialVersionUID = 8424976147652818772L;

	//短信渠道
	@ApiModelProperty(value = "短信渠道")
	private String msgChannel;
	
	//联系人
	@ApiModelProperty(value = "发送状态 失败：0  成功：1  处理中： -1")
	private String status;
	
	//短信渠道
	@ApiModelProperty(value = "回复状态 已回:1 ,未：0")
	private String replyStatus;
	
	//发送开始时间
	@ApiModelProperty(value = "发送开始时间")
	private String sendTimeBegin;
	
	@ApiModelProperty(value = "发送结束时间")
	private String sendTimeEnd;
	
	@ApiModelProperty(value = "到期开始时间")
	private String expireTimeBegin;
	
	@ApiModelProperty(value = "到期结束时间")
	private String expireTimeEnd;
	
	@ApiModelProperty(value = "受理人员")
	private String createUser;
	
	@ApiModelProperty(value = "是否发送全部 true 发送 false 不发")
	private boolean allStatus;
	
	@ApiModelProperty(value = "放款产品")
	private String productName;
	
	@ApiModelProperty(value = "客户姓名")
	private String custName;
	
	@ApiModelProperty(value = "手机号")
	private String mobile;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "发送时间")
	private String createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "到期时间")
	private String expireTime;
	
	@ApiModelProperty(value = "短信内容")
	private String sendContent;
	
	@ApiModelProperty(value = "回复内容")
	private String replyContent;
	
	@ApiModelProperty(value = "订单号")
	private String orderNo;
	
	@ApiModelProperty(value = "剩余应还金额")
	private String waitRepayAmount;
	
	@ApiModelProperty(value = "到期时间")
	private String retExpireTime;
	
	@ApiModelProperty(value = "billNo")
	private String billNo;
	
	@ApiModelProperty(value = "userId")
	private String userId;
	
	@ApiModelProperty(value = "用户返回内容")
	private List<JSONObject> retContent;
	
	


}
