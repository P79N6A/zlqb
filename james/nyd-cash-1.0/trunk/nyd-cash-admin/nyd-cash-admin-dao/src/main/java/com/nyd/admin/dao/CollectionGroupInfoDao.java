package com.nyd.admin.dao;

import com.nyd.admin.model.CollectionGroupInfoVo;
import com.nyd.admin.model.dto.CollectionGroupInfoDto;
import com.tasfe.framework.crud.api.params.Page;

import java.util.List;

/**
 * @author jiaxy
 * @date 20180612
 */
public interface CollectionGroupInfoDao {
    /**
     * 分组保存
     * @param dto
     */
    void save(CollectionGroupInfoDto dto) throws Exception;

    /**
     * 根据所属机构id查询分组列表
     * @param dto
     * @return
     */
    List<CollectionGroupInfoDto> queryGroupInfoListByCompanyId(CollectionGroupInfoDto dto) throws Exception;

    /**
     * 根据条件查询所有分组
     *
     * @param dto
     * @return
     */
    Page<CollectionGroupInfoDto> queryGroupInfoList(CollectionGroupInfoDto dto, int pageNum, int pageSize) throws Exception;

    /**
     * 修改分组信息
     * @param dto
     */
    void updateGroupInfo(CollectionGroupInfoDto dto) throws Exception;

    /**
     * 根据机构id修改分组信息
     * @param vo
     */
    void updateGroupInfoByCompanyId(CollectionGroupInfoVo vo) throws Exception;
}
