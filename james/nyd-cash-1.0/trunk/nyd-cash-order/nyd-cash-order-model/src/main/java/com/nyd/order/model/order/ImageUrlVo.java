package com.nyd.order.model.order;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@Data
public class ImageUrlVo implements Serializable {
	
	private static final long serialVersionUID = 3707952947702991066L;
	
	@ApiModelProperty(value = "正面")
	private String frontUrl;
	
	@ApiModelProperty(value = "反面")
	private String backUrl;
	
	@ApiModelProperty(value = "活体")
	private String livingUrl;
	
	@ApiModelProperty(value = "用户id")
	private String userId;

}
