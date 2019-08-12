package com.nyd.product.dao.impl;

import com.nyd.product.dao.ProductOverdueFeeItemDao;
import com.nyd.product.entity.ProductOverdueFeeItem;
import com.nyd.product.model.ProductOverdueFeeItemInfo;
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
public class ProductOverdueFeeItemDaoImpl implements ProductOverdueFeeItemDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(ProductOverdueFeeItemInfo productOverdueFeeItemInfo) throws Exception {
        ProductOverdueFeeItem productOverdueFeeItem = new ProductOverdueFeeItem();
        BeanUtils.copyProperties(productOverdueFeeItemInfo,productOverdueFeeItem);
        crudTemplate.save(productOverdueFeeItem);
    }

    @Override
    public void update(ProductOverdueFeeItemInfo productOverdueFeeItemInfo) throws Exception {
        Criteria criteria = Criteria.from(ProductOverdueFeeItem.class)
                .whioutId()
                .where().and("product_code", Operator.EQ,productOverdueFeeItemInfo.getProductCode())
                .endWhere();
        productOverdueFeeItemInfo.setProductCode(null);
        crudTemplate.update(productOverdueFeeItemInfo,criteria);
    }

    @Override
    public List<ProductOverdueFeeItemInfo> getObjectsByProductCode(String productCode) throws Exception {
        ProductOverdueFeeItemInfo product = new ProductOverdueFeeItemInfo();
        Criteria criteria = Criteria.from(ProductOverdueFeeItem.class)
                .where().and("product_code", Operator.EQ,productCode)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(product,criteria);
    }
}
