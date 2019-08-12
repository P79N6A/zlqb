package com.nyd.order.dao.impl;

import com.nyd.order.dao.OrderDetailDao;
import com.nyd.order.entity.OrderDetail;
import com.nyd.order.model.OrderDetailInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Dengw on 2017/11/8
 */
@Repository
public class OrderDetailDaoImpl implements OrderDetailDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(OrderDetail orderDetail) throws Exception {
        crudTemplate.save(orderDetail);
    }

    @Override
    public void update(OrderDetailInfo orderDetailInfo) throws Exception {
        Criteria criteria = Criteria.from(OrderDetail.class)
                .whioutId()
                .where()
                .and("order_no", Operator.EQ,orderDetailInfo.getOrderNo())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        orderDetailInfo.setOrderNo(null);
        crudTemplate.update(orderDetailInfo,criteria);
    }

    @Override
    public List<OrderDetailInfo> getObjectsByOrderNo(String orderNo) throws Exception {
        Criteria criteria = Criteria.from(OrderDetail.class)
                .where()
                .and("order_no", Operator.EQ,orderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
        return crudTemplate.find(orderDetailInfo,criteria);
    }

    @Override
    public List<OrderDetailInfo> getObjectsByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(OrderDetail.class)
                .where()
                .and("user_id", Operator.EQ,userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
        return crudTemplate.find(orderDetailInfo,criteria);
    }

    @Override
    public List<OrderDetailInfo> getOrderDetailsByIdCardNo(String idNumber) throws Exception {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 向前减去7天
        calendar.add(Calendar.DAY_OF_MONTH,-7);
        date = calendar.getTime();
        Criteria criteria = Criteria.from(OrderDetail.class)
                .where()
                .and("id_number", Operator.RLIKE,idNumber)
                .and("create_time",Operator.GTE,new Timestamp(date.getTime()))
                .and("create_time",Operator.LTE,new Timestamp(new Date().getTime()))
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
        return crudTemplate.find(orderDetailInfo,criteria);
    }

    @Override
    public List<OrderDetailInfo> getOrderDetailsByMobile(String mobile) throws Exception {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 向前减去7天
        calendar.add(Calendar.DAY_OF_MONTH,-7);
        date = calendar.getTime();
        Criteria criteria = Criteria.from(OrderDetail.class)
                .where()
                .and("mobile", Operator.EQ,mobile)
                .and("create_time",Operator.GTE,new Timestamp(date.getTime()))
                .and("create_time",Operator.LTE,new Timestamp(new Date().getTime()))
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
        return crudTemplate.find(orderDetailInfo,criteria);
    }
}
