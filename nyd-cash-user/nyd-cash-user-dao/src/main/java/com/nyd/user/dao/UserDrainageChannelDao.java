package com.nyd.user.dao;

import com.nyd.user.entity.LoginLog;
import com.nyd.user.entity.UserDrainageChannel;
import com.nyd.user.model.vo.UserDrainageChannelVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author san
 * @since 2019-08-26
 */
public interface UserDrainageChannelDao {
    /**
     * 根据引流渠道名称获取引流渠道信息
     * @param drainageChannelName 引流渠道名称
     * @return 引流渠道信息
     */
    UserDrainageChannel getUserDrainageChannelByDrainageChannelName(String drainageChannelName) throws Exception;

    /**
     * 新增
     * @param userDrainageChannel 新增引流渠道信息
     */
    void save(UserDrainageChannel userDrainageChannel) throws Exception;
}
