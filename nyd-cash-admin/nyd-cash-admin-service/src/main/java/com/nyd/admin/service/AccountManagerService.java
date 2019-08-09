package com.nyd.admin.service;


import java.util.List;
import java.util.Map;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 18:26 2018/10/1
 */
public interface AccountManagerService {

    List<String> findByTime(Map<String, String> timeParam);
}
