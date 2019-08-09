package com.nyd.settlement.service;

/**
 * Created by Dengw on 2018/1/4
 */
public interface SettlementRedisService {
    boolean judgeTimeout(String accountNo, String token);
}
