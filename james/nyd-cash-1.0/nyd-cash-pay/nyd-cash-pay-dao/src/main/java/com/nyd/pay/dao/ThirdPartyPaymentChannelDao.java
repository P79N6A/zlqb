/**
 * Project Name:nyd-cash-pay-service
 * File Name:OrderChannelService.java
 * Package Name:com.creativearts.nyd.pay.service.changjie
 * Date:2018年9月12日上午10:37:20
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.pay.dao;

import java.util.List;

import com.nyd.pay.entity.ThirdPartyPaymentChannel;

/**
 * ClassName:对数据库操作<业务代码> <br/>
 * Date:     2018年9月12日 上午10:37:20 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public interface ThirdPartyPaymentChannelDao {

	/**
	 * 
	 * save:(创建订单). <br/>
	 * @author wangzhch
	 * @param thirdPaymentInfo
	 * @throws Exception 
	 * @since JDK 1.8
	 */
	void save(ThirdPartyPaymentChannel thirdPaymentInfo) throws Exception;

	/**
	 * 
	 * queryThirdPartyOrderByOrderId:(根据造艺订单号查询订单信息). <br/>
	 * @author wangzhch
	 * @param string
	 * @return
	 * @throws Exception 
	 * @since JDK 1.8
	 */
	ThirdPartyPaymentChannel queryThirdPartyOrderByTransId(String transId) throws Exception;

	/**
	 * 
	 * updateThirdPartyOrderStatus:(修改订单状态). <br/>
	 * @author wangzhch
	 * @param thirdPay
	 * @throws Exception 
	 * @since JDK 1.8
	 */
	void updateThirdPartyOrderStatus(ThirdPartyPaymentChannel thirdPay) throws Exception;

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

