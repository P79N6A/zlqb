package com.nyd.settlement.dao.impl;

import com.nyd.settlement.entity.FailOrderKzjr;
import com.nyd.settlement.entity.Order;
import com.nyd.settlement.entity.OrderCancel;
import com.nyd.settlement.dao.OrderCancelDao;
import com.nyd.settlement.entity.OrderStatusLog;
import com.nyd.settlement.model.dto.OrderCancelDto;
import com.nyd.settlement.model.dto.OrderDto;
import com.nyd.settlement.model.dto.OrderStatusLogDto;
import com.nyd.settlement.model.dto.QueryDto;
import com.nyd.settlement.model.vo.OrderCancelVo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author Peng
 * @create 2018-01-16 11:11
 **/
@Repository
public class OrderCancelDaoImpl implements OrderCancelDao{
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Override
    public List<OrderCancelVo> getOrderCancel(QueryDto dto) throws Exception {
        OrderCancelVo vo = new OrderCancelVo();
        Criteria criteria = Criteria.from(OrderCancel.class)
                .where()
                .and("order_no", Operator.EQ,dto.getOrderNo())
                .endWhere()
                .orderBy("create_time desc");
        return crudTemplate.find(vo,criteria);
    }

    @Override
    public void save(OrderCancelDto dto) throws Exception {
        OrderCancel orderCancel = new OrderCancel();
        BeanUtils.copyProperties(dto,orderCancel);
        crudTemplate.save(orderCancel);
    }

    @Override
    public void updateOrder(OrderDto dto) throws Exception {
        Criteria criteria = Criteria.from(Order.class)
                .whioutId()
                .where().and("order_no", Operator.EQ,dto.getOrderNo())
                .endWhere();
        crudTemplate.update(dto,criteria);
    }

    @Override
    public void deleteFailKzjr(String orderNo) throws Exception {
        FailOrderKzjr failOrderKzjr = new FailOrderKzjr();
        Criteria criteria = Criteria.from(FailOrderKzjr.class)
                .whioutId()
                .where()
                .and("order_no",Operator.EQ,orderNo)
                .endWhere();
        crudTemplate.delete(failOrderKzjr,criteria);
    }

    @Override
    public void saveOrderStatusLog(OrderStatusLogDto dto) throws Exception {
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        BeanUtils.copyProperties(dto,orderStatusLog);
        crudTemplate.save(orderStatusLog);
    }
}
