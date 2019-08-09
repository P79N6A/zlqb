package com.nyd.application.dao;

import com.nyd.application.model.request.WebModel;

import java.util.List;

/**
 * Created by Dengw on 2017/12/1
 */
public interface WebDao {
    List<WebModel> getWebInfos() throws Exception;
}
