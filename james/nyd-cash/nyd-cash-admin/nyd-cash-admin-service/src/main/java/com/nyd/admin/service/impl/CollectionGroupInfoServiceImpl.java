package com.nyd.admin.service.impl;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.CollectionCompanyInfoDao;
import com.nyd.admin.dao.CollectionGroupInfoDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.CollectionUserMapper;
import com.nyd.admin.model.CollectionGroupInfoVo;
import com.nyd.admin.model.dto.CollectionCompanyInfoDto;
import com.nyd.admin.model.dto.CollectionGroupInfoDto;
import com.nyd.admin.model.dto.CollectionUserInfoDto;
import com.nyd.admin.service.CollectionGroupInfoService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.DateUtils;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionGroupInfoServiceImpl implements CollectionGroupInfoService {
    private static Logger LOGGER = LoggerFactory.getLogger(CollectionGroupInfoServiceImpl.class);

    @Autowired
    CollectionGroupInfoDao collectionGroupInfoDao;
    @Autowired
    CollectionCompanyInfoDao collectionCompanyInfoDao;
    @Autowired
    CollectionUserMapper collectionUserMapper;
    /**
     * 分组新增
     * @param dto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData saveGroupInfo(CollectionGroupInfoDto dto) {
        ResponseData responseData = ResponseData.success();
        if(StringUtils.isBlank(dto.getGroupName())){
            responseData = ResponseData.error("分组名称不能为空！");
            return responseData;
        }
        if(dto.getCompanyId()==null){
            responseData = ResponseData.error("所属机构id不能为空！");
            return responseData;
        }

        CollectionCompanyInfoDto companyDtoQuery = new CollectionCompanyInfoDto();
        companyDtoQuery.setId(dto.getCompanyId());
        companyDtoQuery.setDeleteFlag(0);   //正常状态，未删除
        try{
            Page<CollectionCompanyInfoDto> pageVoList = collectionCompanyInfoDao.queryCompanyInfo(companyDtoQuery,1,10);
            if (pageVoList.getList()==null && pageVoList.getList().size() == 0){
                responseData = ResponseData.error("所属机构不存在或已删除！");
                return responseData;
            }
        }catch (Exception e){
            LOGGER.error("saveGroupInfo error!", e);
            responseData = ResponseData.error("服务器开小差了");
            return responseData;
        }

        CollectionGroupInfoDto dtoQuery = new CollectionGroupInfoDto();
        BeanUtils.copyProperties(dto,dtoQuery);
        dtoQuery.setDeleteFlag(0);
        try {
            List<CollectionGroupInfoDto> groupList = collectionGroupInfoDao.queryGroupInfoListByCompanyId(dtoQuery);
            if(groupList!=null && groupList.size()>0){
                responseData = ResponseData.error("该机构下已存在此名称的有效分组！");
                return responseData;
            }
        }catch (Exception e){
            LOGGER.error("saveGroupInfo error!", e);
            responseData = ResponseData.error("服务器开小差了");
            return responseData;
        }

        try {
            collectionGroupInfoDao.save(dtoQuery);
        }catch (Exception e){
            LOGGER.error("saveGroupInfo error!", e);
            responseData = ResponseData.error("服务器开小差了");
        }

        return responseData;
    }

    /**
     * 查询所有分组信息
     *
     * @param dto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData queryGroupInfoList(CollectionGroupInfoDto dto) {
        ResponseData responseData = ResponseData.success();
        int pageNum = dto.getPageNum();
        if(pageNum == 0){
            pageNum = 1;
        }
        int pageSize = dto.getPageSize();
        if(pageSize == 0){
            pageSize = 10;
        }
        try{
            Page<CollectionGroupInfoDto> pageList = collectionGroupInfoDao.queryGroupInfoList(dto, pageNum, pageSize);
            List<CollectionGroupInfoDto> dtoList = pageList.getList();
            List<CollectionGroupInfoVo> returnList = new ArrayList();
            if(dtoList!=null && dtoList.size()>0){
                for(CollectionGroupInfoDto groupDto : dtoList){
                    CollectionGroupInfoVo groupVo = new CollectionGroupInfoVo();
                    BeanUtils.copyProperties(groupDto,groupVo);
                    groupVo.setCreateTime(DateUtils.convert(groupDto.getCreateTime()));
                    groupVo.setUpdateTime(DateUtils.convert(groupDto.getUpdateTime()));
                    //获取机构名称
                    CollectionCompanyInfoDto companyInfoDto = new CollectionCompanyInfoDto();
                    companyInfoDto.setId(groupDto.getCompanyId());
                    try{
                        Page<CollectionCompanyInfoDto> companyPageList = collectionCompanyInfoDao.queryCompanyInfo(companyInfoDto,1,10);
                        List<CollectionCompanyInfoDto> companyDtoList = companyPageList.getList();
                        if(companyDtoList!=null && companyDtoList.size()>0){
                            groupVo.setCompanyName(companyDtoList.get(0).getCompanyName());
                        }
                    }catch(Exception e){
                        LOGGER.error("queryGroupInfo error!", e);
                        return ResponseData.error("服务器开小差了");
                    }
                    returnList.add(groupVo);
                }
            }
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageNum(pageNum);
            pageInfo.setPages(pageSize);
            pageInfo.setTotal(pageList.getTotalSize());
            pageInfo.setList(returnList);
            responseData.setData(pageInfo);
        }catch (Exception e){
            LOGGER.error("queryGroupInfo error!", e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    /**
     * 根据所属机构id查询分组列表
     *
     * @param dto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData queryGroupInfoListByCompanyId(CollectionGroupInfoDto dto) {
        ResponseData responseData = ResponseData.success();
        List<Long> companyIdList = dto.getCompanyIdList();
        if(companyIdList==null || companyIdList.size()==0){
            responseData = ResponseData.error("所属机构id不能为空！");
            return responseData;
        }
        List<CollectionGroupInfoVo> returnList = new ArrayList<>();
        for(Long companyId : companyIdList){
            CollectionCompanyInfoDto companyInfoDto = new CollectionCompanyInfoDto();
            companyInfoDto.setId(companyId);
            String companyName = null;
            try{
                Page<CollectionCompanyInfoDto> companyPageList = collectionCompanyInfoDao.queryCompanyInfo(companyInfoDto,1,10);
                List<CollectionCompanyInfoDto> companyDtoList = companyPageList.getList();
                if(companyDtoList!=null && companyDtoList.size()>0){
                    companyName = companyDtoList.get(0).getCompanyName();
                }
            }catch(Exception e){
                LOGGER.error("queryGroupInfoListByCompanyId error!", e);
                return ResponseData.error("服务器开小差了");
            }

            CollectionGroupInfoDto dtoQuery = new CollectionGroupInfoDto();
            dtoQuery.setCompanyId(companyId);
            dtoQuery.setDeleteFlag(dto.getDeleteFlag());

            List<CollectionGroupInfoVo> list = new ArrayList<>();
            try{
                List<CollectionGroupInfoDto> dtoList = collectionGroupInfoDao.queryGroupInfoListByCompanyId(dtoQuery);
                if(dtoList!=null && dtoList.size()>0){
                    for(CollectionGroupInfoDto groupDto : dtoList){
                        CollectionGroupInfoVo returnVo = new CollectionGroupInfoVo();
                        BeanUtils.copyProperties(groupDto, returnVo);
                        returnVo.setCompanyName(companyName);
                        returnVo.setCreateTime(DateUtils.convert(groupDto.getCreateTime()));
                        returnVo.setUpdateTime(DateUtils.convert(groupDto.getUpdateTime()));
                        list.add(returnVo);
                    }
                }
            }catch (Exception e){
                LOGGER.error("queryGroupInfoListByCompanyId error!", e);
                return ResponseData.error("服务器开小差了");
            }
            returnList.addAll(list);
        }
        responseData.setData(returnList);
        return responseData;
    }

    /**
     * 修改分组信息
     * @param dto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData updateGroupInfo(CollectionGroupInfoDto dto) {
        ResponseData responseData = ResponseData.success();
        if(dto.getId() == null){
            responseData = ResponseData.error("分组id不能为空！");
            return responseData;
        }
        if(dto.getCompanyId()==null){
            responseData = ResponseData.error("所属机构id不能为空！");
            return responseData;
        }
        try{
            CollectionGroupInfoDto dtoQuery = new CollectionGroupInfoDto();
            dtoQuery.setId(dto.getId());
            dtoQuery.setDeleteFlag(0);
            Page<CollectionGroupInfoDto> pageVoList = collectionGroupInfoDao.queryGroupInfoList(dtoQuery,1,10);
            if (pageVoList.getTotalSize()==0){
                responseData = ResponseData.error("未查询到该分组id的信息，无法更改！");
                return responseData;
            }

            dtoQuery = new CollectionGroupInfoDto();
            dtoQuery.setCompanyId(dto.getCompanyId());
            dtoQuery.setGroupName(dto.getGroupName());
            dtoQuery.setDeleteFlag(0);
            List<CollectionGroupInfoDto> groupList = collectionGroupInfoDao.queryGroupInfoListByCompanyId(dtoQuery);
            if(groupList!=null && groupList.size()>0){
                responseData = ResponseData.error("该机构下已存在此名称的有效分组！");
                return responseData;
            }

            collectionGroupInfoDao.updateGroupInfo(dto);
        }catch (Exception e){
            LOGGER.error("updateGroupInfo error!", e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    /**
     * 删除分组，逻辑删除
     * @param dto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ResponseData deleteGroupInfo(CollectionGroupInfoDto dto) throws Exception {
        ResponseData responseData = ResponseData.success();
        CollectionGroupInfoDto newDto = new CollectionGroupInfoDto();
        newDto.setId(dto.getId());
        newDto.setDeleteFlag(1);    //删除标记，0正常，1已删除
        try {
            collectionGroupInfoDao.updateGroupInfo(newDto);
        }catch (Exception e){
            LOGGER.error("updateGroupInfo error!", e);
            throw e;
        }
        //根据分组id，删除其下的所有用户(逻辑删除)
        CollectionUserInfoDto userDto = new CollectionUserInfoDto();
        userDto.setGroupId(dto.getId());
        try{
            collectionUserMapper.deleteCollectionUserByGroupId(userDto);
        }catch (Exception e){
            LOGGER.error("deleteGroupInfo error!", e);
            throw e;
        }
        return responseData;
    }
}
