package com.nyd.admin.dao;

import com.nyd.admin.model.Info.SendMsgRecordInfo;

import java.util.List;

public interface SendMsgRecordDao {

    List<SendMsgRecordInfo> findByStatus()throws Exception;

    void update(SendMsgRecordInfo sendMsgRecordInfo) throws Exception;
}
