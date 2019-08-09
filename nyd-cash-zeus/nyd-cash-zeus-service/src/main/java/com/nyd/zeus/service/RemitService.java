package com.nyd.zeus.service;

import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by zhujx on 2018/1/8.
 */
public interface RemitService {

    ResponseData save(RemitInfo remitInfo) throws Exception;


    ResponseData getRemitInfoByOrderNo(String orderNo) throws Exception;
}
