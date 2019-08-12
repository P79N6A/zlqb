/**
 * Project Name:nyd-cash-capital-model
 * File Name:JxCommonResponse.java
 * Package Name:com.nyd.capital.model.jx
 * Date:2018年8月1日上午9:46:27
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName:JxCommonResponse <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月1日 上午9:46:27 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class JxCommonResponse implements Serializable{
	private String statusCode;
	private String message;
}

