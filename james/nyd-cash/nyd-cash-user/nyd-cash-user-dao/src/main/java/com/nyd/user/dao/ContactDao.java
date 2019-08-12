package com.nyd.user.dao;

import com.nyd.user.entity.Contact;
import com.nyd.user.model.ContactInfo;

import java.util.List;

/**
 * Created by Dengw on 17/11/2.
 */
public interface ContactDao {
    void save(Contact contact) throws Exception;

    void saveList(List<Contact> list) throws Exception;

    List<Contact> getContacts(String userId) throws Exception ;

    List<ContactInfo> getContacts(ContactInfo contactInfo) throws Exception;

    List<Contact> findContactByUserId(String userId) throws Exception;

    void updateYmtUesrContact(ContactInfo contactInfo) throws Exception;
    
    List<Contact> getUserContacts(String contactInfo) throws Exception;
}
