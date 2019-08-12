package com.nyd.admin.service;

/**
 * Created by Dengw on 2018/1/4
 */
public interface AdminRedisService {
    boolean judgeTimeout(String accountNo,String token);
}
