package com.nyd.user.service.impl;

import com.nyd.user.dao.mapper.DownloadMapper;
import com.nyd.user.entity.Download;
import com.nyd.user.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhujx on 2017/12/27.
 */
@Service
public class DownloadServiceImpl implements DownloadService {

    @Autowired
    DownloadMapper downloadMapper;

    @Override
    public Download getDownload(String os,String appName) {
        return downloadMapper.getDownload(os,appName);
    }
}
