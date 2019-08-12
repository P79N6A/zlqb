package com.nyd.product.ws.controller;

import com.nyd.product.dao.mapper.ProductFundRelMapper;
import com.nyd.product.entity.Product;
import com.nyd.product.model.FundInfo;
import com.nyd.product.service.consts.FundConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dengw on 2017/11/7
 */
@RestController
@RequestMapping("/api")
public class ProductInfoController {
    private static Logger LOGGER = LoggerFactory.getLogger(ProductInfoController.class);

    @Autowired
    private ProductFundRelMapper productFundRelMapper;

    /**
     * test
     * @param product
     * @return
     */
    @RequestMapping(value = "/fund", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getFundInfo(@RequestBody Product product){
        ResponseData response = ResponseData.success();
        String fundCode = FundConsts.WSM;
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("productCode",product.getProductCode());
            //设置当前时间格式为HH:mm:ss的字符串
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String curTime =  sdf.format(date).toString();
            map.put("curTime",curTime);
            FundInfo fund = productFundRelMapper.getFundInfo(map);
            fundCode = fund.getFundCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setData(fundCode);
        return response;
    }





}
