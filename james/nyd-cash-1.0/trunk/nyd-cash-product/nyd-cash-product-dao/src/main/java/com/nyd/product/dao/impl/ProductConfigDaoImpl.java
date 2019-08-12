package com.nyd.product.dao.impl;

import com.nyd.product.dao.ProductConfigDao;
import com.nyd.product.entity.ProductConfig;
import com.nyd.product.model.ProductConfigInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 17/11/6.
 */
@Repository
public class ProductConfigDaoImpl implements ProductConfigDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(ProductConfigInfo productConfigInfo) throws Exception {
        ProductConfig productConfig = new ProductConfig();
        BeanUtils.copyProperties(productConfigInfo,productConfig);
        crudTemplate.save(productConfig);
    }

    @Override
    public void update(ProductConfigInfo productConfigInfo) throws Exception {
        Criteria criteria = Criteria.from(ProductConfig.class)
                .whioutId()
                .where().and("product_code", Operator.EQ,productConfigInfo.getProductCode())
                .endWhere();
        productConfigInfo.setProductCode(null);
        crudTemplate.update(productConfigInfo,criteria);
    }

    @Override
    public List<ProductConfigInfo> getObjectsByProductCode(String productCode) throws Exception {
        ProductConfigInfo productConfigInfo = new ProductConfigInfo();
        Criteria criteria = Criteria.from(ProductConfig.class)
                .where().and("product_code", Operator.EQ,productCode)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(productConfigInfo,criteria);
    }
}
