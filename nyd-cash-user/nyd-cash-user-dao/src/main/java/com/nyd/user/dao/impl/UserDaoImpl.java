package com.nyd.user.dao.impl;

import com.nyd.user.entity.Account;
import com.nyd.user.entity.User;
import com.nyd.user.dao.UserDao;
import com.nyd.user.model.AccountInfo;
import com.nyd.user.model.PayFee;
import com.nyd.user.model.PayFeeInfo;
import com.nyd.user.model.UserInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dengw on 17/11/3.
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(UserInfo userInfo) throws Exception {
        User user = new User();
        BeanUtils.copyProperties(userInfo,user);
        crudTemplate.save(user);
    }

    @Override
    public void update(UserInfo userInfo) throws Exception {
        User user = new User();
        BeanUtils.copyProperties(userInfo,user);
        Criteria criteria = Criteria.from(User.class)
                .whioutId()
                .where()
                .and("user_id", Operator.EQ, user.getUserId())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        user.setUserId(null);
        crudTemplate.update(user,criteria);
    }

    @Override
    public void delete(String userId) throws Exception {
        User user = new User();
        user.setDeleteFlag(1);
        Criteria criteria = Criteria.from(User.class)
                .whioutId()
                .where()
                .and("user_id", Operator.EQ, userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        crudTemplate.update(user,criteria);
    }

    @Override
    public List<UserInfo> getUsersByIdNumber(String idNumber) throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setIdNumber(idNumber);
        return getUserByIdNumber(userInfo);
    }

    @Override
    public List<UserInfo> getUsersByUserId(String userId) throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        return getUsers(userInfo);
    }

    @Override
    public List<AccountInfo> getMobileByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(Account.class)
                .where()
                .and("user_id",Operator.EQ,userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        AccountInfo accountInfo = new AccountInfo();
        return crudTemplate.find(accountInfo,criteria);
    }

    @Override
    public List<UserInfo> getUsers(UserInfo userInfo) throws Exception {
        Criteria criteria = Criteria.from(User.class)
                .where()
                .and("delete_flag",Operator.EQ,0)
                .and("user_id",Operator.EQ,userInfo.getUserId())
                .endWhere()
                .orderBy("create_time desc")
                .limit(0,1000);
        userInfo.setAccountNumber(null);
        userInfo.setSource(null);
        userInfo.setChannel(null);
        return crudTemplate.find(userInfo,criteria);
    }

    public List<UserInfo> getUserByIdNumber(UserInfo userInfo) throws Exception {
        Criteria criteria = Criteria.from(User.class)
                .where()
                .and("delete_flag",Operator.EQ,0)
                .and("id_number",Operator.EQ,userInfo.getIdNumber())
                .endWhere()
                .orderBy("create_time desc")
                .limit(0,1000);
        userInfo.setAccountNumber(null);
        userInfo.setSource(null);
        userInfo.setChannel(null);
        return crudTemplate.find(userInfo,criteria);
    }

    @Override
    public void updateUserTest(String idNumber, String updateIdNumber) throws Exception {
        Criteria criteria = Criteria.from(User.class)
                .whioutId()
                .where()
                .and("id_number", Operator.EQ,idNumber)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        User user = new User();
        user.setIdNumber(updateIdNumber);
        crudTemplate.update(user,criteria);
    }

    @Override
    public List<PayFeeInfo> getPayFeeByBusiness(String business) throws Exception {
        Criteria criteria = Criteria.from(PayFee.class)
                .where()
                .and("business",Operator.EQ,business)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        PayFeeInfo payFeeInfo = new PayFeeInfo();
        return crudTemplate.find(payFeeInfo,criteria);
    }


}
