package com.nyd.user.service;

import com.nyd.user.entity.HitLog;

public interface HitLogService {
    void saveHitLog(HitLog hitLog) throws Exception;
}
