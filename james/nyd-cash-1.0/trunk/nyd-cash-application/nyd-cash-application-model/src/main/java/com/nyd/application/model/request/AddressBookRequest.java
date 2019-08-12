package com.nyd.application.model.request;

import com.nyd.application.model.mongo.AddressBook;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuqiu
 */
@Data
public class AddressBookRequest implements Serializable {
    private String appName;
    private List<AddressBook> content;
    private String userId;
}
