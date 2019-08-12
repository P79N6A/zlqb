package com.nyd.order.dao;

import com.nyd.order.entity.OrderStatusLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhujx on 2017/11/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/order/configs/dao/xml/spring-mybatis.xml"})
public class OrderStatusLogTest {

    @Autowired
    private OrderStatusLogDao orderStatusLogDao;


    @Test
    public void testSaveOrderStatusLog() throws Exception {
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        orderStatusLog.setBeforeStatus(10);
        orderStatusLog.setAfterStatus(20);
        orderStatusLogDao.save(orderStatusLog);
    }

    @Test
    public void testGetOrderStatusLogInfo() throws Exception {
        System.out.printf(orderStatusLogDao.getObjectsByOrderNo("123456").get(0).toString());
    }
}
