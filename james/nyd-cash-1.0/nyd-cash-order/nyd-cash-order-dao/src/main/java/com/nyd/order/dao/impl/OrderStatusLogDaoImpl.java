package com.nyd.order.dao.impl;

import com.nyd.order.dao.OrderStatusLogDao;
import com.nyd.order.entity.OrderStatusLog;
import com.nyd.order.model.OrderStatusLogInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 2017/11/9.
 */
@Repository
public class OrderStatusLogDaoImpl implements OrderStatusLogDao{

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;


    @Override
    public void save(OrderStatusLog orderStatusLog) throws Exception {
        crudTemplate.save(orderStatusLog);
    }



    @Override
    public List<OrderStatusLogInfo> getObjectsByOrderNo(String orderNo) throws Exception {
        Criteria criteria = Criteria.from(OrderStatusLog.class)
                .where().and("order_no", Operator.EQ,orderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OrderStatusLogInfo orderStatusLogInfo = new OrderStatusLogInfo();
        return crudTemplate.find(orderStatusLogInfo,criteria);
    }
}
