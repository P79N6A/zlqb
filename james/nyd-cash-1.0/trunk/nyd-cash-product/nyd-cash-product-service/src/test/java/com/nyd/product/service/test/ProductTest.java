package com.nyd.product.service.test;

import com.nyd.product.api.ProductContract;
import com.nyd.product.service.ProductOverdueFeeItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by Lait on 2017/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/nyd/product/configs/service/xml/nyd-product-service-local.xml"})
public class ProductTest {

    @Autowired
    private ProductOverdueFeeItemService service;

    @Autowired
    ProductContract productApi;

    @Test
    public void testCreate() throws Exception {

        System.out.printf(productApi.getProductInfo("test2").getData().toString());

      /*  ProductOverdueFeeItemInfo info = new ProductOverdueFeeItemInfo();
        info.setFinanceProductId("test1");
        info.setFirstGearOverdueFeeDays(15);
        info.setFirstGearOverdueRate(BigDecimal.valueOf(0.002));
        info.setSecondGearOverdueFeeDays(35);
        info.setSecondGearOverdueRate(BigDecimal.valueOf(0.003));
        System.out.printf(service.getOverdueFeeItemInfoByProductId("test1").toString());*/
        //service.updateOverdueFeeItemInfo(info);
        //service.savaOverdueFeeItemInfo(info);
        //service.updateOverdueFeeItemInfo(info);
//        Production productionInfos = new Production();
//        productionInfos.setCount(100);
//        //productionInfos.setAreaCode("0589");
//        productionInfos.setPtime(new Date(new java.util.Date().getTime()));
//        productionInfos.setGtime(new Date(2020, 12, 20));
//        productionInfos.setBrand("yuetu");
//        productionInfos.setModel("KFR-35GW/DY");
//        productionInfos.setBatch(1);
//        productionService.save(productionInfos);
        //productService.produceProduct(productInfos);

    }










}
