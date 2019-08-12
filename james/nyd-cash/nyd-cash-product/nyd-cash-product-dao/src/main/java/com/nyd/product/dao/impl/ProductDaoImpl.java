package com.nyd.product.dao.impl;

import com.nyd.product.dao.ProductDao;
import com.nyd.product.entity.Product;
import com.nyd.product.model.ProductInfo;
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
public class ProductDaoImpl implements ProductDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(ProductInfo productInfo) throws Exception {
        Product product = new Product();
        BeanUtils.copyProperties(productInfo,product);
        crudTemplate.save(product);
    }

    @Override
    public void update(ProductInfo productInfo) throws Exception {
        Criteria criteria = Criteria.from(Product.class)
                .whioutId()
                .where().and("product_code", Operator.EQ,productInfo.getProductCode())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        productInfo.setProductCode(null);
        crudTemplate.update(productInfo,criteria);
    }

    @Override
    public List<ProductInfo> getObjectsByProductCode(String productCode) throws Exception {
        Criteria criteria = Criteria.from(Product.class)
                .where().and("product_code", Operator.EQ,productCode)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        ProductInfo productInfo = new ProductInfo();
        return crudTemplate.find(productInfo,criteria);
    }

    @Override
    public List<ProductInfo> getProducts() throws Exception {
        Criteria criteria = Criteria.from(Product.class)
                .where()
                .and("business",Operator.EQ,"nyd")
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        ProductInfo productInfo = new ProductInfo();
        return crudTemplate.find(productInfo,criteria);
    }

    @Override
    public List<ProductInfo> getObjectsByBusiness(String businessCode) throws Exception {
        Criteria criteria = Criteria.from(Product.class)
                .where()
                .and("business",Operator.EQ,businessCode)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time asc");
        ProductInfo productInfo = new ProductInfo();
        return crudTemplate.find(productInfo,criteria);
    }

}
