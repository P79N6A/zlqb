package com.nyd.order.dao.impl;

import com.nyd.order.dao.OrderWentongDao;
import com.nyd.order.dao.mapper.OrderWentongMapper;
import com.nyd.order.entity.OrderWentong;
import com.nyd.order.model.OrderWentongInfo;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2018/4/24
 **/
@Repository
public class OrderWentongDaoImpl implements OrderWentongDao{

    final Logger logger = LoggerFactory.getLogger(OrderWentongDaoImpl.class);
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Autowired
    private OrderWentongMapper orderWentongMapper;
    @Override
    public void save(OrderWentong orderWentong) throws Exception {
       crudTemplate.save(orderWentong);
    }

    @Override
    public List<OrderWentongInfo> getOrderWTByTime(String startDate, String endDate,String name,String mobile) {
        List<OrderWentongInfo> result = new ArrayList<>();
        Map map = new HashMap();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("name",name);
        map.put("mobile",mobile);
        List<OrderWentong> queryList = orderWentongMapper.selectByTime(map);

        if(queryList!=null) {
            for (OrderWentong orderWentong : queryList) {
                OrderWentongInfo info = new OrderWentongInfo();
                info.setMobile(orderWentong.getMobile());
                info.setUserId(orderWentong.getUserId());
                info.setOrderNo(orderWentong.getOrderNo());
                info.setLoanTime(DateFormatUtils.format(orderWentong.getLoanTime(),"yyyy-MM-dd HH:mm:ss"));
                info.setName(orderWentong.getName());
                result.add(info);
            }
        }
        return result;
    }
}
