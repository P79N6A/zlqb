package com.nyd.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.CollectionUserMapper;
import com.nyd.admin.model.CollectionUserInfoVo;
import com.nyd.admin.model.dto.CollectionUserInfoDto;
import com.nyd.admin.service.CollectionUserService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.RegexUtils;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiaxy
 * @date 20180618
 */
@Service
public class CollectionUserServiceImpl implements CollectionUserService {
    private static Logger LOGGER = LoggerFactory.getLogger(TransformReportServiceImpl.class);

    @Autowired
    private CollectionUserMapper collectionUserMapper;

    /**
     * 查询所有催收人员列表
     * @param dto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData<List<CollectionUserInfoVo>> queryAllCollectionUser(CollectionUserInfoDto dto) {
        ResponseData responseData = ResponseData.success();
        try{
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), true);
            List<CollectionUserInfoVo> voList = collectionUserMapper.queryAllCollectionUser(dto);
            responseData.setData(new PageInfo(voList));
        }catch (Exception e){
            LOGGER.error("queryAllCollectionUser has exception",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    /**
     * 根据分组id查询所有催收人员
     * @param dto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData queryCollectionUserByGroupId(CollectionUserInfoDto dto) {
        ResponseData responseData = ResponseData.success();
        List<Long> groupIdList = dto.getGroupIdList();
        if(groupIdList==null || groupIdList.size()==0){
            responseData = ResponseData.error("分组id不能为空！");
            return responseData;
        }
        List<CollectionUserInfoVo> voList = collectionUserMapper.queryCollectionUserByGroupId(groupIdList,dto.getDeleteFlag(),dto.getDisabledFlag());
        return responseData.setData(voList);
    }

    /**
     * 更新催收人员信息
     * @param dto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData updateCollectionUser(CollectionUserInfoDto dto) {
        ResponseData responseData = ResponseData.success();
        if(dto.getId()==null){
            responseData = ResponseData.error("用户id不能为空！");
            return responseData;
        }
        if(!StringUtils.isBlank(dto.getContactPhone())){
            if(!RegexUtils.checkMobile(dto.getContactPhone()) && !RegexUtils.checkPhone(dto.getContactPhone())){
                responseData = ResponseData.error("输入的联系电话格式不正确！");
                return responseData;
            }
        }

        if(!StringUtils.isBlank(dto.getTelNum())){
            if(!RegexUtils.checkExtension(dto.getTelNum())){
                responseData = ResponseData.error("输入的分机号格式不正确！");
                return responseData;
            }
        }

        try{
            collectionUserMapper.updateCollectionUser(dto);
        }catch (Exception e){
            LOGGER.error("updateCollectionUser has exception",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    /**
     * 删除催收人员（逻辑删除）
     * @param dto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData deleteCollectionUser(CollectionUserInfoDto dto) {
        CollectionUserInfoDto newDto = new CollectionUserInfoDto();
        newDto.setId(dto.getId());
        newDto.setDeleteFlag(1);
        return this.updateCollectionUser(newDto);
    }
}
