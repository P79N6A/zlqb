package com.nyd.application.dao.impl;

import com.nyd.application.dao.MessageDao;
import com.nyd.application.entity.Message;
import com.nyd.application.model.request.MessageModel;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by Dengw on 2017/12/1
 */
@Repository
public class MessageDaoImpl implements MessageDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(MessageModel message) throws Exception {
        Message msg = new Message();
        BeanUtils.copyProperties(message,msg);
        crudTemplate.save(msg);
    }
}
