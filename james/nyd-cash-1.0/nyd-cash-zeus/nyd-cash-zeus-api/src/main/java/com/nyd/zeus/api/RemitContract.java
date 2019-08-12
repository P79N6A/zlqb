package com.nyd.zeus.api;

import com.nyd.zeus.model.RemitInfo;
import com.nyd.zeus.model.RemitModel;
import com.tasfe.framework.support.model.ResponseData;

import java.util.Date;
import java.util.List;

/**
 * Created by zhujx on 2017/11/21.
 */
public interface RemitContract {

    ResponseData save(RemitInfo remitInfo) throws Exception;

    ResponseData getRemitInfoByOrderNo(String orderNo) throws Exception;

    ResponseData updateStatus(String remitNo) throws Exception;

    ResponseData selectTime(String assetNo);

    //根据放款状态查询放款记录list
    ResponseData<List<RemitModel>> getByCreateTime(int state, Date startTime, Date endTime);

    ResponseData<List<RemitInfo>> getSuccessRemit(Date startTime, Date endTime);
}
