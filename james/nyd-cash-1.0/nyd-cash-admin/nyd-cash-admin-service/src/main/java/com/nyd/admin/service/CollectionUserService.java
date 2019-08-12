package com.nyd.admin.service;

import com.nyd.admin.model.dto.CollectionUserInfoDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author jiaxy
 * @date 20180618
 */
public interface CollectionUserService {
    /**
     * 查询所有催收人员列表
     * @param dto
     * @return
     */
    ResponseData queryAllCollectionUser(CollectionUserInfoDto dto);

    /**
     * 根据分组id查询所有催收人员
     * @param dto
     * @return
     */
    ResponseData queryCollectionUserByGroupId(CollectionUserInfoDto dto);

    /**
     * 更新催收人员信息
     * @param dto
     * @return
     */
    ResponseData updateCollectionUser(CollectionUserInfoDto dto);

    /**
     * 删除催收人员（逻辑删除）
     * @param dto
     * @return
     */
    ResponseData deleteCollectionUser(CollectionUserInfoDto dto);
}
