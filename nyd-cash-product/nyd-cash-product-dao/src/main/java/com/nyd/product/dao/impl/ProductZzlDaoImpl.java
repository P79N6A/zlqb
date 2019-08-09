package com.nyd.product.dao.impl;

import com.nyd.product.dao.FundDao;
import com.nyd.product.dao.ProductZzlDao;
import com.nyd.product.dao.mapper.ProductMapper;
import com.nyd.product.entity.Fund;
import com.nyd.product.entity.ProductZzl;
import com.nyd.product.model.FundInfo;
import com.nyd.product.model.ProductInfoForZzlVO;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.util.List;

/**
 * Created by zhujx on 2017/11/22.
 */
@Repository
public class ProductZzlDaoImpl implements ProductZzlDao {

    @Autowired
    private ProductMapper productMapper;

   
    @Override
    public List<ProductInfoForZzlVO> getProductInfoForZzl(String productNo) throws Exception {
    	ProductZzl pro = new ProductZzl();
    	pro.setProductCode(productNo);
    	List<ProductInfoForZzlVO> list =productMapper.getproductForZzl(pro);
    	return list;
    }
}
