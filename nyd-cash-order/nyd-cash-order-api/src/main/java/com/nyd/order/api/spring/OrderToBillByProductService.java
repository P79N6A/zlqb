package com.nyd.order.api.spring;

import com.nyd.order.model.vo.ProductOrderVo;


public interface OrderToBillByProductService {
	
	public Boolean getProductInfo(ProductOrderVo product);

}
