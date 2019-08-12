package com.nyd.user.service.impl;

import com.nyd.user.dao.HitLogDao;
import com.nyd.user.entity.HitLog;
import com.nyd.user.service.HitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HitLogServiceImpl implements HitLogService {
    @Autowired
    private HitLogDao hitLogDao;

    @Override
    public void saveHitLog(HitLog hitLog) throws Exception {
        hitLogDao.save(hitLog);
    }
}
