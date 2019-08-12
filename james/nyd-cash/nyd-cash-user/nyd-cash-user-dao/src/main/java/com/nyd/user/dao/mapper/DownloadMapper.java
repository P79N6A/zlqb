package com.nyd.user.dao.mapper;

import com.nyd.user.entity.Download;
import org.apache.ibatis.annotations.Param;

/**
 * Created by zhujx on 2017/12/27.
 */
public interface DownloadMapper {
    /**
     * 查找下载链接
     * @param os
     * @param appName
     * @return
     */
    Download getDownload(@Param("os") String os,@Param("appName") String appName);
}
