package com.nyd.admin.service;

import com.nyd.admin.model.dto.CollectionCompanyInfoDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author jiaxy
 * @date 20180611
 */
public interface CollectionCompanyInfoService {

    /**
     * 新增机构
     * @param dto
     */
    ResponseData saveCompanyInfo(CollectionCompanyInfoDto dto);

    /**
     * 查询机构列表
     * @param dto
     * @return
     */
    ResponseData queryCompanyInfoList(CollectionCompanyInfoDto dto);

    /**
     * 修改机构信息
     * @param dto
     * @return
     */
    ResponseData updateCompanyInfo(CollectionCompanyInfoDto dto);

    /**
     * 删除机构信息（逻辑删除）
     * @param dto
     * @return
     */
    ResponseData deleteCompanyInfo(CollectionCompanyInfoDto dto) throws Exception;
}
