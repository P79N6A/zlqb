package com.nyd.msg.dao;

import com.nyd.msg.entity.SysSmsConfig;

import java.util.List;

public interface ISysSmsConfigDao {
    List<SysSmsConfig> queryList();
    
     List<SysSmsConfig> querySmsReportList();
}
