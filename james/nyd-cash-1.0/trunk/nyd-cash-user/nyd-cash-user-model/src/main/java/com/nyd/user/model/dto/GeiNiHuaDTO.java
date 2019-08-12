package com.nyd.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
* @ClassName: GeiNiHuaDTO
* @Description: 给你花接口参数
* @author zhangcheng
* @date 2018年9月27日
*
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GeiNiHuaDTO {
	String appId;
	String mobile;
	String mobileMd5;
	String channelId;
	String timestamp;
	String sign;
}
