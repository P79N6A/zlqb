package com.nyd.user.dao;


import java.util.List;

import com.nyd.user.entity.HitAccount;
import com.nyd.user.model.HitAccountInfo;

/**
 * @Author: zhangp
 * @Description: 引流撞库dao
 * @Date: 11:05 2018/9/6
 */
public interface HitAccountDao {
    /**
     * 保存
     * @param hitAccount
     * @throws Exception
     */
    void insert (HitAccount hitAccount) throws Exception;

    /**
     * 根据手机号查找撞库信息
     * @param mobile
     * @return
     * @throws Exception
     */
    List<HitAccountInfo> getByMobile(String mobile) throws Exception;

    /**
     * 根据sha256查找撞库信息
     * @param sha256Str
     * @return
     * @throws Exception
     */
    List<HitAccountInfo> getBySha256Str(String sha256Str) throws Exception;
    /**
     * 根据md5查找撞库信息
     * @param md5Str
     * @return
     * @throws Exception
     */
    List<HitAccountInfo> getByMd5Str(String md5Str) throws Exception;

    /**
     * 根据md5加密后的字符串进行更新
     * @param hitAccountInfo
     * @throws Exception
     */
    void updateByMdStr(HitAccountInfo hitAccountInfo)throws Exception;
}
