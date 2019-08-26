package com.nyd.user.dao.mapper;

import com.nyd.user.entity.UserDrainageChannel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author san
 * @since 2019-08-26
 */
@Mapper
@Repository
public interface UserDrainageChannelMapper {
    /**
     * 根据引流渠道名称获取引流渠道信息
     * @param drainageChannelName 引流渠道名称
     * @return 引流渠道信息
     */
    UserDrainageChannel getUserDrainageChannelByDrainageChannelName(@Param("drainageChannelName") String drainageChannelName) throws Exception;

    int deleteByPrimaryKey(Long id);

    int insert(UserDrainageChannel record);

    int insertSelective(UserDrainageChannel record);

    UserDrainageChannel selectByPrimaryKey(Long id);

    List<UserDrainageChannel> selectByCondistion(Map map);

    int updateByPrimaryKeySelective(UserDrainageChannel record);

    int updateByPrimaryKey(UserDrainageChannel record);
}
