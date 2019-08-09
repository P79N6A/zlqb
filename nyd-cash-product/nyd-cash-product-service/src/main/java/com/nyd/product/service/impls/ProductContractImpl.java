package com.nyd.product.service.impls;

import com.nyd.product.api.ProductContract;
import com.nyd.product.dao.ProductConfigDao;
import com.nyd.product.dao.ProductDao;
import com.nyd.product.dao.ProductFundRelDao;
import com.nyd.product.dao.ProductOverdueFeeItemDao;
import com.nyd.product.dao.ProductZzlDao;
import com.nyd.product.dao.mapper.ProductFundRelMapper;
import com.nyd.product.model.FundInfo;
import com.nyd.product.model.ProductInfo;
import com.nyd.product.model.ProductInfoForZzlVO;
import com.nyd.product.model.ProductOverdueFeeItemInfo;
import com.nyd.product.service.consts.FundConsts;
import com.nyd.product.service.consts.ProductConsts;
import com.tasfe.framework.support.model.ResponseData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dengw on 2017/11/11.
 */
@Service(value = "productContract")
public class ProductContractImpl implements ProductContract {
    private static Logger LOGGER = LoggerFactory.getLogger(ProductContractImpl.class);

    @Autowired
    ProductDao productDao;

    @Autowired
    ProductConfigDao productConfigDao;

    @Autowired
    ProductOverdueFeeItemDao productOverdueFeeItemDao;

    @Autowired
    ProductFundRelDao productFundRelDao;

    @Autowired
    ProductFundRelMapper productFundRelMapper;
    
    @Autowired
    private ProductZzlDao productZzlDao;

    /**
     * 根据code查询金融产品信息
     * @param productCode
     * @return
     */
    @Override
    public ResponseData<ProductInfo> getProductInfo(String productCode) {
        LOGGER.info("begin to get productInfo，productCode is "+ productCode);
        ResponseData response = ResponseData.success();
        try{
            List<ProductInfo> productInfoList  = productDao.getObjectsByProductCode(productCode);
            if(productInfoList != null && productInfoList.size()>0) {
                ProductInfo productInfo = productInfoList.get(0);
                response.setData(productInfo);
            }
            LOGGER.info("get productInfo success by productCode !");
        }catch(Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("get productInfo failed, productCode = " + productCode, e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseData<List<ProductInfo>> getProductInfos() {
        LOGGER.info("begin to get productInfos");
        ResponseData response = ResponseData.success();
        try{
            List<ProductInfo> productInfoList  = productDao.getProducts();
            if(productInfoList != null && productInfoList.size()>0) {
                response.setData(productInfoList);
            }
            LOGGER.info("get productInfo success by productCode !");
        }catch(Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("get productInfo failed, productCode = " , e);
        }
        return response;
    }

    /**
     * 根据business查询金融产品信息
     * @param businessCode
     * @return
     */
    @Override
    public ResponseData<List<ProductInfo>> getProductInfoByBusiness(String businessCode) {
        LOGGER.info("begin to get productInfo，businessCode is "+ businessCode);
        ResponseData response = ResponseData.success();
        try {
            List<ProductInfo> productList = productDao.getObjectsByBusiness(businessCode);
            if(productList !=null && productList.size()>0){
                response.setData(productList);
            }
            LOGGER.info("get productInfo success by businessCode !");
        } catch (Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("get productInfo list error! businessCode = "+ businessCode, e.getMessage());
        }
        return response;
    }

    /**
     * 根据code查询金融产品逾期配置信息
     * @param productCode
     * @return
     */
    @Override
    public ResponseData<ProductOverdueFeeItemInfo> getProductOverdueFeeItemInfo(String productCode) {
        LOGGER.info("begin to get product OverdueFeeItemInfo，productCode is "+ productCode);
        ResponseData response = ResponseData.success();
        try{
            List<ProductOverdueFeeItemInfo> overdueFeeItemInfoList  = productOverdueFeeItemDao.getObjectsByProductCode(productCode);
            if(overdueFeeItemInfoList != null && overdueFeeItemInfoList.size()>0) {
                ProductOverdueFeeItemInfo productOverdueFeeItemInfo = overdueFeeItemInfoList.get(0);
                response.setData(productOverdueFeeItemInfo);
            }
            LOGGER.info("get product OverdueFeeItemInfo success by businessCode !");
        }catch(Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("get productOverdueFeeItemInfo failed, productCode = " + productCode, e.getMessage());
        }
        return response;
    }

    /**
     * 根据资金源配置关系，选择一个合适的资金源下单
     * @return
     */
    @Override
    public ResponseData<String> getFundInfo(String productCode) {
        LOGGER.info("begin to get fundInfo，productCode is "+ productCode);
        ResponseData response = ResponseData.success();
        String fundCode = FundConsts.WSM;
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("productCode",productCode);
            //设置当前时间格式为HH:mm:ss的字符串
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String curTime =  sdf.format(date).toString();
            map.put("curTime",curTime);
            FundInfo fund = productFundRelMapper.getFundInfo(map);
            if(fund != null){
                fundCode = fund.getFundCode();
            }
            LOGGER.info("get fundInfo success by productCode !");
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setData(fundCode);
        return response;
    }

	@Override
	public ProductInfoForZzlVO getProductForZzl(String productCode) {
		List<ProductInfoForZzlVO> list = null;
		try {
			list = productZzlDao.getProductInfoForZzl(productCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}

    /* @Override
    public ResponseData<ProductConfigInfo> getProductConfigInfo(String productCode) {
        ResponseData response = ResponseData.success();
        try{
            List<ProductConfigInfo> productConfigInfoList  = productConfigDao.getObjectsByProductCode(productCode);
            if(productConfigInfoList != null && productConfigInfoList.size()>0) {
                ProductConfigInfo productConfigInfo = productConfigInfoList.get(0);
                response.setData(productConfigInfo);
            }
        }catch(Exception e) {
            response = ResponseData.error(ProductConsts.DB_ERROR_MSG);
            LOGGER.error("get productConfigInfo failed, productCode = " + productCode, e.getMessage());
        }
        return response;
    }*/
}
