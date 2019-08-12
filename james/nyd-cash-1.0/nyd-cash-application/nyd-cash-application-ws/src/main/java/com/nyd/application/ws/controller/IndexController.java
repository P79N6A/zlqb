package com.nyd.application.ws.controller;

import com.nyd.application.model.request.IndexRequestModel;
import com.nyd.application.model.request.JiGuangDto;
import com.nyd.application.model.request.MessageModel;
import com.nyd.application.service.AppInfoService;
import com.nyd.application.service.IndexService;
import com.nyd.application.service.VersionService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Dengw on 2017/11/27
 */
@RestController
@RequestMapping("/application")
public class IndexController {

    @Autowired
    VersionService versionService;

    @Autowired
    AppInfoService appInfoService;


    @Autowired
    IndexService indexService;

    @RequestMapping(value = "/version", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getVersion(@RequestBody Map<String, String> map){
        ResponseData responseData = versionService.getVersionInfo(map);
        return responseData;
    }

    @RequestMapping(value = "/checkVersion", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getVersionNew(@RequestBody Map<String, String> map){
        ResponseData responseData = versionService.getVersionInfo(map);
        return responseData;
    }

    @RequestMapping(value = "/appinfo/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getAppInfo(@RequestBody Map<String, String> map){
        ResponseData responseData = appInfoService.getAppInfo(map);
        return responseData;
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getIndexInfo(@RequestBody IndexRequestModel model){
        ResponseData responseData = indexService.getIndexInfo(model);
        return responseData;
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveMessage(@RequestBody MessageModel model){
        ResponseData responseData = indexService.saveMessage(model);
        return responseData;
    }

    @RequestMapping(value = "/common/webKeyList", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getWebInfo(){
        ResponseData responseData = indexService.getWebInfo();
        return responseData;
    }

    @RequestMapping(value = "/push/bindClient", method = RequestMethod.POST, produces = "application/json")
    public ResponseData pushBindClient(@RequestBody JiGuangDto jiGuangDto) {
        return indexService.pushBindClient(jiGuangDto);
    }
}
