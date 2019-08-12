package com.creativearts.nyd.pay.service.impl;

import com.creativearts.nyd.pay.service.utils.LogNamingStrategy;

/**
 * 文件名命名
 * Cong Yuxiang
 * 2017/11/20
 **/
public class PayResponseDataNamingStrategy implements LogNamingStrategy{
    @Override
    public String generate() {
        return "log-" + 1;
    }


}
