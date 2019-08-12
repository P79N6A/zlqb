package com.nyd.user.dao;



import com.nyd.user.entity.UnbindUser;

import java.util.List;

/**
 * Created by hwei on 2018/11/12.
 */
public interface UnbindUserDao {
    void save(UnbindUser unbindUser) throws Exception;

    void updateByOriginMobile(String originMobile, UnbindUser unbindUser) throws Exception;

    List<UnbindUser> getUnbindUserByOriginMobile(String originMobile) throws Exception;
}
