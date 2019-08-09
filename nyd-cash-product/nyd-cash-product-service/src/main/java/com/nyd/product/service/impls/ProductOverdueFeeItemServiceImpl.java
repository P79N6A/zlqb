package com.nyd.product.service.impls;

import com.nyd.product.dao.ProductOverdueFeeItemDao;
import com.nyd.product.model.ProductOverdueFeeItemInfo;
import com.nyd.product.service.ProductOverdueFeeItemService;
import com.nyd.product.service.consts.ProductConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dengw on 17/11/7.
 */
@Service
public class ProductOverdueFeeItemServiceImpl implements ProductOverdueFeeItemService {
    private static Logger LOGGER = LoggerFactory.getLogger(ProductOverdueFeeItemServiceImpl.class);

    @Autowired
    private ProductOverdueFeeItemDao productOverdueFeeItemDao;

    @Override
    public ResponseData saveOverdueFeeItemInfo(ProductOverdueFeeItemInfo productOverdueFeeItemInfo) {
        ResponseData response = ResponseData.success();
        try {
            productOverdueFeeItemDao.save(productOverdueFeeItemInfo);
        } catch (Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("save productOverdueFeeItemInfo error! productCode= "+productOverdueFeeItemInfo.getProductCode(), e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseData updateOverdueFeeItemInfo(ProductOverdueFeeItemInfo productOverdueFeeItemInfo) {
        ResponseData response = ResponseData.success();
        try {
            productOverdueFeeItemDao.update(productOverdueFeeItemInfo);
        } catch (Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("update productOverdueFeeItemInfo error! productCode= "+productOverdueFeeItemInfo.getProductCode(), e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseData<ProductOverdueFeeItemInfo> getOverdueFeeItemInfoByProductCode(String productCode) {
        ResponseData response = ResponseData.success();
        try {
            ProductOverdueFeeItemInfo overdueFeeItemInfo = new ProductOverdueFeeItemInfo();
            List<ProductOverdueFeeItemInfo> overdueFeeItemList = productOverdueFeeItemDao.getObjectsByProductCode(productCode);
            if(overdueFeeItemList != null && overdueFeeItemList.size()>0){
                overdueFeeItemInfo = overdueFeeItemList.get(0);
            }
            response.setData(overdueFeeItemInfo);
        } catch (Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("get ProductOverdueFeeItemInfo error! productCode= "+productCode, e.getMessage());
        }
        return response;
    }
}
