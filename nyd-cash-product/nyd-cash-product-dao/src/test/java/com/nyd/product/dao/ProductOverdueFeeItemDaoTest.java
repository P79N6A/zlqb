package com.nyd.product.dao;

import com.nyd.product.model.ProductOverdueFeeItemInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Created by Dengw on 2017/11/7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/product/configs/dao/xml/spring-mybatis.xml"})
public class ProductOverdueFeeItemDaoTest {
    @Autowired
    private ProductOverdueFeeItemDao productOverdueFeeItemDao;

    @Test
    public void testSaveProduct() throws Exception {
        ProductOverdueFeeItemInfo productOverdueFeeItem = new ProductOverdueFeeItemInfo();
        productOverdueFeeItem.setProductCode("test2");
        productOverdueFeeItem.setGearOverdueFeeDays(7);
        productOverdueFeeItem.setFirstGearOverdueRate(BigDecimal.valueOf(0.5));
        productOverdueFeeItem.setMaxOverdueFeeRate(new BigDecimal(1.8));
        productOverdueFeeItem.setSecondGearOverdueRate(BigDecimal.valueOf(0.8));
        productOverdueFeeItemDao.save(productOverdueFeeItem);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductOverdueFeeItemInfo productOverdueFeeItem = new ProductOverdueFeeItemInfo();
        productOverdueFeeItem.setProductCode("test2");
        productOverdueFeeItem.setSecondGearOverdueRate(BigDecimal.valueOf(0.6));
        productOverdueFeeItemDao.update(productOverdueFeeItem);
    }

    @Test
    public void testGetProduct() throws Exception {
        System.out.printf(productOverdueFeeItemDao.getObjectsByProductCode("test1").get(0).toString());
    }
}
