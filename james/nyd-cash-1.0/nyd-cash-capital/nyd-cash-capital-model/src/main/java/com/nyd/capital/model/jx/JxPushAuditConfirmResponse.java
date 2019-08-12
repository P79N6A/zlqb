/**
 * Project Name:nyd-cash-capital-model
 * File Name:JxpushAuditConfirmResponse.java
 * Package Name:com.nyd.capital.model.jx
 * Date:2018年7月30日下午12:05:01
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.model.jx;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:JxpushAuditConfirmResponse <br/>
 * Function: 即信推单外审确认响应参数
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年7月30日 下午12:05:01 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class JxPushAuditConfirmResponse extends JxCommonResponse implements Serializable {
	
	private static final long serialVersionUID = 7376271283505780490L;

	//标ID  文档是long,这里要改成string
	private String loanId;
}

