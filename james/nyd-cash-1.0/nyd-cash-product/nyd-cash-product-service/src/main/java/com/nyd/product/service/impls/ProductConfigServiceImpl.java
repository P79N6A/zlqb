package com.nyd.product.service.impls;

import com.nyd.product.dao.ProductConfigDao;
import com.nyd.product.model.ProductConfigInfo;
import com.nyd.product.service.ProductConfigService;
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
public class ProductConfigServiceImpl implements ProductConfigService {
    private static Logger LOGGER = LoggerFactory.getLogger(ProductConfigServiceImpl.class);

    @Autowired
    private ProductConfigDao productConfigDao;

    @Override
    public ResponseData saveProductConfigInfo(ProductConfigInfo productConfigInfo) {
        ResponseData response = ResponseData.success();
        try {
            productConfigDao.save(productConfigInfo);
        } catch (Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("save productConfigInfo error! productId= "+productConfigInfo.getProductCode(), e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseData updateProductConfigInfo(ProductConfigInfo productConfigInfo) {
        ResponseData response = ResponseData.success();
        try {
            productConfigDao.update(productConfigInfo);
        } catch (Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("update productConfigInfo error! productId= "+productConfigInfo.getProductCode(), e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseData<ProductConfigInfo> getProductConfigInfoByProductCode(String productCode) {
        ResponseData response = ResponseData.success();
        try {
            ProductConfigInfo productConfigInfo = new ProductConfigInfo();
            List<ProductConfigInfo> productConfigList = productConfigDao.getObjectsByProductCode(productCode);
            if(productConfigList != null && productConfigList.size()>0){
                productConfigInfo = productConfigList.get(0);
            }
            response.setData(productConfigInfo);
        } catch (Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("get productConfigInfo error! productCode= "+productCode, e.getMessage());
        }
        return response;
    }
}
