package com.nyd.user.api;

import com.nyd.user.entity.UserDrainageChannel;
import com.nyd.user.model.vo.DrainageChannelCheckVo;
import com.nyd.user.model.vo.UserDrainageChannelVo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * 引流渠道服务
 * @author san
 */
public interface DrainageChannelService {
    /**
     * 检查渠道状态
     * @return 渠道信息
     */
    ResponseData<UserDrainageChannel> checkState(DrainageChannelCheckVo drainageChannelCheckVo);

    /**
     * 新增引流渠道
     * @return 渠道信息
     */
    ResponseData<String> save(UserDrainageChannelVo userDrainageChannelVo);

    /**
     * 新增引流渠道
     * @return 渠道信息
     */
    ResponseData<String> update(UserDrainageChannelVo userDrainageChannelVo);

    /**
     * 引流渠道分页列表
     * @return 渠道信息
     */
    ResponseData<List<UserDrainageChannel>> page(UserDrainageChannelVo userDrainageChannelVo);
}
