package com.nyd.admin.service.utils;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.entity.Product;
import com.nyd.admin.entity.ProductOverdueFeeItem;
import com.nyd.admin.model.ProductInfoVo;
import com.nyd.admin.model.ProductVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Created by Dengw on 2017/12/14
 */
@Mapper
public interface ProductMapStruct {
    ProductMapStruct INSTANCE = Mappers.getMapper(ProductMapStruct.class);

    @Mappings(value = {
            @Mapping(target = "productType",expression = "java(Integer.parseInt(vo.getProductType()))"),
            @Mapping(target = "isInUse",expression = "java(Integer.parseInt(vo.getIsInUse()))")
    })
    Product ProductVo2Po(ProductInfoVo vo);

    @Mappings(value = {
            @Mapping(target = "productType",expression = "java(String.valueOf(po.getProductType()))"),
            @Mapping(target = "isInUse",expression = "java(String.valueOf(po.getIsInUse()))")
    })
    ProductInfoVo Po2ProductInfoVo(Product po);

    @Mappings(value = {
            @Mapping(target = "productType",expression = "java(String.valueOf(po.getProductType()))")
    })
    ProductVo Po2ProductVo(Product po);

    ProductOverdueFeeItem overdueVo2Po(ProductInfoVo vo);

    PageInfo<ProductVo> poPage2VoPage(PageInfo<Product> page);
}
