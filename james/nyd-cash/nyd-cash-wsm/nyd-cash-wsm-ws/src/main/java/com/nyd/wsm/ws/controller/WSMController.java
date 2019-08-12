package com.nyd.wsm.ws.controller;

import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by zhujx on 2017/12/8.
 */
@RestController
@RequestMapping("/api/nyd")
public class WSMController {

    private static Logger LOGGER = LoggerFactory.getLogger(WSMController.class);

    /**
     * 用户信息
     * @param reqMap
     * @return ResponseData
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = "application/json")
    public ResponseData user(@RequestBody Map reqMap){

        return null;
    }


    /**
     * 订单信息
     * @param reqMap
     * @return ResponseData
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST, produces = "application/json")
    public ResponseData order(@RequestBody Map reqMap){

        return null;
    }


    /**
     * 账单信息
     * @param reqMap
     * @return ResponseData
     */
    @RequestMapping(value = "/bill", method = RequestMethod.POST, produces = "application/json")
    public ResponseData bill(@RequestBody Map reqMap){

        return null;
    }


}
