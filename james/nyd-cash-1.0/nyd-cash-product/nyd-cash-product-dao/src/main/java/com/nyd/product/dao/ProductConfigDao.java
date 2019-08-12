package com.nyd.product.dao;

import com.nyd.product.model.ProductConfigInfo;

import java.util.List;

/**
 * Created by Dengw on 17/11/6.
 */
public interface ProductConfigDao {
    void save(ProductConfigInfo productConfigInfo) throws Exception;

    void update(ProductConfigInfo productConfigInfo) throws Exception;

    List<ProductConfigInfo> getObjectsByProductCode(String productCode) throws Exception;
}
