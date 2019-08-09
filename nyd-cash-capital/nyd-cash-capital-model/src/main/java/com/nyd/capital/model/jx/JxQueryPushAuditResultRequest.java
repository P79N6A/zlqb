/**
 * Project Name:nyd-cash-capital-model
 * File Name:JxqueryPushAuditResultRequest.java
 * Package Name:com.nyd.capital.model.jx
 * Date:2018年7月30日下午12:07:16
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.model.jx;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:JxqueryPushAuditResultRequest <br/>
 * Function:推单外审结果查询请求参数
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年7月30日 下午12:07:16 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class JxQueryPushAuditResultRequest extends JxCommonRequest implements Serializable {
	
	private static final long serialVersionUID = 4479916265401269020L;

	//借款单ID
	private Long loanOrderId;
	//加密值
	private String sign;
}

