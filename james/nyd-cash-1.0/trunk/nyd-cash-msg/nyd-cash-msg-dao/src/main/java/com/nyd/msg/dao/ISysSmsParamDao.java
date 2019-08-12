package com.nyd.msg.dao;

import com.nyd.msg.entity.SysSmsParam;
import org.springframework.stereotype.Repository;

public interface ISysSmsParamDao {
    SysSmsParam selectBySourceType(Integer type);
}
