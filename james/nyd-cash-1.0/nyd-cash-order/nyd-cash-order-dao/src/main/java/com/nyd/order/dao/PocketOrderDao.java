/**
* @Title: PocketOrderDao.java
* @Package com.nyd.capital.dao
* @Description: TODO
* @author chenjqt
* @date 2018年9月29日
* @version V1.0
*/
package com.nyd.order.dao;

import java.util.List;

import com.nyd.order.entity.PockerOrderEntity;


/**
* @ClassName: PocketOrderDao
* @Description: TODO
* @author chenjqt
* @date 2018年9月29日
*
*/
public interface PocketOrderDao {

	public void save(PockerOrderEntity pockerOrderEntity) throws Exception;
	
	public List<PockerOrderEntity> find(String orderNo) throws Exception;
	
	public void updateByOrderNo(PockerOrderEntity entity) throws Exception;

	public List<PockerOrderEntity> getPockerOrderEntityByPocketNo(String pocketNo) throws Exception;
	
	public List<PockerOrderEntity> kdlcQueryCreateOrder() throws Exception;
}
