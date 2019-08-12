package com.nyd.admin.ws.controller;

import com.nyd.admin.model.dto.CollectionGroupInfoDto;
import com.nyd.admin.service.CollectionGroupInfoService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiaxy
 * @date 20180612
 */
@RestController
@RequestMapping("/admin/group")
public class CollectionGroupInfoController {
    private static Logger LOGGER = LoggerFactory.getLogger(CollectionGroupInfoController.class);

    @Autowired
    CollectionGroupInfoService collectionGroupInfoService;

    @RequestMapping("/saveGroupInfo")
    public ResponseData saveGroupInfo(@RequestBody CollectionGroupInfoDto collectionGroupInfoDto){
        try{
            return collectionGroupInfoService.saveGroupInfo(collectionGroupInfoDto);
        }catch (Exception e){
            LOGGER.error("CollectionGroupInfoController saveGroupInfo error!",e);
            return ResponseData.error("服务器开小差了!");
        }
    }

    @RequestMapping("/queryGroupInfoList")
    public ResponseData queryGroupInfoList(@RequestBody CollectionGroupInfoDto collectionGroupInfoDto){
        try{
            return collectionGroupInfoService.queryGroupInfoList(collectionGroupInfoDto);
        }catch (Exception e){
            LOGGER.error("CollectionGroupInfoController queryGroupInfoList error!",e);
            return ResponseData.error("服务器开小差了!");
        }
    }

    @RequestMapping("/queryGroupInfoListByCompanyId")
    public ResponseData queryGroupInfoListByCompanyId(@RequestBody CollectionGroupInfoDto collectionGroupInfoDto){
        try{
            return collectionGroupInfoService.queryGroupInfoListByCompanyId(collectionGroupInfoDto);
        }catch (Exception e){
            LOGGER.error("CollectionGroupInfoController queryGroupInfoListByCompanyId error!",e);
            return ResponseData.error("服务器开小差了!");
        }
    }

    @RequestMapping("/updateGroupInfo")
    public ResponseData updateGroupInfo(@RequestBody CollectionGroupInfoDto collectionGroupInfoDto){
        try{
            return collectionGroupInfoService.updateGroupInfo(collectionGroupInfoDto);
        }catch (Exception e){
            LOGGER.error("CollectionGroupInfoController updateGroupInfo error!",e);
            return ResponseData.error("服务器开小差了!");
        }
    }

    @RequestMapping("/deleteGroupInfo")
    public ResponseData deleteGroupInfo(@RequestBody CollectionGroupInfoDto collectionGroupInfoDto){
        try {
            return collectionGroupInfoService.deleteGroupInfo(collectionGroupInfoDto);
        }catch (Exception e){
            LOGGER.error("CollectionGroupInfoController deleteGroupInfo error!",e);
            return ResponseData.error("服务器开小差了!");
        }
    }

}
