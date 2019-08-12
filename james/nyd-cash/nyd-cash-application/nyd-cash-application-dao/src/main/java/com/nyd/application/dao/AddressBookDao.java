package com.nyd.application.dao;

import com.nyd.application.entity.AddressBook;

/**
 * 
 * @author admin
 *
 */
public interface AddressBookDao {
	 void save(com.nyd.application.model.mongo.AddressBook message) throws Exception;
}
