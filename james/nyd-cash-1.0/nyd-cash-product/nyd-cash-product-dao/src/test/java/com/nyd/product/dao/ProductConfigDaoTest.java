package com.nyd.product.dao;

import com.nyd.product.entity.ProductConfig;
import com.nyd.product.model.ProductConfigInfo;
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
public class ProductConfigDaoTest {
    @Autowired
    private ProductConfigDao productConfigDao;

    @Test
    public void testSaveProduct() throws Exception {
        ProductConfigInfo productConfig = new ProductConfigInfo();
        productConfig.setProductCode("test2");
        productConfig.setIsBeheadInterest(1);
        productConfig.setBeheadPercentage(BigDecimal.valueOf(0.02));
        productConfig.setFastAuditFeeRate(BigDecimal.valueOf(0.02));
        productConfig.setAccountManageFeeRate(BigDecimal.valueOf(0.02));
        productConfig.setIdentityVerifyFeeRate(BigDecimal.valueOf(0.02));
        productConfig.setMobileVerifyFeeRate(BigDecimal.valueOf(0.02));
        productConfig.setBankVerifyFeeRate(BigDecimal.valueOf(0.02));
        productConfig.setCreditServiceFeeRate(BigDecimal.valueOf(0.02));
        productConfig.setInformationPushFeeRate(BigDecimal.valueOf(0.02));
        productConfig.setSyntheticalFeeRate(BigDecimal.valueOf(0.02));
        productConfig.setSlidingFeeRate(BigDecimal.valueOf(0.02));
        productConfigDao.save(productConfig);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductConfigInfo productConfig = new ProductConfigInfo();
        productConfig.setProductCode("test2");
        productConfigDao.update(productConfig);
    }

    @Test
    public void testGetProduct() throws Exception {
        System.out.printf(productConfigDao.getObjectsByProductCode("test2").get(0).toString());
    }
}
