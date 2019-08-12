/**
 * Project Name:nyd-cash-capital-model
 * File Name:JxQueryPushStatusRequest.java
 * Package Name:com.nyd.capital.model.jx
 * Date:2018年7月30日上午11:00:49
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.model.jx;

import java.io.Serializable;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * ClassName:JxQueryPushStatusRequest <br/>
 * Function: 即信推单查询接口请求参数
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年7月30日 上午11:00:49 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
@Configuration
public class JxQueryPushStatusRequest extends JxCommonRequest implements Serializable{

	/**
	 * serialVersionUID:序列化ID.
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -3365299244862369567L;

	//借款人手机号
	private String mobile;
	//姓名
	private String realName;
	//身份证号
	private String idCardNumber;
	//银行卡号
	private String bankCardNumber;
	//密钥值
	private String sign;
}

