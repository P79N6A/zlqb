package com.creativearts.nyd.pay.service.yinshengbao;

import com.nyd.pay.entity.YsbUser;

import java.util.List;
import java.util.Map;

public interface YsbUserService {


    void save(YsbUser user) throws Exception;

//    List<YsbUser> findByUserId(String userid, String cardno) throws Exception;

    List<YsbUser> findByUserId(Map<String, String> params);
}
