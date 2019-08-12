package com.nyd.zeus.service;

/**
 * Created by Dengw on 2017/12/8
 */
public interface AuthService {
    boolean judgeTimeout(String accountNumber,String deviceId);
}
