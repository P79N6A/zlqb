package com.nyd.order.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.nyd.order.dao.OrderDao;
import com.nyd.order.entity.Order;
import com.nyd.order.entity.OrderReviewed;
import com.nyd.order.model.OrderCheckQuery;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.OrderStatus;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Dengw on 2017/11/8.
 */
@Repository
public class OrderDaoImpl implements OrderDao{
	
	private Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(Order order) throws Exception {
        crudTemplate.save(order);
    }

    @Override
    public void update(OrderInfo orderInfo) throws Exception {
        Criteria criteria = Criteria.from(Order.class)
                .whioutId()
                .where().and("order_no", Operator.EQ,orderInfo.getOrderNo())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        crudTemplate.update(orderInfo,criteria);
    }

    @Override
    public void updateOrderNo(String orderNo,OrderInfo orderInfo) throws Exception {
        Criteria criteria = Criteria.from(Order.class)
                .whioutId()
                .where().and("order_no", Operator.EQ,orderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        crudTemplate.update(orderInfo,criteria);
    }

    @Override
    public List<OrderInfo> getObjectsByOrderNo(String orderNo) throws Exception {
        Criteria criteria = Criteria.from(Order.class)
                .where().and("order_no", Operator.EQ,orderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OrderInfo orderInfo = new OrderInfo();
        return crudTemplate.find(orderInfo,criteria);
    }

    @Override
    public List<OrderInfo> getObjectsByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(Order.class)
                .where().and("user_id", Operator.EQ,userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        OrderInfo orderInfo = new OrderInfo();
        return crudTemplate.find(orderInfo,criteria);
    }
    @Override
    public List<OrderInfo> getObjectsByUserIdAndAppName(String userId,String appName) throws Exception {
    	Criteria criteria = Criteria.from(Order.class)
    			.where().and("user_id", Operator.EQ,userId)
    			.and("app_name", Operator.EQ,appName)
    			.and("delete_flag",Operator.EQ,0)
    			.endWhere()
    			.orderBy("create_time desc");
    	OrderInfo orderInfo = new OrderInfo();
    	return crudTemplate.find(orderInfo,criteria);
    }

    @Override
    public List<OrderInfo> getLastOrderByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(Order.class)
                .where().and("user_id", Operator.EQ,userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("loan_time desc")
                .limit(0,1);
        OrderInfo orderInfo = new OrderInfo();
        return crudTemplate.find(orderInfo,criteria);
    }

    @Override
    public List<OrderInfo> getRepayOrderByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(Order.class)
                .where().and("user_id", Operator.EQ,userId)
                .and("order_status",Operator.LT,40)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OrderInfo orderInfo = new OrderInfo();
        return crudTemplate.find(orderInfo,criteria);
    }

    @Override
    public List<OrderInfo> getObjectsByIbankOrderNo(String ibankOrderNo) throws Exception {
        Criteria criteria = Criteria.from(Order.class)
                .where().and("ibank_order_no", Operator.EQ,ibankOrderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OrderInfo orderInfo = new OrderInfo();
        return crudTemplate.find(orderInfo,criteria);
    }

	@Override
	public List<OrderInfo> getOrderCheckQuery(OrderCheckQuery orderCheckQuery) throws Exception {
		Criteria criteria = Criteria.from(Order.class)
                .where().and("channel", Operator.EQ,orderCheckQuery.getAppName())
                .and("delete_flag",Operator.EQ,0)
                .and("assign_time",Operator.GTE,orderCheckQuery.getBeginTime())
                .and("assign_time",Operator.LTE,orderCheckQuery.getEndTime())
                .endWhere();
        OrderInfo orderInfo = new OrderInfo();
        return crudTemplate.find(orderInfo,criteria);
	}

    @Override
     public List<OrderInfo> getBorrowInfoByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(Order.class)
                .where().and("user_id", Operator.EQ,userId)
                .and("delete_flag",Operator.EQ,0)
                .and("order_status",Operator.EQ,50)
                .or("order_status",Operator.EQ,1200)
                .endWhere()
                .orderBy("create_time desc");
        OrderInfo orderInfo = new OrderInfo();
        return crudTemplate.find(orderInfo,criteria);
    }

    @Override
    public List<OrderInfo> getBorrowInfoByOrderNo(String orderNo) throws Exception {
        Criteria criteria = Criteria.from(Order.class)
                .where().and("order_no", Operator.EQ,orderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        OrderInfo orderInfo = new OrderInfo();
        return crudTemplate.find(orderInfo,criteria);
    }

	@Override
	public OrderInfo getCheckOrderSuccess(String userId) throws Exception {
		Criteria criteria = Criteria.from(Order.class)
                .where().and("user_id", Operator.EQ,userId)
                .endWhere().orderBy("loan_time desc");
        OrderInfo orderInfo = new OrderInfo();
		return crudTemplate.find(orderInfo,criteria).get(0);
	}

	@Override
	public void saveOrderReviewed(OrderReviewed reviewed) {
		try {
			crudTemplate.save(reviewed);
		} catch (Exception e) {
			logger.error("OrderReviewed保存异常:"+JSONObject.toJSONString(reviewed));
		}
	}

}
