/**
 * Project Name:nyd-cash-pay-service
 * File Name:ChangJieBindCardService.java
 * Package Name:com.creativearts.nyd.pay.service.changjie
 * Date:2018年9月4日下午6:39:42
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.service.changjie;
/**
 * ClassName:畅捷绑卡操作类 <br/>
 * Date:     2018年9月4日 下午6:39:42 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */

import java.util.List;

import com.creativearts.nyd.pay.model.business.changjie.ZftQuickPayMentVo;
import com.creativearts.nyd.pay.model.changjie.PaymentSmsConfirmRequest;
import com.nyd.pay.entity.ThirdPartyPaymentChannel;
import com.tasfe.framework.support.model.ResponseData;

public interface ChangJieQuickPayService {

	/**
	 * 
	 * paymentSmsConfirm:(支付确认接口). <br/>
	 * @author wangzhch
	 * @param paymentSmsConfirmRequest
	 * @return
	 * @since JDK 1.8
	 */
	ResponseData paymentSmsConfirm(PaymentSmsConfirmRequest paymentSmsConfirmRequest);

	/**
	 * 
	 * zftQuickPayment:(直接支付请求接口). <br/>
	 * @author wangzhch
	 * @param zftQuickPayMentVo
	 * @return
	 * @throws Exception 
	 * @since JDK 1.8
	 */
	ResponseData zftQuickPayment(ZftQuickPayMentVo zftQuickPayMentVo) throws Exception;


	/**
	 * 
	 * queryTrade:(向畅捷发起订单查询接口). <br/>
	 * @author wangzhch
	 * @param thirdPartyPaymentChannel
	 * @return
	 * @since JDK 1.8
	 */
	ResponseData queryTrade(ThirdPartyPaymentChannel thirdPartyPaymentChannel);

	/**
	 * 
	 * queryOrderStatus:(查询处理中的交易). <br/>
	 * @author wangzhch
	 * @return
	 * @throws Exception 
	 * @since JDK 1.8
	 */
	List<ThirdPartyPaymentChannel> queryOrderStatus() throws Exception;

}

