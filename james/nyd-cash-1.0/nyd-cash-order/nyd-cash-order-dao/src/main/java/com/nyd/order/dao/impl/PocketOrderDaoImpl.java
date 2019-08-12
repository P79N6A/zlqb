/**
* @Title: PocketOrderDaoImpl.java
* @Package com.nyd.capital.dao.impl
* @Description: TODO
* @author chenjqt
* @date 2018年9月29日
* @version V1.0
*/
package com.nyd.order.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.nyd.order.dao.PocketOrderDao;
import com.nyd.order.entity.PockerOrderEntity;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;

/**
* @ClassName: PocketOrderDaoImpl
* @Description: TODO
* @author chenjqt
* @date 2018年9月29日
*
*/
@Repository
public class PocketOrderDaoImpl implements PocketOrderDao {
	@Resource(name = "mysql")
	private CrudTemplate crudTemplate;

	@Override
	public void save(PockerOrderEntity entity) throws Exception {
		crudTemplate.save(entity);
	}

	@Override
	public List<PockerOrderEntity> find(String orderNo) throws Exception {
		Criteria criteria = Criteria.from(PockerOrderEntity.class)
				.where()
				.and("order_no", Operator.EQ, orderNo)
				.and("delete_flag", Operator.EQ, 0)
				.endWhere();
		PockerOrderEntity p = new PockerOrderEntity();
		return crudTemplate.find(p, criteria);
	}

	@Override
	public void updateByOrderNo(PockerOrderEntity entity) throws Exception {
		Criteria criteria = Criteria.from(PockerOrderEntity.class)
				.whioutId()
				.where()
				.and("order_no", Operator.EQ, entity.getOrderNo())
				.and("delete_flag", Operator.EQ, 0).endWhere();
//		entity.setOrderNo(null);
		crudTemplate.update(entity, criteria);
	}

	@Override
	public List<PockerOrderEntity> getPockerOrderEntityByPocketNo(String pocketNo) throws Exception {
		Criteria criteria = Criteria.from(PockerOrderEntity.class).where().and("pocket_no", Operator.EQ, pocketNo)
				.and("delete_flag", Operator.EQ, 0).endWhere();
		return crudTemplate.find(new PockerOrderEntity(), criteria);
	}
	
	@Override
	public List<PockerOrderEntity> kdlcQueryCreateOrder() throws Exception {
		Criteria criteria = Criteria.from(PockerOrderEntity.class)
				.where()
				.and("order_status", Operator.EQ,1)
				.and("delete_flag", Operator.EQ, 0)
				.endWhere();
		PockerOrderEntity p = new PockerOrderEntity();
		return crudTemplate.find(p, criteria);
	}
}
