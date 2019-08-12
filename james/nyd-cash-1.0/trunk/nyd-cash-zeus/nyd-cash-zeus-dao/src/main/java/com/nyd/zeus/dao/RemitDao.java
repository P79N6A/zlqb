package com.nyd.zeus.dao;

import com.nyd.zeus.model.RemitInfo;
import com.nyd.zeus.model.RemitModel;

import java.util.Date;
import java.util.List;

/**
 * Created by zhujx on 2017/11/21.
 */
public interface RemitDao {

    void save(RemitInfo remitInfo) throws Exception;

    List<RemitInfo> getRemitInfoByOrderNo(String orderNo) throws Exception;

    void update(String remitNo)throws Exception;

    List<RemitModel> getByCreateTimeAndStatus(int status, Date startTime, Date endTime) throws Exception;

}
