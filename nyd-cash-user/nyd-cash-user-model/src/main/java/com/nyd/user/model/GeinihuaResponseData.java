/**
* @Package com.ibank.user.model.flow
* @Description: TODO
* @author zhangcheng
* @date 2018年9月27日
* @version V1.0
*/
package com.nyd.user.model;

import java.io.Serializable;

import lombok.Data;

/**
* @ClassName: GeinihuaResponseData
* @Description: 给你花返回数据
* @author chenjqt
* @date 2018年9月13日
*
*/
@Data
public class GeinihuaResponseData<T> implements Serializable {
	private String code;

	private String message;
	
	private String url;
}
