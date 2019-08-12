/**
 * Project Name:nyd-cash-pay-dao
 * File Name:CardBinDaoImpl.java
 * Package Name:com.nyd.pay.dao.impl
 * Date:2018年9月18日下午8:31:48
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.pay.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.nyd.pay.dao.CardBinDao;
import com.nyd.pay.entity.CardBin;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;

/**
 * ClassName:CardBinDaoImpl <br/>
 * Date:     2018年9月18日 下午8:31:48 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Repository
public class CardBinDaoImpl implements CardBinDao {
	@Resource(name="mysql")
	private CrudTemplate crudTemplate;
	
	@Override
	public List<CardBin> queryCardBinList(String binNo) throws Exception {
		Criteria criteria = Criteria.from(CardBin.class)
				.where().and("BIN_NO", Operator.EQ, binNo)
				.endWhere()
				.groupBy("BANK_CODE");
		CardBin cardBin = new CardBin();
		List<CardBin> list = crudTemplate.find(cardBin, criteria);
		if(null != list && !list.isEmpty()) {
			return list;
		}
		return null;
	}

}

