/**
 * Project Name:nyd-cash-capital-model
 * File Name:JxQueryPushStatusResponse.java
 * Package Name:com.nyd.capital.model.jx
 * Date:2018年7月30日上午11:01:01
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.model.jx;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:JxQueryPushStatusResponse <br/>
 * Function: 即信推单查询接口相应结果参数
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年7月30日 上午11:01:01 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class JxQueryPushStatusResponse extends JxCommonResponse implements Serializable{
	/**
	 * serialVersionUID:序列化ID.
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 5174327493755746402L;

    //渠道推单通道是否开启
	private boolean allowPass;
	//用户在资金平台的注册用户Id  memberId
	private String memberId;
	//用户是否开户
	private boolean hasOpenAccount;
	//用户是否绑卡
	private boolean bankCardHasBound;
	//用户绑定信息是否一致
	private boolean bindingCardDifferent;
	//交易密码是否设置
	private boolean tradePasswordHasSet;
	//是否已开通代扣
	private boolean authorizedDeductionDelegationHasSigned;
	//还款是否授权
	private boolean repaymentDelegationHasSigned;
	//缴费授权
	private boolean paymentDelegationHasSigned;
	//身份证号和手机号是否绑定
	private boolean idCardIsUsed;
	/**
	 * 是否电子签章认证
	 */
	private boolean accreditFdd;
}

