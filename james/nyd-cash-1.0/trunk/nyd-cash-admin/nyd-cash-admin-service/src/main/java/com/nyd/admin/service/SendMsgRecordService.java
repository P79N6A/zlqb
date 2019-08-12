package com.nyd.admin.service;

import com.nyd.admin.model.Info.SendMsgRecordInfo;

import java.util.List;

public interface SendMsgRecordService {
    /**
     * 查找待发的短息
     * @return
     */
    List<SendMsgRecordInfo> findByStatus();


    /**
     * 修改短信的状态
     */
    void updateByPhone(SendMsgRecordInfo sendMsgRecordInfo);


}
