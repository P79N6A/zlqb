package com.nyd.batch.service.util;


import com.nyd.batch.model.AddressBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Dengw on 2017/11/16
 */
@Component
public class MongoApi {
    private static Logger LOGGER = LoggerFactory.getLogger(MongoApi.class);
    @Autowired
    private MongoTemplate mongoTemplate;



    /**
     * 查询通讯录
     * @return
     */
    public List<AddressBook> getAddressBooks(String phoneNo) {
        Criteria criteria = Criteria.where("phoneNo").is(phoneNo);
        return mongoTemplate.find(new Query(criteria),AddressBook.class);
    }



}
