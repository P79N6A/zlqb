package com.nyd.user.dao;

import com.nyd.user.entity.InnerWhiteUser;

import java.util.List;


public interface InnerWhiteUserDao {

    List<InnerWhiteUser> getObjectsByMobile(String mobile) throws Exception;


}
