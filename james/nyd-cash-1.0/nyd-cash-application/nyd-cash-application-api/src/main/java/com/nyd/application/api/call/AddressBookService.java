package com.nyd.application.api.call;

import com.nyd.application.model.mongo.AddressBook;

public interface AddressBookService {
	public void save(AddressBook addressBook) throws Exception;

}
