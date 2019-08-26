package com.nyd.user.service.impl;

import com.nyd.user.api.DrainageChannelService;
import com.nyd.user.dao.UserDrainageChannelDao;
import com.nyd.user.dao.mapper.UserDrainageChannelMapper;
import com.nyd.user.entity.UserDrainageChannel;
import com.nyd.user.model.vo.DrainageChannelCheckVo;
import com.nyd.user.model.vo.UserDrainageChannelVo;
import com.nyd.user.service.util.BeanUtils;
import com.nyd.zeus.model.helibao.util.xunlian.R;
import com.tasfe.framework.support.model.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 引流渠道
 * @author san
 */
@Service("drainageChannelService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DrainageChannelServiceImpl implements DrainageChannelService {

    private final UserDrainageChannelDao userDrainageChannelDao;

    private final UserDrainageChannelMapper userDrainageChannelMapper;

    @Override
    public ResponseData<UserDrainageChannel> checkState(DrainageChannelCheckVo drainageChannelCheckVo) {
        String drainageChannelName = drainageChannelCheckVo.getDrainageChannelName();

        ResponseData<UserDrainageChannel> responseData = new ResponseData<>();
        if (drainageChannelName == null){
            responseData.setCode("500");
            responseData.setMsg("失败");
            responseData.setStatus("1");
            return responseData;
        }
        try {
            UserDrainageChannel userDrainageChannel = userDrainageChannelMapper.getUserDrainageChannelByDrainageChannelName(drainageChannelCheckVo.getDrainageChannelName());
            if (userDrainageChannel == null){
                responseData.setCode("500");
                responseData.setMsg("渠道不存在");
                responseData.setStatus("1");
                return responseData;
            }
            responseData.setData(userDrainageChannel);
            responseData.setCode("200");
            responseData.setMsg("成功");
            responseData.setStatus("0");
        } catch (Exception e) {
            responseData.setCode("500");
            responseData.setMsg("失败");
            responseData.setStatus("1");
            e.printStackTrace();
        }
        return responseData;
    }

    @Override
    public ResponseData<String> save(UserDrainageChannelVo userDrainageChannelVo) {
        ResponseData<String> responseData = new ResponseData<>();
        String createMan = userDrainageChannelVo.getCreateMan();
        if (createMan == null){
            responseData.setCode("500");
            responseData.setMsg("createMan参数错误");
            responseData.setStatus("1");
            return responseData;
        }
        String userDrainageChannelVoDrainageChannelName = userDrainageChannelVo.getDrainageChannelName();

        if (userDrainageChannelVoDrainageChannelName == null){
            responseData.setCode("500");
            responseData.setMsg("userDrainageChannelVoDrainageChannelName参数错误");
            responseData.setStatus("1");
            return responseData;
        }
        try {
            UserDrainageChannel userDrainageChannel = userDrainageChannelMapper.getUserDrainageChannelByDrainageChannelName(userDrainageChannelVoDrainageChannelName);
            if (userDrainageChannel != null){
                responseData.setCode("500");
                responseData.setMsg("该渠道已经存在");
                responseData.setStatus("1");
                return responseData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userDrainageChannelVo.setCreateTime(new Date());
            userDrainageChannelVo.setState(1);
            userDrainageChannelMapper.insertSelective(BeanUtils.copy(userDrainageChannelVo, UserDrainageChannel.class));
            responseData.setCode("200");
            responseData.setMsg("成功");
            responseData.setStatus("0");
        } catch (Exception e) {
            responseData.setCode("500");
            responseData.setMsg("失败");
            responseData.setStatus("1");
            e.printStackTrace();
        }
        return responseData;
    }

    @Override
    public ResponseData<String> update(UserDrainageChannelVo userDrainageChannelVo) {
        return null;
    }

    @Override
    public ResponseData<List<UserDrainageChannel>> page(UserDrainageChannelVo userDrainageChannelVo) {
        return null;
    }
}
