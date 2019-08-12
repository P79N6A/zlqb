/**
 * Project Name:nyd-cash-capital-model
 * File Name:JxPushAuditResponse.java
 * Package Name:com.nyd.capital.model.jx
 * Date:2018年7月30日上午11:48:02
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.model.jx;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:JxPushAuditResponse <br/>
 * Function: 推单外审响应参数
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年7月30日 上午11:48:02 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class JxPushAuditResponse extends JxCommonResponse implements Serializable {
	/**
	 * serialVersionUID:序列化ID.
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = -7817610111890110726L;
	
	//用户ID
	private String memberId;
	//借款单Id
	private Long loanOrderId;
}

