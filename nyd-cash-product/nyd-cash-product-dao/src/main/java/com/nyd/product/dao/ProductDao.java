package com.nyd.product.dao;

import com.nyd.product.model.ProductInfo;

import java.util.List;

/**
 * Created by Dengw on 17/11/6.
 */
public interface ProductDao {
    void save(ProductInfo productInfo) throws Exception;

    void update(ProductInfo productInfo) throws Exception;

    List<ProductInfo> getObjectsByProductCode(String productCode) throws Exception;

    List<ProductInfo> getProducts() throws Exception;

    List<ProductInfo> getObjectsByBusiness(String businessCode) throws Exception;
}
