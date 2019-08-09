package com.nyd.application.ws.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.application.api.QiniuContract;
import com.nyd.application.model.mongo.AddressBook;
import com.nyd.application.model.mongo.CallInfo;
import com.nyd.application.model.mongo.SmsInfo;
import com.nyd.application.model.request.AddressBookRequest;
import com.nyd.application.model.request.SmsRequest;
import com.nyd.application.service.MongoService;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 2017/11/16
 */
@RestController
@RequestMapping("/application")
public class MongoController {
    private static Logger LOGGER = LoggerFactory.getLogger(MongoController.class);

    @Autowired
    private MongoService mongoService;

    @Autowired
    private QiniuContract qiniuContract;

    @RequestMapping(value = "/addressBook/infos", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveAddressBook2(@RequestBody AddressBookRequest map){
    	List<AddressBook> list = map.getContent();
    	String appName = map.getAppName();
    	String userId = map.getUserId();
    	if (list.size()==0){
    		return ResponseData.error("参数错误");
    	}
    	for (AddressBook addressBook : list){
    		addressBook.setAppName(appName);
    		addressBook.setUserId(userId);
    	}
    ResponseData responseData = mongoService.saveAddressBook(list);
    return responseData;}

    @RequestMapping(value = "/addressBook/infos/ios", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveAddressBook(@RequestBody AddressBookRequest map){
        List<AddressBook> list = map.getContent();
        String appName = map.getAppName();
        String userId = map.getUserId();
        if (list.size()==0){
            return ResponseData.error("参数错误");
        }
        for (AddressBook addressBook : list){
            addressBook.setAppName(appName);
            addressBook.setUserId(userId);
        }
        ResponseData responseData = mongoService.saveAddressBook(list);
        return responseData;
    }

    @RequestMapping(value = "/call/infos", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveCall(@RequestBody List<CallInfo> list){
        ResponseData responseData = mongoService.saveCallInfo(list);
        return responseData;
    }

    @RequestMapping(value = "/call/upload", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveCallNew(@RequestBody List<CallInfo> list){
        ResponseData responseData = mongoService.saveCallInfo(list);
        return responseData;
    }

    @RequestMapping(value = "/sms/infos", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveSms(@RequestBody SmsRequest map){
    	 List<SmsInfo> list = map.getContent();
         String appName = map.getAppName();
         String userId = map.getUserId();
         if (list.size()==0){
             return ResponseData.error("参数错误");
         }
         for (SmsInfo smsInfo : list){
        	 smsInfo.setAppName(appName);
        	 smsInfo.setUserId(userId);
         }
        ResponseData responseData = mongoService.saveSmsInfo(list);
        return responseData;
    }

    @RequestMapping(value = "/sms/upload", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveSmsNew(@RequestBody List<SmsInfo> list){
        ResponseData responseData = mongoService.saveSmsInfo(list);
        return responseData;
    }

    @RequestMapping(value = "/device/infos", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveDevice(@RequestBody Map<String, Object> map){
        ResponseData responseData = mongoService.saveDeviceInfo(map);
        return responseData;
    }

    @RequestMapping(value = "/device/upload", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveDeviceNew(@RequestBody Map<String, Object> map){
        ResponseData responseData = mongoService.saveDeviceInfo(map);
        return responseData;
    }

    @RequestMapping(value = "/buried/infos", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveBuried(@RequestBody List<Map<String, Object>> list){
        ResponseData responseData = mongoService.saveBuriedInfo(list);
        return responseData;
    }

    @RequestMapping(value = "/buried/upload", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveBuriedNew(@RequestBody List<Map<String, Object>> list){
        ResponseData responseData = mongoService.saveBuriedInfo(list);
        return responseData;
    }

    @RequestMapping(value = "/app/infos", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveApp(@RequestBody Map<String, Object> map){
        ResponseData responseData = mongoService.saveAppInfo(map);
        return responseData;
    }

    @RequestMapping(value = "/app/upload", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveAppNew(@RequestBody Map<String, Object> map){
        ResponseData responseData = mongoService.saveAppInfo(map);
        return responseData;
    }

}
