package com.nyd.admin.dao;

import com.nyd.admin.model.dto.CollectionCompanyInfoDto;
import com.tasfe.framework.crud.api.params.Page;

/**
 * @author jiaxy
 * @date 20180611
 */
public interface CollectionCompanyInfoDao {
    /**
     * 新增机构
     * @param dto
     */
    void save(CollectionCompanyInfoDto dto) throws Exception;

    /**
     * 根据名称查询机构
     * @param dto
     * @return
     */
    Page<CollectionCompanyInfoDto> queryCompanyInfo(CollectionCompanyInfoDto dto, int pageNum, int pageSize) throws Exception;

    /**
     * 更新机构信息
     * @param dto
     */
    void update(CollectionCompanyInfoDto dto) throws Exception;
}
