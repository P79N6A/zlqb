package com.nyd.admin.service;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.ProductInfoVo;
import com.nyd.admin.model.ProductQueryVo;
import com.nyd.admin.model.ProductVo;

/**
 * Created by Dengw on 2017/12/14
 */
public interface ProductService {
    boolean saveProduct(ProductInfoVo vo);

    boolean updateProduct(ProductInfoVo vo);

    ProductInfoVo getProductDetail(ProductQueryVo vo);

    PageInfo<ProductVo> findPage(ProductQueryVo vo) throws Exception;
}
