/**
* @Title: SearchOrderRequest.java
* @Package com.nyd.capital.model.kdlc
* @Description: TODO
* @author chenjqt
* @date 2018年9月29日
* @version V1.0
*/
package com.nyd.order.model.kdlc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @ClassName: SearchOrderRequest
* @Description: 口袋理财查询订单接口
* @author chenjqt
* @date 2018年9月29日
*
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchOrderModel {

	String project_name;
	
	String yur_ref;
	
	String sign;
	
	String user_id;
}
