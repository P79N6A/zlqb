package com.nyd.application.dao;

import com.nyd.application.model.request.MessageModel;

/**
 * Created by Dengw on 2017/12/1
 */
public interface MessageDao {
    void save(MessageModel message) throws Exception;
}
