package com.nyd.product.dao;

import java.util.List;

import com.nyd.product.model.ProductInfoForZzlVO;

/**
 * 
 * @author admin
 *
 */
public interface ProductZzlDao {

	 public List<ProductInfoForZzlVO> getProductInfoForZzl(String productNo) throws Exception;
}
