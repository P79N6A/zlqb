package com.nyd.user.dao.impl;

import com.nyd.user.dao.mapper.ContactMapper;
import com.nyd.user.entity.Contact;
import com.nyd.user.dao.ContactDao;
import com.nyd.user.model.ContactInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 17/11/2.
 */
@Repository
public class ContactDaoImpl implements ContactDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Autowired
    private ContactMapper contactMapper;

    @Override
    public void save(Contact contact) throws Exception {
        crudTemplate.save(contact);
    }

    @Override
    public void saveList(List<Contact> list) throws Exception {
        contactMapper.insertList(list);
    }

    @Override
    public List<Contact> getContacts(String userId) throws Exception {
        Contact contact = new Contact();
        Criteria criteria = Criteria.from(Contact.class)
                .where()
                .and("user_id", Operator.EQ, userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc")
                .limit(0,2);
        return crudTemplate.find(contact,criteria);
    }

    @Override
    public List<ContactInfo> getContacts(ContactInfo contactInfo) throws Exception {
        Criteria criteria = Criteria.from(Contact.class)
                .where()
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        return crudTemplate.find(contactInfo,criteria);
    }


    @Override
    public List<Contact> findContactByUserId(String userId) throws Exception {
        Contact contact = new Contact();
        Criteria criteria = Criteria.from(Contact.class)
                .where()
                .and("user_id", Operator.EQ, userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        return crudTemplate.find(contact,criteria);
    }

    @Override
    public void updateYmtUesrContact(ContactInfo contactInfo) throws Exception {
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactInfo,contact);
        Criteria criteria = Criteria.from(Contact.class)
                .whioutId()
                .where()
                .and("user_id", Operator.EQ,contactInfo.getUserId())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        contact.setUserId(null);
        crudTemplate.update(contact,criteria);
    }

    @Override
    public List<Contact> getUserContacts(String userId) throws Exception {
    	Contact contact = new Contact();
    	contact.setUserId(userId);;
        return getContacts(contact);
    }



    private List<Contact> getContacts(Contact contact) throws Exception {
    	Criteria criteria = Criteria.from(Contact.class)
                .where()
                .and("user_id", Operator.EQ,contact.getUserId())
                .endWhere();
        return crudTemplate.find(contact,criteria);
    }
}
