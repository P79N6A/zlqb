package com.nyd.product.dao;

import com.nyd.product.entity.Product;
import com.nyd.product.model.ProductInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Dengw on 2017/11/7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/product/configs/dao/xml/spring-mybatis.xml"})
public class ProductDaoTest {
    @Autowired
    private ProductDao productDao;

    @Test
    public void testSaveProduct() throws Exception {
        ProductInfo product = new ProductInfo();
        product.setProductCode("test5");
        product.setProductName("测试5号");
        product.setIsInUse(1);
        productDao.save(product);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductInfo product = new ProductInfo();
        product.setProductCode("test5");
        productDao.update(product);
    }

    @Test
    public void testGetProduct() throws Exception {
        System.out.printf(productDao.getObjectsByProductCode("test5").get(0).toString());
    }
}
