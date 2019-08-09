package com.nyd.order.dao;

import com.nyd.order.entity.OrderDetail;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderStatusLogInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Created by zhujx on 2017/11/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/order/configs/dao/xml/spring-mybatis.xml"})
public class OrderDetailDaoTest {

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Test
    public void testSaveOrderDetail() throws Exception {

        OrderDetail orderDetailInfo = new OrderDetail();
        orderDetailInfo.setOrderNo("jhg3288329");
        orderDetailInfo.setUserId("jjjj");
        orderDetailInfo.setMobile("18302187777");
        orderDetailInfo.setIdType("SFZ");
        orderDetailInfo.setIdNumber("32087619990928");
        orderDetailInfo.setRealName("小龍女");
        orderDetailInfo.setSource("app");
        orderDetailInfo.setProductCode("2017ghsjhds");
        orderDetailInfo.setIsBeheadInterest(0);
        orderDetailInfo.setBeheadPercentage(new BigDecimal(0));
        orderDetailInfo.setProductType(0);
        orderDetailInfo.setProductPeriods(1);
        orderDetailInfo.setInterestRate(new BigDecimal(0));
        orderDetailInfo.setFastAuditFee(new BigDecimal(0));
        orderDetailInfo.setAccountManageFee(new BigDecimal(0));
        orderDetailInfo.setIdentityVerifyFee(new BigDecimal(0));

        orderDetailDao.save(orderDetailInfo);
    }

    @Test
    public void testUpdateOrderDetail() throws Exception {
        OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
        orderDetailInfo.setOrderNo("jhg3288329");
        orderDetailInfo.setRealName("大龍女");

        orderDetailDao.update(orderDetailInfo);

    }

    @Test
    public void testGetOrderDetailInfo() throws Exception {
        System.out.printf(orderDetailDao.getObjectsByOrderNo("jhg3288329").get(0).toString());
    }
    @Test
    public void testGetOrderDetail() throws Exception {
        System.out.println( orderDetailDao.getOrderDetailsByIdCardNo("320382"));
    }
}
