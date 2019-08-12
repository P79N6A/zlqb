package com.nyd.user.service;

import com.nyd.user.entity.Download;

/**
 * Created by zhujx on 2017/12/27.
 */
public interface DownloadService {
    /**
     * 查找下载链接
     * @param os
     * @param appName
     * @return
     */
    Download getDownload(String os,String appName);
}
