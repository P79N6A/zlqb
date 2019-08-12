package com.nyd.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.ProductMapper;
import com.nyd.admin.dao.mapper.ProductOverdueFeeItemMapper;
import com.nyd.admin.entity.Product;
import com.nyd.admin.entity.ProductOverdueFeeItem;
import com.nyd.admin.model.ProductInfoVo;
import com.nyd.admin.model.ProductQueryVo;
import com.nyd.admin.model.ProductVo;
import com.nyd.admin.service.ProductService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.ProductMapStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Dengw on 2017/12/14
 */
@Service
public class ProductServiceImpl implements ProductService {
    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductOverdueFeeItemMapper productOverdueFeeItemMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public boolean saveProduct(ProductInfoVo vo) {
        boolean saveFlag = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String productCode = "nyd"+sdf.format(date);
        vo.setProductCode(productCode);
        Product product = ProductMapStruct.INSTANCE.ProductVo2Po(vo);
        ProductOverdueFeeItem overdueFeeItem = ProductMapStruct.INSTANCE.overdueVo2Po(vo);
        try {
            productMapper.insert(product);
            productOverdueFeeItemMapper.insert(overdueFeeItem);
            saveFlag = true;
        } catch (Exception e) {
            logger.error("保存金融产品失败",e);
        }
        return saveFlag;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public boolean updateProduct(ProductInfoVo vo) {
        boolean saveFlag = false;
        Product product = ProductMapStruct.INSTANCE.ProductVo2Po(vo);
        ProductOverdueFeeItem overdueFeeItem = ProductMapStruct.INSTANCE.overdueVo2Po(vo);
        try {
            productMapper.update(product);
            productOverdueFeeItemMapper.update(overdueFeeItem);
            saveFlag = true;
        } catch (Exception e) {
            logger.error("更新金融产品失败",e);
        }
        return saveFlag;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public ProductInfoVo getProductDetail(ProductQueryVo vo) {
        if(vo.getProductCode() == null){
            logger.info("金融产品查询参数为空");
            return null;
        }
        ProductInfoVo productInfoVo = new ProductInfoVo();
        try {
            Product product = productMapper.select(vo.getProductCode());
            ProductOverdueFeeItem overdueFeeItem = productOverdueFeeItemMapper.select(vo.getProductCode());
            productInfoVo = ProductMapStruct.INSTANCE.Po2ProductInfoVo(product);
            productInfoVo.setOverdueFine(overdueFeeItem.getOverdueFine());
            productInfoVo.setGearOverdueFeeDays(overdueFeeItem.getGearOverdueFeeDays());
            productInfoVo.setFirstGearOverdueRate(overdueFeeItem.getFirstGearOverdueRate());
            productInfoVo.setSecondGearOverdueRate(overdueFeeItem.getSecondGearOverdueRate());
            productInfoVo.setMaxOverdueFeeRate(overdueFeeItem.getMaxOverdueFeeRate());
        } catch (Exception e) {
            logger.error("获取金融产品详情失败",e);
        }
        return productInfoVo;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public PageInfo<ProductVo> findPage(ProductQueryVo vo) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Product product = new Product();
        product.setProductCode(vo.getProductCode());
        product.setProductName(vo.getProductName());
        product.setBusiness(vo.getBusiness());
        if(vo.getEndDate() == null){
            product.setEndDate(new Date());
        }else{
            product.setEndDate(sdf.parse(vo.getEndDate()));
        }
        if(vo.getStartDate() != null){
            product.setStartDate(sdf.parse(vo.getStartDate()));
        }
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize(), vo.getOrderBy());
        List<Product> list = productMapper.findList(product);
        PageInfo<Product> result = new PageInfo<>(list);
        return ProductMapStruct.INSTANCE.poPage2VoPage(result);
    }
}
