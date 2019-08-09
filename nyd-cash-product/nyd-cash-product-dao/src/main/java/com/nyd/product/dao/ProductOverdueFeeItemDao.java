package com.nyd.product.dao;


import com.nyd.product.model.ProductOverdueFeeItemInfo;

import java.util.List;

/**
 * Created by Dengw on 17/11/6.
 */
public interface ProductOverdueFeeItemDao {
    void save(ProductOverdueFeeItemInfo productOverdueFeeItemInfo) throws Exception;

    void update(ProductOverdueFeeItemInfo productOverdueFeeItemInfo) throws Exception;

    List<ProductOverdueFeeItemInfo> getObjectsByProductCode(String productCode) throws Exception;
}
