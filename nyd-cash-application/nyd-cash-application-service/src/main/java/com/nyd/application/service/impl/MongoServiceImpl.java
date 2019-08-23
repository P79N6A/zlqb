package com.nyd.application.service.impl;

import java.util.List;
import java.util.Map;

import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.XunlianBankListInfo;
import com.nyd.user.model.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.application.dao.AddressBookDao;
import com.nyd.application.dao.CallInfoDao;
import com.nyd.application.dao.SmsInfoDao;
import com.nyd.application.model.mongo.AddressBook;
import com.nyd.application.model.mongo.CallInfo;
import com.nyd.application.model.mongo.SmsInfo;
import com.nyd.application.service.MongoService;
import com.nyd.application.service.commonEnum.MongoCollection;
import com.nyd.application.service.util.MongoApi;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 2017/11/23
 */
@Service(value="mongoService")
public class MongoServiceImpl implements MongoService {
    private static Logger LOGGER = LoggerFactory.getLogger(MongoServiceImpl.class);

    @Autowired
    private MongoApi mongoApi;
    
    @Autowired
    private SmsInfoDao smsInfoDao;
    
    @Autowired
    private CallInfoDao callInfoDao;
    
    @Autowired
    private AddressBookDao addressBookDao;

    @Autowired
    private UserIdentityContract userIdentityContract;

    @Override
    public ResponseData saveAddressBook(List<AddressBook> list,String userId) {
        LOGGER.info("begin to save addressBook success !");
        ResponseData responseData = ResponseData.success();

        if(list != null && list.size()>0){
            String phone = list.get(0).getPhoneNo();
            LOGGER.info("用户手机号为saveAddressBook param phone is " + phone);
            LOGGER.info("userid....." + userId);
            //如果userid位空去查询
            if (!StringUtils.isNotBlank(userId)){
                UserDto userDto = new UserDto();
                userDto.setAccountNumber(phone);
                List<UserInfo> userInfoList = userIdentityContract.getUserInfos(userDto).getData();
                if (userInfoList != null && userInfoList.size() > 0){
                    UserInfo userInfo = userInfoList.get(0);
                    LOGGER.info("用户手机号为:{},查询出来的userid为{}",phone,userInfo.getUserId());
                    userId = userInfo.getUserId();
                    for (AddressBook addressBook : list){
                        addressBook.setUserId(userId);
                    }
                }else{
                    return responseData;
                }
            }

            try {
                List<AddressBook> listFromDB = mongoApi.getAddressBooks(list.get(0).getPhoneNo(), list.get(0).getDeviceId());
                if (listFromDB != null && listFromDB.size() > 0) {
                    for (AddressBook y : listFromDB) {
                        for (int i = list.size() - 1; i >= 0; i--) {
                            AddressBook x = list.get(i);
                            if(x.getName() != null && x.getTel() != null) {
                                if (x.getName().equals(y.getName()) && x.getTel().equals(y.getTel())) {
                                    list.remove(i);
                                }
                            }
                        }
                    }
                }
                //入mongo库的同时入mysql
                try {
        			//TODO: hwt
                	LOGGER.info(" 通讯录：" + list);
                	for(AddressBook info : list){
                		addressBookDao.save(info);
                	} 	
        		} catch (Exception e) {
        			LOGGER.error(" 获取通讯录记录异常（mysql）"+ e);
        		}
                mongoApi.saveAddressBook(list);
                LOGGER.info("save addressBook success !");
            } catch (Exception e) {
                LOGGER.error("save addressBook error !",e);
            }
        }
        return responseData;
    }

    @Override
    public ResponseData saveCallInfo(List<CallInfo> list) {
        LOGGER.info("begin to save callInfo success !");
        ResponseData responseData = ResponseData.success();

        if(list != null && list.size()>0){
            try {
                List<CallInfo> listFromDB = mongoApi.getCallInfos(list.get(0).getPhoneNo(),list.get(0).getDeviceId());
                if (listFromDB != null && listFromDB.size() > 0) {
                    for (CallInfo y : listFromDB) {
                        for (int i = list.size() - 1; i >= 0; i--) {
                            CallInfo x = list.get(i);
                            if(x.getCallNo() != null && x.getCalltime() != null) {
                                if (x.getCallNo().equals(y.getCallNo()) && x.getCalltime().equals(y.getCalltime())) {
                                    list.remove(i);
                                }
                            }
                        }
                    }
                }
                //入mongo库的同时入mysql
                try {
        			//TODO: hwt
                	LOGGER.info(" 通话记录：" + list);
                	for(CallInfo info : list){
                		callInfoDao.save(info);
                	} 	
        		} catch (Exception e) {
        			LOGGER.error(" 获取短信记录异常（mysql）"+ e);
        		}
                mongoApi.saveCallInfo(list);
                LOGGER.info("save callInfo success !");
            } catch (Exception e) {
                LOGGER.error("save callInfo error !",e);
            }
        }
        return responseData;
    }

    @Override
    public ResponseData saveSmsInfo(List<SmsInfo> list) {
        LOGGER.info("begin to save smsInfo");
        ResponseData responseData = ResponseData.success();
        if(list != null && list.size()>0){
            try {
                List<SmsInfo> listFromDB = mongoApi.getSmsInfo(list.get(0).getPhoneNo(),list.get(0).getDeviceId());
                if (listFromDB != null && listFromDB.size() > 0) {
                    for (SmsInfo y : listFromDB) {
                        for (int i = list.size() - 1; i >= 0; i--) {
                            SmsInfo x = list.get(i);
                            if(x.getCallNo() != null && x.getTime() != null){
                                if (x.getCallNo().equals(y.getCallNo()) && x.getTime().equals(y.getTime())) {
                                    list.remove(i);
                                }
                            }
                        }
                    }
                }
                //入mongo库的同时入mysql
                try {
        			//TODO: hwt
                	LOGGER.info(" 短信记录：" + list);
                	for(SmsInfo info : list){
                		smsInfoDao.save(info);
                	} 	
        		} catch (Exception e) {
        			LOGGER.error(" 获取短信记录异常（mysql）"+ e);
        		}
                mongoApi.saveSmsInfo(list);
                LOGGER.info("save smsInfo success !");
            } catch (Exception e) {
                LOGGER.error("save smsInfo error !",e);
            }
        }
        return responseData;
    }

    @Override
    public ResponseData saveDeviceInfo(Map<String, Object> map) {
        LOGGER.info("begin to save deviceInfo");
        ResponseData responseData = ResponseData.success();
        try {
            mongoApi.save(map, MongoCollection.DEVICEINFO.getCode());
            LOGGER.info("save deviceInfo success !");
        } catch (Exception e) {
            LOGGER.error("save deviceInfo error !",e);
        }
        return responseData;
    }

    @Override
    public ResponseData saveBuriedInfo(List<Map<String, Object>> list) {
        LOGGER.info("begin to save buriedInfo");
        ResponseData responseData = ResponseData.success();
        try {
            mongoApi.saveBuriedInfo(list);
            LOGGER.info("save buriedInfo success !");
        } catch (Exception e) {
            LOGGER.error("save buriedInfo error !",e);
        }
        return responseData;
    }

    @Override
    public ResponseData saveAppInfo(Map<String, Object> map) {
        LOGGER.info("begin to save appInfo");
        ResponseData responseData = ResponseData.success();
        try {
            mongoApi.save(map, MongoCollection.APPINFO.getCode());
            LOGGER.info("save appInfo success !");
        } catch (Exception e) {
            LOGGER.error("save appInfo error !",e);
        }
        return responseData;
    }
}
