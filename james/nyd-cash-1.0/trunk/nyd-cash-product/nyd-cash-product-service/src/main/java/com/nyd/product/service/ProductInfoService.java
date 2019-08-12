package com.nyd.product.service;

import com.nyd.product.model.ProductInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 17/11/6.
 */
public interface ProductInfoService {
    ResponseData saveProductInfo(ProductInfo productInfo);

    ResponseData updateProductInfo(ProductInfo productInfo);

    ResponseData<ProductInfo> getProductInfoByProductCode(String productCode);
}
