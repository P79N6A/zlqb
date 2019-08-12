package com.nyd.user.ws.controller;

import com.alibaba.fastjson.JSON;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.BaseInfo;
import com.nyd.user.model.IdentityInfo;
import com.nyd.user.service.IdentityInfoService;
import com.nyd.user.service.consts.UserConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Dengw on 2017/11/1.
 */
@RestController
@RequestMapping("/user")
public class IdentityInfoController {
    private static Logger LOGGER = LoggerFactory.getLogger(IdentityInfoController.class);

    @Autowired
    IdentityInfoService identityInfoService;
    @Autowired
    private UserIdentityContract userIdentityContract;

    @RequestMapping(value = "/identity/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveIdentity(@RequestBody IdentityInfo identityInfo) throws Throwable{
        ResponseData responseData = null;
        try {
            responseData = identityInfoService.saveUserInfo(identityInfo);
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
        }
        return responseData;
    }
    @RequestMapping(value = "/identity/fetch/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchIdentity (@RequestBody BaseInfo baseInfo) throws Throwable{
        if(baseInfo.getUserId() == null){
            return ResponseData.success();
        }else{
            return identityInfoService.getIdentityInfo(baseInfo.getUserId());
        }
    }

    @RequestMapping(value = "/identity/helibao", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryHlb(@RequestBody BaseInfo baseInfo){
        ResponseData responseData = userIdentityContract.getUserInfo(baseInfo.getUserId());
        LOGGER.info(baseInfo.getUserId()+"调用的结果为"+ JSON.toJSONString(responseData));
        return responseData;
    }

}
