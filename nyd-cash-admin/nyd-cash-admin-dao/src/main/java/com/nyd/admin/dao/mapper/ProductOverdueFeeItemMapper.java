package com.nyd.admin.dao.mapper;

import com.nyd.admin.dao.CrudDao;
import com.nyd.admin.entity.ProductOverdueFeeItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Dengw on 2017/12/15
 */
@Mapper
public interface ProductOverdueFeeItemMapper extends CrudDao<ProductOverdueFeeItem> {
    ProductOverdueFeeItem select(String productCode);
}
