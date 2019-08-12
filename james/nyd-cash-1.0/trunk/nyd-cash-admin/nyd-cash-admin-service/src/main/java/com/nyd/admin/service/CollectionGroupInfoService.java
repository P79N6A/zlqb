package com.nyd.admin.service;

import com.nyd.admin.model.dto.CollectionGroupInfoDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author jiaxy
 * @date 20180612
 */
public interface CollectionGroupInfoService {

    /**
     * 分组新增
     * @param dto
     * @return
     */
    ResponseData saveGroupInfo(CollectionGroupInfoDto dto);

    /**
     * 查询所有分组信息
     * @param dto
     * @return
     */
    ResponseData queryGroupInfoList(CollectionGroupInfoDto dto);

    /**
     * 根据所属机构id查询分组列表
     * @param dto
     * @return
     */
    ResponseData queryGroupInfoListByCompanyId(CollectionGroupInfoDto dto);

    /**
     * 修改分组信息
     * @param dto
     * @return
     */
    ResponseData updateGroupInfo(CollectionGroupInfoDto dto);

    /**
     * 删除分组，逻辑删除
     * @param dto
     * @return
     */
    ResponseData deleteGroupInfo(CollectionGroupInfoDto dto) throws Exception;
}
