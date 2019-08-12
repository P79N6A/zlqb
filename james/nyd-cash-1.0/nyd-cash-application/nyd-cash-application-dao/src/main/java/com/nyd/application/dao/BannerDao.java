package com.nyd.application.dao;

import com.nyd.application.model.request.BannerModel;

import java.util.List;

/**
 * Created by Dengw on 2017/11/28
 */
public interface BannerDao {
    List<BannerModel> getBannersForApp(String appName) throws Exception;
    List<BannerModel> getBanners() throws Exception;
}
