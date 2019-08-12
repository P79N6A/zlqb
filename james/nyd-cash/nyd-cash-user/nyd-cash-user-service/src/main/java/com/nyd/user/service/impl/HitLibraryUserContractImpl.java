package com.nyd.user.service.impl;

import com.nyd.user.api.HitLibraryUserContract;
import com.nyd.user.dao.mapper.HitLibraryUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shaoqing.liu
 * @date 2018/7/3 15:31
 */
@Service(value = "hitLibraryUserContract")
public class HitLibraryUserContractImpl implements HitLibraryUserContract {

    private static Logger LOGGER = LoggerFactory.getLogger(HitLibraryUserContractImpl.class);
    @Autowired
    private HitLibraryUserMapper hitLibraryUserMapper;

    /**
     * @param diffDate 已注册天数
     */
    @Override
    public void updateHitLibraryUser(int diffDate) {
        LOGGER.info("开始进行撞库记录生成");
        try {
            //删除撞库表中老转新用户
            int count = hitLibraryUserMapper.deleteHitLibraryUser(diffDate);
            //每天跑批生成撞库老数据 未填写完资料并且注册时间不超过7天 或者 已全部填写完成个人资料 为老户
            // 已过滤撞库表中存在的记录
            if (count >= 0) {
                hitLibraryUserMapper.insertHitLibraryUser(diffDate);
            }
        } catch (Exception ex) {
            LOGGER.error(String.format("updateHitLibraryUser Error"), ex);
        }
        LOGGER.info("撞库记录生成结束");

    }
}
