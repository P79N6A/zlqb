package com.nyd.application.dao;


import com.nyd.application.entity.Version;

import java.util.List;

/**
 * Created by Dengw on 2017/11/27
 */
public interface VersionDao {
    List<Version> getVersion(String os, String appName) throws Exception;

}
