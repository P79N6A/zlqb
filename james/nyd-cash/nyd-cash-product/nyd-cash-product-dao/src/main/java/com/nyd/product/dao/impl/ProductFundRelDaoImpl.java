package com.nyd.product.dao.impl;

import com.nyd.product.dao.ProductFundRelDao;
import com.nyd.product.entity.Fund;
import com.nyd.product.entity.ProductFundRel;
import com.nyd.product.model.ProductFundRelInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhujx on 2017/11/23.
 */
@Repository
public class ProductFundRelDaoImpl  implements ProductFundRelDao {

    @Resource(name="mysql")
    CrudTemplate crudTemplate;

    @Override
    public void save(ProductFundRelInfo productFundRelInfo) throws Exception {
        ProductFundRel productFundRel  = new ProductFundRel();
        BeanUtils.copyProperties(productFundRelInfo,productFundRel);
        crudTemplate.save(productFundRel);
    }

    @Override
    public void updateByProductCode(ProductFundRelInfo productFundRelInfo) throws Exception {
        Criteria criteria = Criteria.from(Fund.class)
                .whioutId()
                .where().and("product_code", Operator.EQ,productFundRelInfo.getProductCode())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        productFundRelInfo.setProductCode(null);
        crudTemplate.update(productFundRelInfo,criteria);
    }

    @Override
    public List<ProductFundRelInfo> getProductFundRelInfo(String productCode) throws Exception {
        Criteria criteria = Criteria.from(Fund.class)
                .whioutId()
                .where().and("product_code", Operator.EQ,productCode)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        ProductFundRelInfo productFundRelInfo = new ProductFundRelInfo();
        return crudTemplate.find(productFundRelInfo,criteria);
    }
}
