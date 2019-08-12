/**
* @Title: KdlcLoanRequest.java
* @Package com.nyd.capital.model.kdlc
* @Description: TODO
* @author chenjqt
* @date 2018年9月28日
* @version V1.0
*/
package com.nyd.order.model.kdlc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @ClassName: LoanRequest
* @Description: 口袋理财放款model
* @author chenjqt
* @date 2018年9月28日
*
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanModel {

	// 项目名
	String project_name;
	// 项目密码
	String pwd;
	// 放款订单号，固定30位
	String yur_ref;
	// 本地user_id
	String original_id;
	// 口袋理财用户id
	String user_id;
	// 姓名
	String real_name;
	// 银行id
	int bank_id;
	// 银行卡号
	String card_no;
	// 扣款金额（分）
	String money;
	// 扣款手续费（分）
	String fee;
	// 备注
	String pay_summary;
	// 签名
	String sign;
	// 用户身份证号
	String id_number;
	// 时间戳
	String timestamp;
}
