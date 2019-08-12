/**
 * Project Name:nyd-cash-pay-service
 * File Name:OrderChannelServiceImp.java
 * Package Name:com.creativearts.nyd.pay.service.changjie.imp
 * Date:2018年9月12日上午10:38:38
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.pay.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.nyd.pay.dao.ThirdPartyPaymentChannelDao;
import com.nyd.pay.entity.ThirdPartyPaymentChannel;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;

/**
 * ClassName:OrderChannelServiceImp <br/>
 * Date:     2018年9月12日 上午10:38:38 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Repository
public class ThirdPartyPaymentChannelImpl implements ThirdPartyPaymentChannelDao {
	@Resource(name="mysql")
    private CrudTemplate crudTemplate;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public void save(ThirdPartyPaymentChannel thirdPaymentInfo) throws Exception {
		crudTemplate.save(thirdPaymentInfo);
	}
	/**
	 * 
	 * 查询订单
	 * @throws Exception 
	 * @see com.nyd.pay.dao.ThirdPartyPaymentChannelDao#queryThirdPartyOrderByOrderId(java.lang.String)
	 */
	@Override
	public ThirdPartyPaymentChannel queryThirdPartyOrderByTransId(String transId) throws Exception {
		Criteria criteria = Criteria.from(ThirdPartyPaymentChannel.class)
				.where().and("trans_id", Operator.EQ, transId)
				.and("delete_flag",Operator.EQ,0)
				.endWhere();
		ThirdPartyPaymentChannel thirdPartyOrder = new ThirdPartyPaymentChannel();
		List<ThirdPartyPaymentChannel> list = crudTemplate.find(thirdPartyOrder, criteria);
		if(null != list && !list.isEmpty()) {
			thirdPartyOrder = list.get(0);
			return thirdPartyOrder;
		}
		return null;
	}
	
	/**
	 * 
	 * 修改订单状态
	 * @throws Exception 
	 * @see com.nyd.pay.dao.ThirdPartyPaymentChannelDao#updateThirdPartyOrderStatus(com.nyd.pay.entity.ThirdPartyPaymentChannel)
	 */
	@Override
	public void updateThirdPartyOrderStatus(ThirdPartyPaymentChannel thirdPay) throws Exception {
		crudTemplate.update(thirdPay,Criteria.from(ThirdPartyPaymentChannel.class));		
	}
	
	/**
	 * 
	 * 查询处理中的交易
	 * @throws Exception 
	 * @see com.nyd.pay.dao.ThirdPartyPaymentChannelDao#queryOrderStatus()
	 */
	@Override
	public List<ThirdPartyPaymentChannel> queryOrderStatus() throws Exception {
		Criteria criteria = Criteria.from(ThirdPartyPaymentChannel.class)
				.where()
				.and("channel_code", Operator.EQ, "CJ")
				.and("create_time", Operator.LTE,dateToString(new Date()))
				.and("status", Operator.EQ, 2)
				.and("delete_flag",Operator.EQ,0)
				.endWhere()
				.orderBy("create_time desc")
				.limit(0, 1000);
		ThirdPartyPaymentChannel thirdPartyOrder = new ThirdPartyPaymentChannel();
		List<ThirdPartyPaymentChannel> list = crudTemplate.find(thirdPartyOrder, criteria);
		return list;
	}
	
	private String dateToString(Date date) {
		long time = date.getTime() - 10*60*1000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}
}

