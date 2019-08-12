package com.nyd.user.model.vo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@Data
public class ContratUrlVo implements Serializable {
	
	private static final long serialVersionUID = 3707952947702991066L;
	
	@ApiModelProperty(value = "协议名称")
	private String contratName;
	
	@ApiModelProperty(value = "创建时间")
	private String createTime;
	
	@ApiModelProperty(value = "用户id")
	private String userId;

	@ApiModelProperty(value = "订单id")
	private String orderNo;
	
	@ApiModelProperty(value = "路径")
	private String url;
}
