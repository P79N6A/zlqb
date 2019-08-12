package com.nyd.user.dao;

import com.nyd.user.entity.HitLog;

public interface HitLogDao {
    void save(HitLog hitLog) throws Exception;
}
