package com.nyd.settlement.service;

/**
 * Created by Dengw on 2017/12/8
 */
public interface AuthService {
    boolean judgeTimeout(String accountNumber,String deviceId);
}
