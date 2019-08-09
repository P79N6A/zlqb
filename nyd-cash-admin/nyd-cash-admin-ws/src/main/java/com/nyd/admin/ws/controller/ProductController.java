package com.nyd.admin.ws.controller;

import com.nyd.admin.model.ProductInfoVo;
import com.nyd.admin.model.ProductQueryVo;
import com.nyd.admin.service.ProductService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Dengw on 2017/12/14
 **/
@RestController
@RequestMapping("/admin/product")
public class ProductController {
    @Autowired
    ProductService productService;

    /**
     * 金融产品保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveProduct(@RequestBody ProductInfoVo vo){
        boolean flag = productService.saveProduct(vo);
        if(flag) {
            return ResponseData.success();
        }else {
            return ResponseData.error();
        }
    }

    /**
     * 金融产品更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
    public ResponseData updateProduct(@RequestBody ProductInfoVo vo){
        boolean flag = productService.updateProduct(vo);
        if(flag) {
            return ResponseData.success();
        }else {
            return ResponseData.error();
        }
    }

    /**
     * 金融产品查询
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryProduct(@RequestBody ProductQueryVo vo){
        try {
            return ResponseData.success(productService.findPage(vo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.error();
    }

    /**
     * 金融产品查询
     */
    @RequestMapping(value = "/query/detail", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryProductDetail(@RequestBody ProductQueryVo vo){
        return ResponseData.success(productService.getProductDetail(vo));
    }
}
