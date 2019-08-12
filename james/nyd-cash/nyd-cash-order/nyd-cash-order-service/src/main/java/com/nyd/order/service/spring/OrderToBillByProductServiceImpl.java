package com.nyd.order.service.spring;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.order.api.spring.OrderToBillByProductService;
import com.nyd.order.model.vo.ProductOrderVo;
import com.nyd.product.api.ProductContract;
import com.nyd.product.model.ProductInfoForZzlVO;
import com.nyd.zeus.api.zzl.ZeusForWHServise;
import com.nyd.zeus.model.common.req.BillProductVO;

@Service
public class OrderToBillByProductServiceImpl implements OrderToBillByProductService{
	
	@Autowired
	private ProductContract productContract;
	
	@Autowired
	private ZeusForWHServise zeusForWHServise;

	@Override
	public Boolean getProductInfo(ProductOrderVo product) {
		ProductInfoForZzlVO info = productContract.getProductForZzl("");
		if(null != info){
			BillProductVO billPro = new BillProductVO();
		     BeanUtils.copyProperties(info, billPro);
		     billPro.setOrderNo(product.getOrderNo());
		     billPro.setUserId(product.getUserId());
		   return  zeusForWHServise.saveBillProduct(billPro);
		}
		return false;
	}
	
	

}
