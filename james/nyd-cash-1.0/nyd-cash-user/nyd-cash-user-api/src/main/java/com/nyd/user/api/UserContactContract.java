package com.nyd.user.api;

import com.nyd.user.model.ContactInfo;
import com.nyd.user.model.ContactInfos;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by Dengw on 2017/11/13
 */
public interface UserContactContract {
    ResponseData<ContactInfos> getContactInfo(String userId);

    ResponseData<List<ContactInfo>> getContactInfo(String name, String mobile);
}
