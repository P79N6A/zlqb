package com.nyd.admin.api;

import com.nyd.admin.model.CollectionUserManegeModel.CollectionUserModel;
import com.nyd.admin.model.dto.CollectionUserInfoDto;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * @author jiaxy
 * @date 20180614
 */
public interface CollectionUserInfo {

    /**
     * 依据用户账号查询查询催收人员信息
     * @param
     * @return
     */
    ResponseData<List<CollectionUserInfoDto>> queryCollectionUserList(CollectionUserModel collectionUserModel);
}
