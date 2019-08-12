package com.nyd.product.service.impls;

import com.nyd.product.dao.ProductDao;
import com.nyd.product.model.ProductInfo;
import com.nyd.product.service.ProductInfoService;
import com.nyd.product.service.consts.ProductConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dengw on 17/11/6.
 */
@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    private static Logger LOGGER = LoggerFactory.getLogger(ProductInfoServiceImpl.class);
    @Autowired
    private ProductDao productDao;

    /**
     * 保存金融产品信息
     * @param productInfo
     * @return ResponseData
     */
    @Override
    public ResponseData saveProductInfo(ProductInfo productInfo) {
        ResponseData response = ResponseData.success();
        try {
            productDao.save(productInfo);
        } catch (Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("save productInfo error! productCode= "+productInfo.getProductCode(), e.getMessage());
        }
        return response;
    }

    /**
     * 更新金融产品信息
     * @param productInfo
     * @return ResponseData
     */
    @Override
    public ResponseData updateProductInfo(ProductInfo productInfo) {
        ResponseData response = ResponseData.success();
        try {
            productDao.update(productInfo);
        } catch (Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("update productInfo error! productCode= "+productInfo.getProductCode(), e.getMessage());
        }
        return response;
    }

    /**
     * 根据code查询金融产品信息
     * @param productCode
     * @return ResponseData
     */
    @Override
    public ResponseData<ProductInfo> getProductInfoByProductCode(String productCode) {
        ResponseData response = ResponseData.success();
        try {
            ProductInfo productInfo= new ProductInfo();
            List<ProductInfo> productList = productDao.getObjectsByProductCode(productCode);
            if(productList !=null && productList.size()>0){
                productInfo = productList.get(0);
            }
            response.setData(productInfo);
        } catch (Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("get productInfo error! productCode= "+productCode, e.getMessage());
        }
        return null;
    }
}
