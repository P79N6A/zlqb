package com.nyd.user.dao;

import com.nyd.user.entity.PhoneRegion;




public interface PhoneRegionDao {

    PhoneRegion getPhoneRegionsByMobile(String mobile) throws Exception;


}
