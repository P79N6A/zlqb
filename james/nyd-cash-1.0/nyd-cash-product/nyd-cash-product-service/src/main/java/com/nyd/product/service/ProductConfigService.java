package com.nyd.product.service;

import com.nyd.product.model.ProductConfigInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 17/11/7.
 */
public interface ProductConfigService {
    ResponseData saveProductConfigInfo(ProductConfigInfo productConfigInfo);

    ResponseData updateProductConfigInfo(ProductConfigInfo productConfigInfo);

    ResponseData<ProductConfigInfo> getProductConfigInfoByProductCode(String productCode);
}
