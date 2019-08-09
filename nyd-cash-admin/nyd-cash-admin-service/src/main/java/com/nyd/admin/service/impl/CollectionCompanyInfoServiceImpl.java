package com.nyd.admin.service.impl;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.CollectionCompanyInfoDao;
import com.nyd.admin.dao.CollectionGroupInfoDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.CollectionUserMapper;
import com.nyd.admin.model.CollectionCompanyInfoVo;
import com.nyd.admin.model.CollectionGroupInfoVo;
import com.nyd.admin.model.dto.CollectionCompanyInfoDto;
import com.nyd.admin.model.dto.CollectionGroupInfoDto;
import com.nyd.admin.model.dto.CollectionUserInfoDto;
import com.nyd.admin.service.CollectionCompanyInfoService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.DateUtils;
import com.nyd.admin.service.utils.RegexUtils;
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

/**
 * @author jiaxy
 * @date 20180611
 */
@Service
public class CollectionCompanyInfoServiceImpl implements CollectionCompanyInfoService {

    private static Logger LOGGER = LoggerFactory.getLogger(CollectionCompanyInfoServiceImpl.class);

    @Autowired
    CollectionCompanyInfoDao collectionCompanyInfoDao;
    @Autowired
    CollectionGroupInfoDao collectionGroupInfoDao;
    @Autowired
    CollectionUserMapper collectionUserMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData saveCompanyInfo(CollectionCompanyInfoDto dto) {
        ResponseData responseData = ResponseData.success();
        if(StringUtils.isBlank(dto.getCompanyName())){
            return ResponseData.error("机构名称不能为空！");
        }
        if (StringUtils.isBlank(dto.getCompanyType())) {
            return ResponseData.error("机构类型不能为空！");
        }
        if(StringUtils.isBlank(dto.getContactPerson())){
            return ResponseData.error("联系人不能为空！");
        }
        if(StringUtils.isBlank(dto.getContactPhone())){
            return ResponseData.error("联系电话不能为空！");
        }else{
            if(!RegexUtils.checkMobile(dto.getContactPhone()) && !RegexUtils.checkPhone(dto.getContactPhone())){
                return ResponseData.error("联系电话格式不正确！");
            }
        }

        if(!StringUtils.isBlank(dto.getContactEmail())){
            if(!RegexUtils.checkEmail(dto.getContactEmail())){
                return ResponseData.error("输入的邮箱格式不正确！");
            }
            if(dto.getContactEmail().length()>50){
                return ResponseData.error("邮箱长度不能超过50位字符！");
            }
        }

        try {
            CollectionCompanyInfoDto dtoQuery = new CollectionCompanyInfoDto();
            dtoQuery.setCompanyName(dto.getCompanyName());
            dtoQuery.setDeleteFlag(0);
            Page<CollectionCompanyInfoDto> pageInfo = collectionCompanyInfoDao.queryCompanyInfo(dtoQuery,1,10);
            if (pageInfo.getList()!=null && pageInfo.getList().size()>0){
                return ResponseData.error("该机构名称已存在！");
            }

            dto.setDeleteFlag(0);    //删除标记：0正常
            collectionCompanyInfoDao.save(dto);
        }catch (Exception e){
            LOGGER.error("saveCompanyInfo error!", e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData queryCompanyInfoList(CollectionCompanyInfoDto dto) {
        ResponseData responseData = ResponseData.success();
        int pageNum = dto.getPageNum();
        if(pageNum==0){
            pageNum = 1;
        }
        int pageSize = dto.getPageSize();
        if(pageSize == 0){
            pageSize = 10;
        }
        try{
            Page<CollectionCompanyInfoDto> pageList = collectionCompanyInfoDao.queryCompanyInfo(dto,pageNum,pageSize);
            List<CollectionCompanyInfoVo> voList = new ArrayList<>();
            List<CollectionCompanyInfoDto> dtoList = pageList.getList();
            if(dtoList!=null && dtoList.size()>0){
                for(CollectionCompanyInfoDto companyDto : dtoList){
                    CollectionCompanyInfoVo vo = new CollectionCompanyInfoVo();
                    BeanUtils.copyProperties(companyDto, vo);
                    vo.setCreateTime(DateUtils.convert(companyDto.getCreateTime()));
                    vo.setUpdateTime(DateUtils.convert(companyDto.getUpdateTime()));
                    voList.add(vo);
                }
            }
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageNum(pageNum);
            pageInfo.setPages(pageSize);
            pageInfo.setTotal(pageList.getTotalSize());
            pageInfo.setList(voList);
            responseData.setData(pageInfo);
        }catch (Exception e){
            LOGGER.error("queryCompanyInfo error!", e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData updateCompanyInfo(CollectionCompanyInfoDto dto) {
        ResponseData responseData = ResponseData.success();
        if(dto.getId() == null){
            return ResponseData.error("机构id不能为空！");
        }
        if(StringUtils.isBlank(dto.getCompanyName())){
            return ResponseData.error("机构名称不能为空！");
        }
        if(StringUtils.isBlank(dto.getCompanyType())){
            return ResponseData.error("机构类型不能为空！");
        }
        if(!StringUtils.isBlank(dto.getContactPhone())){
            if(!RegexUtils.checkMobile(dto.getContactPhone()) && !RegexUtils.checkPhone(dto.getContactPhone())){
                return ResponseData.error("输入的联系电话格式不正确！");
            }
        }

        if(!StringUtils.isBlank(dto.getContactEmail())){
            if(!RegexUtils.checkEmail(dto.getContactEmail())){
                return ResponseData.error("输入的邮箱格式不正确！");
            }
            if(dto.getContactEmail().length()>50){
                return ResponseData.error("邮箱长度不能超过50位字符！");
            }
        }

        try {
            //校验机构id
            CollectionCompanyInfoDto dtoQuery = new CollectionCompanyInfoDto();
            dtoQuery.setId(dto.getId());
            dtoQuery.setDeleteFlag(0);
            Page<CollectionCompanyInfoDto> pageInfo = collectionCompanyInfoDao.queryCompanyInfo(dtoQuery,1,10);
            if (pageInfo.getList() == null && pageInfo.getList().size() == 0) {
                return ResponseData.error("未查询到该机构id的有效信息，无法更改！");
            } else {    //若id已存在，说明可以做update，但要校验机构名称是否重复
                dtoQuery = new CollectionCompanyInfoDto();
                dtoQuery.setCompanyName(dto.getCompanyName());
                dtoQuery.setDeleteFlag(0);
                Page<CollectionCompanyInfoDto> pageInfoAgain = collectionCompanyInfoDao.queryCompanyInfo(dtoQuery,1,10);
                if (pageInfoAgain.getList() != null && pageInfoAgain.getList().size() != 0) { //存在相同名称的未删除机构信息
                    if(!dto.getId().equals(pageInfoAgain.getList().get(0).getId())){
                        return ResponseData.error("机构名称重复！");
                    }
                }
            }
            //执行更新
            collectionCompanyInfoDao.update(dto);
        }catch (Exception e){
            LOGGER.error("updateCompanyInfo error!", e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }


    @Transactional(rollbackFor=Exception.class)
    @Override
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    public ResponseData deleteCompanyInfo(CollectionCompanyInfoDto dto) throws Exception {
        ResponseData responseData = ResponseData.success();
        CollectionCompanyInfoDto newDto = new CollectionCompanyInfoDto();
        newDto.setId(dto.getId());
        newDto.setDeleteFlag(1);
        try{
            collectionCompanyInfoDao.update(newDto);
        }catch (Exception e){
            LOGGER.error("deleteCompanyInfo error!", e);
            throw e;
        }

        //删除机构下的所有分组
        CollectionGroupInfoVo groupVo = new CollectionGroupInfoVo();
        groupVo.setCompanyId(dto.getId());
        groupVo.setDeleteFlag(1);
        try {
            collectionGroupInfoDao.updateGroupInfoByCompanyId(groupVo);
        }catch (Exception e){
            LOGGER.error("deleteCompanyInfo error!", e);
            throw e;
        }

        //查询机构下所有分组id，然后删除每个分组下的催收人
        CollectionGroupInfoDto groupDto = new CollectionGroupInfoDto();
        groupDto.setCompanyId(dto.getId());
        try{
            List<CollectionGroupInfoDto> groupList = collectionGroupInfoDao.queryGroupInfoListByCompanyId(groupDto);
            if(groupList!=null && groupList.size()>0){
                groupList.stream().forEach(vo->{
                    CollectionUserInfoDto userDto = new CollectionUserInfoDto();
                    userDto.setGroupId(vo.getId());
                    collectionUserMapper.deleteCollectionUserByGroupId(userDto);
                });
            }
        }catch (Exception e){
            LOGGER.error("deleteCompanyInfo error!", e);
            throw e;
        }
        return responseData;
    }

}
