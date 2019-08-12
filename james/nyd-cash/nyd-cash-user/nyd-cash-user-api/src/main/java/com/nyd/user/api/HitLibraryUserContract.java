package com.nyd.user.api;

/**
 * @author shaoqing.liu
 * @date 2018/7/3 15:31
 */
public interface HitLibraryUserContract {

    /**
     * 每天进行撞库表更新
     * @param diffDate
     */
    void updateHitLibraryUser(int diffDate);
}
