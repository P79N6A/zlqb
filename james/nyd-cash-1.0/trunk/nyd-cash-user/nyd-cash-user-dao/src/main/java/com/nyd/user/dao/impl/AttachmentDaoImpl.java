package com.nyd.user.dao.impl;

import com.nyd.user.dao.AttachmentDao;
import com.nyd.user.entity.Attachment;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hwei on 2017/11/2.
 */
@Repository
public class AttachmentDaoImpl implements AttachmentDao{
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<Attachment> getAttachmentsByUserId(String userId) throws Exception{
        Attachment attachment = new Attachment();
        Criteria criteria= Criteria.from(Attachment.class)
                .where()
                .and("user_id", Operator.EQ, userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        return crudTemplate.find(attachment,criteria);
    }

    @Override
    public void save(Attachment attachment) throws Exception {
        crudTemplate.save(attachment);
    }
}
