package com.nyd.user.dao.impl;

import com.nyd.user.entity.Detail;
import com.nyd.user.dao.UserDetailDao;
import com.nyd.user.model.UserDetailInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hwei on 2017/11/2.
 */
@Repository
public class UserDetailDaoImpl implements UserDetailDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(UserDetailInfo detailInfo) throws Exception {
        Detail detail = new Detail();
        BeanUtils.copyProperties(detailInfo,detail);
        crudTemplate.save(detail);
    }

    @Override
    public void update(UserDetailInfo detailInfo) throws Exception {
        Criteria criteria = Criteria.from(Detail.class)
                .whioutId()
                .where()
                .and("user_id", Operator.EQ, detailInfo.getUserId())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        detailInfo.setUserId(null);
        crudTemplate.update(detailInfo,criteria);
    }

    @Override
    public void delete(String userId) throws Exception {
        Detail detail = new Detail();
        detail.setDeleteFlag(1);
        Criteria criteria = Criteria.from(Detail.class)
                .whioutId()
                .where()
                .and("user_id", Operator.EQ, userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        crudTemplate.update(detail,criteria);
    }

    @Override
    public List<UserDetailInfo> getUserDetailsByUserId(String userId) throws Exception {
        UserDetailInfo userDetailInfo = new UserDetailInfo();
        userDetailInfo.setUserId(userId);
        return getUserDetails(userDetailInfo);
    }

    private List<UserDetailInfo> getUserDetails(UserDetailInfo userDetailInfo) throws Exception {
        Criteria criteria = Criteria.from(Detail.class)
                .where()
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc")
                .limit(0,1000);
        return crudTemplate.find(userDetailInfo,criteria);
    }
    @Override
    public List<UserDetailInfo> getUserDetailsByIdNumber(String IdNumber) throws Exception {
        Criteria criteria = Criteria.from(Detail.class)
                .where()
                .and("id_number",Operator.EQ,IdNumber)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc")
                .limit(0,1000);
        UserDetailInfo userDetailInfo = new UserDetailInfo();
        return crudTemplate.find(userDetailInfo,criteria);
    }

    @Override
    public void updateUserDetailTest(String idNumber, String updateIdNumber) throws Exception {
        Criteria criteria = Criteria.from(Detail.class)
                .whioutId()
                .where()
                .and("id_number", Operator.EQ,idNumber)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        Detail detail = new Detail();
        detail.setIdNumber(updateIdNumber);
        crudTemplate.update(detail,criteria);
    }

}
