package com.nyd.user.ws.controller;

import com.nyd.user.model.BaseInfo;
import com.nyd.user.model.ContactInfos;
import com.nyd.user.service.ContactInfoService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Dengw on 17/11/3.
 */
@RestController
@RequestMapping("/user")
public class ContactInfoController {
    private static Logger LOGGER = LoggerFactory.getLogger(BankInfoController.class);

    @Autowired
    ContactInfoService contactInfoService;

    @RequestMapping(value = "/contact/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveContact(@RequestBody ContactInfos contactInfo) throws Throwable{
        ResponseData responseData = contactInfoService.saveContactInfo(contactInfo);
        return responseData;
    }

    @RequestMapping(value = "/contact/fetch/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchContact(@RequestBody BaseInfo baseInfo) throws Throwable{
        if(baseInfo.getUserId() == null){
            return ResponseData.success();
        }else{
            return contactInfoService.getContactInfo(baseInfo.getUserId());
        }
    }

}
