package com.nyd.admin.ws.controller;

import com.nyd.admin.model.dto.CollectionCompanyInfoDto;
import com.nyd.admin.service.CollectionCompanyInfoService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiaxy
 * @date 20180611
 */
@RestController
@RequestMapping("/admin/company")
public class CollectionCompanyInfoController {
    private static Logger LOGGER = LoggerFactory.getLogger(CollectionCompanyInfoController.class);

    @Autowired
    CollectionCompanyInfoService collectionCompanyInfoService;

    @RequestMapping("/saveCompanyInfo")
    public ResponseData saveCompanyInfo(@RequestBody CollectionCompanyInfoDto collectionCompanyInfoDto){
        try {
            return collectionCompanyInfoService.saveCompanyInfo(collectionCompanyInfoDto);
        } catch (Exception e) {
            LOGGER.error("CollectionCompanyInfoController saveCompanyInfo error!",e);
            return ResponseData.error("服务器开小差了!");
        }
    }

    @RequestMapping("/queryCompanyInfoList")
    public ResponseData queryCompanyInfoList(@RequestBody CollectionCompanyInfoDto collectionCompanyInfoDto){
        try{
            return collectionCompanyInfoService.queryCompanyInfoList(collectionCompanyInfoDto);
        }catch (Exception e){
            LOGGER.error("CollectionCompanyInfoController queryCompanyInfoList error!",e);
            return ResponseData.error("服务器开小差了!");
        }
    }

    @RequestMapping("/updateCompanyInfo")
    public ResponseData updateCompanyInfo(@RequestBody CollectionCompanyInfoDto collectionCompanyInfoDto){
        try{
            return collectionCompanyInfoService.updateCompanyInfo(collectionCompanyInfoDto);
        }catch (Exception e){
            LOGGER.error("CollectionCompanyInfoController updateCompanyInfo error!",e);
            return ResponseData.error("服务器开小差了!");
        }
    }

    @RequestMapping("/deleteCompanyInfo")
    public ResponseData deleteCompanyInfo(@RequestBody CollectionCompanyInfoDto collectionCompanyInfoDto){
        try{
            return collectionCompanyInfoService.deleteCompanyInfo(collectionCompanyInfoDto);
        }catch (Exception e){
            LOGGER.error("CollectionCompanyInfoController deleteCompanyInfo error!",e);
            return ResponseData.error("服务器开小差了");
        }
    }

}
