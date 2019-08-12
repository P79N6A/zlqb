package com.nyd.user.ws.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.nyd.user.model.UserBindCardConfirm;
import com.nyd.user.model.UserBindCardReq;
import com.nyd.user.service.BindCardService;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@RestController
@RequestMapping("/user")
public class BindCardController {
    private static Logger LOGGER = LoggerFactory.getLogger(BindCardController.class);

    @Autowired
    private BindCardService bindCardService;

    @RequestMapping(value = "/bindCard", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveBank(@RequestBody UserBindCardReq req) throws Throwable{
        return bindCardService.bindCard(req);
    }

    @RequestMapping(value = "/bindBankCard", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveBankNew(@RequestBody UserBindCardReq req) throws Throwable{
        return bindCardService.bindCard(req);
    }


    @RequestMapping(value = "/bindCardConfirm", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchBankList(@RequestBody UserBindCardConfirm req) throws Throwable{
    		LOGGER.info("确认绑卡请求参数：" + JSON.toJSONString(req));
            return bindCardService.bindCardConfirm(req);
    }

    @RequestMapping(value = "/bindBankCardConfirm", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchBankListNew(@RequestBody UserBindCardConfirm req) throws Throwable{
        LOGGER.info("确认绑卡请求参数：" + JSON.toJSONString(req));
        return bindCardService.bindCardConfirm(req);
    }

    @RequestMapping(value = "/queryCardChannelCode", method = RequestMethod.POST)
    public JSONObject queryCardChannelCode() throws Throwable{
        return bindCardService.queryBindCardChannelCode();
    }

    /**
     * 绑卡记录手动录入
     * @param req
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/bindCardReset", method = RequestMethod.POST, produces = "application/json")
    public ResponseData bankCardReset(@RequestBody List<UserBindCardConfirm> req) throws Throwable{
    	LOGGER.info("绑卡记录手动录入请求参数：" + JSON.toJSONString(req));
    	return bindCardService.bindCardReset(req);
    }
}
