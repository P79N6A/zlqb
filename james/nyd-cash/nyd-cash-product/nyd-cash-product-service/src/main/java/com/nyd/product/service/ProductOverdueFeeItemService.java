package com.nyd.product.service;

import com.nyd.product.model.ProductOverdueFeeItemInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 17/11/7.
 */
public interface ProductOverdueFeeItemService {
    ResponseData saveOverdueFeeItemInfo(ProductOverdueFeeItemInfo productOverdueFeeItemInfo);

    ResponseData updateOverdueFeeItemInfo(ProductOverdueFeeItemInfo productOverdueFeeItemInfo);

    ResponseData<ProductOverdueFeeItemInfo> getOverdueFeeItemInfoByProductCode(String productCode);
}
