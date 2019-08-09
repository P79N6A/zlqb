package com.nyd.product.dao;

import com.nyd.product.model.ProductFundRelInfo;

import java.util.List;

/**
 * Created by zhujx on 2017/11/22.
 */
public interface ProductFundRelDao {

    void save(ProductFundRelInfo productFundRelInfo) throws Exception;

    void updateByProductCode(ProductFundRelInfo productFundRelInfo) throws Exception;

    List<ProductFundRelInfo> getProductFundRelInfo(String productCode) throws Exception;

}
