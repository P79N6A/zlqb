/**
 * Project Name:nyd-cash-capital-model
 * File Name:JxCommonRequest.java
 * Package Name:com.nyd.capital.model.jx
 * Date:2018年7月31日下午1:47:58
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName:JxCommonRequest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年7月31日 下午1:47:58 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class JxCommonRequest implements Serializable{
	private String version;//版本
	private String txCode;//接口名称
	private String instCode;//即信分配给机构的编号
	private String channel;//渠道
	private String txDate;//格式:YYYYMMDD
	private String txTime;//格式:HHMMSS
	private String seqNo;//6位随机数
	private String acqRes;//请求方保留
}

