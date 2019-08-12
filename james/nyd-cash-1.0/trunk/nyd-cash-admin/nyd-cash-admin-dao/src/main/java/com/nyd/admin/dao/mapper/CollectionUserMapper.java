package com.nyd.admin.dao.mapper;

import com.nyd.admin.dao.CrudDao;
import com.nyd.admin.entity.power.User;
import com.nyd.admin.model.CollectionUserInfoVo;
import com.nyd.admin.model.dto.CollectionUserInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jiaxy
 * @date 20180618
 */
public interface CollectionUserMapper extends CrudDao<User> {
    /**
     * 查询所有催收人员列表
     * @param dto
     * @return
     */
    List<CollectionUserInfoVo> queryAllCollectionUser(CollectionUserInfoDto dto);

    /**
     * 根据分组id查询所有催收人员
     * @param groupIdList
     * @return
     */
    List<CollectionUserInfoVo> queryCollectionUserByGroupId(
            @Param("groupIdList") List<Long> groupIdList,
            @Param("deleteFlag")Integer deleteFlag,
            @Param("disabledFlag")Integer disabledFlag
    );

    /**
     * 更新催收人员信息
     * @param dto
     */
    void updateCollectionUser(CollectionUserInfoDto dto);

    /**
     * 删除机构下所有催收人员
     * @param dto
     */
    void deleteCollectionUserByGroupId(CollectionUserInfoDto dto);

}
