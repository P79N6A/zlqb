package com.nyd.product.api;

import com.nyd.product.model.ProductInfo;
import com.nyd.product.model.ProductInfoForZzlVO;
import com.nyd.product.model.ProductOverdueFeeItemInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by dengw 2017/11/8
 * 金融产品后端接口
 */
public interface ProductContract {
    /**
     * 根据code查询金融产品信息
     * @param productCode
     * @return
     */
    ResponseData<ProductInfo> getProductInfo(String productCode);

    /**
     * 查询所有金融产品信息
     * @return
     */
    ResponseData<List<ProductInfo>> getProductInfos();

    /**
     * 根据code查询金融产品逾期配置信息
     * @param productCode
     * @return
     */
    ResponseData<ProductOverdueFeeItemInfo> getProductOverdueFeeItemInfo(String productCode);

    /**
     * 根据business查询金融产品信息
     * @param businessCode
     * @return
     */
    ResponseData<List<ProductInfo>> getProductInfoByBusiness(String businessCode);

    /**
     * 路由合适的资金源
     * @param productCode
     * @return
     */
    ResponseData<String> getFundInfo(String productCode);
    
    /**
     * 获取中资联产品信息
     * @param productCode
     * @return
     */
    public ProductInfoForZzlVO getProductForZzl(String productCode);

}
