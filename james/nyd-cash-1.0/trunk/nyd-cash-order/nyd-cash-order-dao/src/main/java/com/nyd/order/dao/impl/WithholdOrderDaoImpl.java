package com.nyd.order.dao.impl;

import com.nyd.order.dao.WithholdOrderDao;
import com.nyd.order.entity.WithholdOrder;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;

/**
 * @author liuqiu
 */
@Repository
public class WithholdOrderDaoImpl implements WithholdOrderDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(WithholdOrder order) throws Exception {
        crudTemplate.save(order);
    }

    @Override
    public void update(WithholdOrder orderInfo) throws Exception {
        Criteria criteria = Criteria.from(WithholdOrder.class)
                .whioutId()
                .where().and("member_id", Operator.EQ,orderInfo.getMemberId())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        orderInfo.setMemberId(null);
        crudTemplate.update(orderInfo,criteria);
    }

    @Override
    public List<WithholdOrder> getObjectsByMemberId(String memberId) throws Exception {
        Criteria criteria = Criteria.from(WithholdOrder.class)
                .where().and("member_id", Operator.EQ,memberId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        WithholdOrder orderInfo = new WithholdOrder();
        return crudTemplate.find(orderInfo,criteria);
    }

    @Override
    public List<WithholdOrder> getObjectsPayOrderNo(String payOrderNo) throws Exception {
        Criteria criteria = Criteria.from(WithholdOrder.class)
                .where().and("pay_order_no", Operator.EQ,payOrderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        WithholdOrder orderInfo = new WithholdOrder();
        return crudTemplate.find(orderInfo,criteria);
    }
}
