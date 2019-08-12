package com.nyd.admin.service.impl;

import com.nyd.admin.api.CollectionUserInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.CollectionUserMapper;
import com.nyd.admin.model.CollectionUserManegeModel.CollectionUserModel;
import com.nyd.admin.model.CollectionUserInfoVo;
import com.nyd.admin.model.dto.CollectionUserInfoDto;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.ResponseDataUtil;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiaxy
 * @date 20180614
 */
@Service(value = "collectionUserInfo")
public class CollectionUserInfoImpl implements CollectionUserInfo {
    private static Logger LOGGER = LoggerFactory.getLogger(CollectionUserInfoImpl.class);
    @Autowired
    private CollectionUserMapper collectionUserMapper;

    /**
     * 依据条件查询查询催收人员信息
     * @param userModel
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData<List<CollectionUserInfoDto>> queryCollectionUserList(CollectionUserModel userModel) {
        ResponseData responseData = ResponseData.success();
        CollectionUserInfoDto dto = new CollectionUserInfoDto();
        BeanUtils.copyProperties(userModel, dto);
        try{
            List<CollectionUserInfoVo> voList = collectionUserMapper.queryAllCollectionUser(dto);
            if(voList!=null && voList.size()>0){
                responseData.setData(ResponseDataUtil.copyBeanList(voList, CollectionUserInfoDto.class));
            }
        }catch (Exception e){
            LOGGER.error("queryAllCollectionUser has exception",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

}
