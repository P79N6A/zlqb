package com.nyd.user.dao.mapper;

import com.nyd.user.entity.Contact;

import java.util.List;

/**
 * Created by Dengw on 2017/11/10
 */
public interface ContactMapper {
    void insertList(List<Contact> list);
}
