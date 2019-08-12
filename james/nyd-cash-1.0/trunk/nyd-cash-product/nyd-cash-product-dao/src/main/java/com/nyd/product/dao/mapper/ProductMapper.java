package com.nyd.product.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.product.entity.Product;
import com.nyd.product.entity.ProductZzl;
import com.nyd.product.model.ProductInfoForZzlVO;

/**
 * Created by Dengw on 2017/11/7
 */

@Mapper
public interface ProductMapper {
    void save(Product product);

    void update(Product product);

    Product getProduct(String productId);
    
    List<ProductInfoForZzlVO> getproductForZzl(ProductZzl product);
}
