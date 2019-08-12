package com.nyd.user.service.impl;

import com.nyd.user.api.UserContactContract;
import com.nyd.user.dao.ContactDao;
import com.nyd.user.dao.StepDao;
import com.nyd.user.entity.Contact;
import com.nyd.user.entity.Step;
import com.nyd.user.model.ContactInfo;
import com.nyd.user.model.ContactInfos;
import com.nyd.user.model.enums.StepScore;
import com.nyd.user.service.ContactInfoService;
import com.nyd.user.service.consts.UserConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dengw on 17/11/3.
 */
@Service(value = "userContactContract")
public class ContactInfoServiceImpl implements ContactInfoService,UserContactContract {
    private static Logger LOGGER = LoggerFactory.getLogger(ContactInfoServiceImpl.class);
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private StepDao stepDao;

    /**
     * 保存联系人信息
     * @param contactInfo
     * @return
     */
    @Override
    public ResponseData saveContactInfo(ContactInfos contactInfo){
        LOGGER.info("begin to save contactInfo, userId is " + contactInfo.getUserId());
        ResponseData responseData = ResponseData.success();
        List<Contact> contactList = getContact(contactInfo);
        try {
            contactDao.saveList(contactList);
            LOGGER.info("save contactInfo success");
        } catch (Exception e) {
            LOGGER.error("save contactInfo error! userId = "+contactInfo.getUserId(),e);
            return ResponseData.error(UserConsts.CONTACT_FORMAT_ERROR);
        }
        try {
            //更新信息完整度
            LOGGER.info("begin to save stepInfo");
            Step step = new Step();
            step.setUserId(contactInfo.getUserId());
            step.setContactFlag(UserConsts.FILL_FLAG);
            stepDao.updateStep(step);
            LOGGER.info("update stepInfo success");
        } catch (Exception e) {
            LOGGER.error("save contactInfo success，but update stepInfo failed ! userId= "+contactInfo.getUserId(), e);
        }
        return responseData;
    }

    /**
     * 查询联系人信息
     * @param userId
     * @return
     */
    @Override
    public ResponseData<ContactInfos> getContactInfo(String userId){
        LOGGER.info("begin to get contactInfo, userId is " + userId);
        ResponseData responseData = ResponseData.success();
        ContactInfos contactInfo = new ContactInfos();
        List<Contact> contactList = null;
        try {
            contactList = contactDao.getContacts(userId);
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("get contactInfo error! userId = "+userId,e);
            return responseData;
        }
        if (contactList != null && contactList.size()>0) {
            for(Contact contact : contactList){
                if("direct".equals(contact.getType())){
                    contactInfo.setDirectContactName(contact.getName());
                    contactInfo.setDirectContactMobile(contact.getMobile());
                    contactInfo.setDirectContactRelation(contact.getRelationship());
                }
                if("major".equals(contact.getType())){
                    contactInfo.setMajorContactName(contact.getName());
                    contactInfo.setMajorContactMobile(contact.getMobile());
                    contactInfo.setMajorContactRelation(contact.getRelationship());
                }
            }
            responseData.setData(contactInfo);
        }
        return responseData;
    }



    /**
     * 查询联系人信息
     * @param name,mobile
     * @return
     */
    @Override
    public ResponseData<List<ContactInfo>> getContactInfo(String name, String mobile) {
        ResponseData responseData = ResponseData.success();
        if(StringUtils.isBlank(mobile) && StringUtils.isBlank(name)){
            responseData = ResponseData.error(UserConsts.PARAM_ERROR);
            return responseData;
        }
        ContactInfo contactInfo= new ContactInfo();
        contactInfo.setName(name);
        contactInfo.setMobile(mobile);
        try {
            List<ContactInfo> list = contactDao.getContacts(contactInfo);
            if(list != null && list.size()>0){
                responseData.setData(list);
            }
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR);
            LOGGER.error("get contactInfo error! mobile = "+mobile,e);
            return responseData;
        }
        return responseData;
    }

    /**
     * 组装联系人信息
     * @param contactInfo
     * @return
     */
    private List<Contact> getContact(ContactInfos contactInfo){
        List<Contact> contactList = new ArrayList<Contact>();
        Contact directContact = new Contact();
        directContact.setUserId(contactInfo.getUserId());
        directContact.setType("direct");
        directContact.setName(contactInfo.getDirectContactName());
        directContact.setMobile(contactInfo.getDirectContactMobile());
        directContact.setRelationship(contactInfo.getDirectContactRelation());
        Contact majorContact = new Contact();
        majorContact.setUserId(contactInfo.getUserId());
        majorContact.setType("major");
        majorContact.setName(contactInfo.getMajorContactName());
        majorContact.setMobile(contactInfo.getMajorContactMobile());
        majorContact.setRelationship(contactInfo.getMajorContactRelation());
        contactList.add(directContact);
        contactList.add(majorContact);
        return contactList;
    }

    /**
     * 根据用户userID找到他相关联系人信息
     * @param userId
     * @return
     */
    @Override
    public List<Contact> findContactByUserId(String userId) throws Exception {
        return contactDao.findContactByUserId(userId);
    }

    /**
     * 更新联系人信息
     * @param contactInfo
     */
    @Override
    public void updateYmtUesrContact(ContactInfo contactInfo) throws Exception {
        contactDao.updateYmtUesrContact(contactInfo);
    }

    /**
     * 保存联系人信息
     * @param contact
     */
    @Override
    public void saveYmtUesrContact(Contact contact) throws Exception {
        contactDao.save(contact);
    }
}
