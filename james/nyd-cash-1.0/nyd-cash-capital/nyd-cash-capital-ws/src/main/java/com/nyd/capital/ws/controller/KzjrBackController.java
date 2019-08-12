package com.nyd.capital.ws.controller;

import com.nyd.capital.service.impl.KzjrFundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Cong Yuxiang
 * 2018/5/12
 **/
@RestController
@RequestMapping("/nyd/capital/kzjr")
public class KzjrBackController {
    Logger logger = LoggerFactory.getLogger(KzjrBackController.class);

    @Autowired
    private KzjrFundService kzjrFundService;

    @RequestMapping(value = "/callback" ,produces="text/plain;charset=UTF-8")
    public String callback(@RequestBody String callback){
        logger.info("回调信息"+callback);
        try {
            kzjrFundService.saveLoanResult(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
    @RequestMapping(value = "/index",produces="text/plain;charset=UTF-8")
    public String index(){
        return "欢迎index";
    }
}
