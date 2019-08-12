package com.nyd.admin.ws.controller;

import com.nyd.admin.service.ResourceRatioService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Peng
 * @create 2017-12-27 14:21
 **/

@RestController
@RequestMapping("/admin")
public class ResourceRatioController {
    @Autowired
    ResourceRatioService resourceRatioService;

    @RequestMapping("/queryResourceRatio")
    public ResponseData queryResourceRatio(){
        return resourceRatioService.getResourceRatio();
    }
}
