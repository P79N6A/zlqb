package com.nyd.application.dao;

import com.nyd.application.entity.Advertising;

import java.util.List;

/**
 * Created by Dengw on 2017/11/28
 */
public interface AdvertisingDao {
    List<Advertising> getAdvertisings() throws Exception;
}
