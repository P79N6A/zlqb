package com.nyd.admin.dao.mapper;

import com.nyd.admin.dao.CrudDao;
import com.nyd.admin.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Dengw on 2017/12/15
 */
@Mapper
public interface ProductMapper extends CrudDao<Product> {
    Product select(String productCode);
}
