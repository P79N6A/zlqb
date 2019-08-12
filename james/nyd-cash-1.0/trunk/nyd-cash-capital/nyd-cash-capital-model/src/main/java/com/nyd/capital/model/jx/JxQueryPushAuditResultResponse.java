/**
 * Project Name:nyd-cash-capital-model
 * File Name:JxqueryPushAuditResultResponse.java
 * Package Name:com.nyd.capital.model.jx
 * Date:2018年7月30日下午12:08:34
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.model.jx;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:JxqueryPushAuditResultResponse <br/>
 * Function:推单外审结果查询响应参数
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年7月30日 下午12:08:34 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class JxQueryPushAuditResultResponse extends JxCommonResponse implements Serializable {
	private static final long serialVersionUID = -4166891079391906535L;
	
	//审核结果：1-借款提交;2-审核中;3-审核通过;4-审核不通过;5-待人工审核;9-已开标
	private Integer status;
	//审核意见
	private String auditDescription;
}

