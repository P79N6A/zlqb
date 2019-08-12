package com.nyd.application.dao.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.nyd.application.dao.AddressBookDao;
import com.nyd.application.entity.AddressBook;
import com.tasfe.framework.crud.core.CrudTemplate;
@Repository
public class AddressBookDaoImpl implements AddressBookDao {

	@Resource(name="mysql")
    private CrudTemplate crudTemplate;
	
	@Override
	public void save(com.nyd.application.model.mongo.AddressBook message) throws Exception {
		    AddressBook addressBook = new AddressBook();
		    addressBook.setCreateTime(new Date());
		    addressBook.setUpdateTime(new Date());
	        BeanUtils.copyProperties(message,addressBook);
	        addressBook.setUserId(message.getUserId());
	        //去除拉取手机号有空格的问题
	        if(StringUtils.isNotEmpty(addressBook.getTel())){
	        	addressBook.setTel(addressBook.getTel().replaceAll(" ", ""));
	        }
	        crudTemplate.save(addressBook);
	}
}
