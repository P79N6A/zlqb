package com.nyd.order.dao;

import com.nyd.order.entity.Order;
import com.nyd.order.model.OrderInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/order/configs/dao/xml/spring-mybatis.xml"})
public class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    public void testSaveOrder() throws Exception {
        Order orderInfo = new Order();

        orderInfo.setUserId("jjjj");
        orderInfo.setOrderNo("jhg3288329");
        orderInfo.setLoanAmount(new BigDecimal("1000"));
        orderInfo.setRealLoanAmount(new BigDecimal("900"));
        orderInfo.setBorrowTime(0);
        orderInfo.setOrderStatus(30);
        orderInfo.setAuditReason("hhhj");

        orderDao.save(orderInfo);
    }

    @Test
    public void testUpdateOrder() throws Exception {
        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setUserId("hhhh");
        orderInfo.setOrderNo("jhg3288329");
        orderInfo.setLoanAmount(new BigDecimal("1000"));
        orderInfo.setRealLoanAmount(new BigDecimal("900"));
        orderInfo.setBorrowTime(0);
        orderInfo.setOrderStatus(50);
        orderInfo.setAuditReason("hhhj");

        orderDao.update(orderInfo);
    }

    @Test
    public void testGetOrderInfo() throws Exception {
        System.out.printf(orderDao.getObjectsByOrderNo("101512455379006001").get(0).toString());
//        System.out.println(orderDao.getObjectsByUserId("173341400001"));
    }


}
