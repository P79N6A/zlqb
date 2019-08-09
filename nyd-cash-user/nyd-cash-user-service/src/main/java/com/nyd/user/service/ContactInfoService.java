package com.nyd.user.service;


import com.nyd.user.entity.Contact;

import com.nyd.user.model.ContactInfo;
import com.nyd.user.model.ContactInfos;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by Dengw on 17/11/3.
 */
public interface ContactInfoService {

    ResponseData saveContactInfo(ContactInfos contactInfo);

    ResponseData<ContactInfos> getContactInfo(String userId);


    /**
     *ymt数据同步到侬要贷涉及到的方法
     */
    List<Contact> findContactByUserId(String userId) throws Exception;

    void updateYmtUesrContact(ContactInfo contactInfo) throws Exception;

    void saveYmtUesrContact(Contact contact) throws Exception;
}
