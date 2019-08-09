package com.nyd.user.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author shaoqing.liu
 * @date 2018/7/3 9:52
 */
@Mapper
public interface HitLibraryUserMapper {


    int existHitLibraryUser(String accountNumber);

    /**
     * 每天跑批生成撞库老数据
     * 未填写完资料并且注册时间不超过7天 或者 已全部填写完成个人资料 为老户
     * 已过滤hitlibraryUser表中存在的记录
     * @param diffDate
     * @return
     */
    void insertHitLibraryUser(int diffDate);

    /**
     * 删除老转新数据
     * @param diffDate
     */
    int deleteHitLibraryUser(int diffDate);
}
