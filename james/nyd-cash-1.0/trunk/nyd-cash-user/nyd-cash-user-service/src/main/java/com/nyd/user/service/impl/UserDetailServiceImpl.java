package com.nyd.user.service.impl;

import com.nyd.user.dao.UserDetailDao;
import com.nyd.user.entity.Detail;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private UserDetailDao userDetailDao;

    @Override
    public void saveUserDetail(UserDetailInfo userDetailInfo) throws Exception {
        userDetailDao.save(userDetailInfo);
    }
    @Override
    public void updateUserDetailByUserId(UserDetailInfo userDetailInfo) throws Exception {
    	userDetailDao.update(userDetailInfo);
    }
}
