/**
 * Project Name:nyd-cash-pay-dao
 * File Name:CardBinDao.java
 * Package Name:com.nyd.pay.dao
 * Date:2018年9月18日下午8:30:31
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.pay.dao;
/**
 * ClassName:CardBinDao <br/>
 * Date:     2018年9月18日 下午8:30:31 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */

import java.util.List;

import com.nyd.pay.entity.CardBin;

public interface CardBinDao {

	//根据卡Bin去查询卡的缩写
	public List<CardBin> queryCardBinList(String binNo) throws Exception;
}

