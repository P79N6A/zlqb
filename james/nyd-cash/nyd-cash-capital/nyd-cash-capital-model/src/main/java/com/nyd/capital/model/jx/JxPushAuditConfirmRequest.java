/**
 * Project Name:nyd-cash-capital-model
 * File Name:JxpushAuditConfirmRequest.java
 * Package Name:com.nyd.capital.model.jx
 * Date:2018年7月30日下午12:01:35
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.model.jx;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:JxpushAuditConfirmRequest <br/>
 * Function: 即信推单外审确认请求参数
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年7月30日 下午12:01:35 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class JxPushAuditConfirmRequest extends JxCommonRequest implements Serializable {
	/**
	 * serialVersionUID:序列化ID.
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -990054130849056883L;

	//借款单id
	private Long loanOrderId;
	//外部单号(调用方生成，保证唯一)
	private String outOrderId;
	//用户授权协议下载地址
	private String userAgreement;
	//加密值
	private String sign;
}

