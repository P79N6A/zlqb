package com.nyd.user.dao.impl;

import com.nyd.user.dao.PasswordDao;
import com.nyd.user.entity.Password;
import com.nyd.user.model.AccountInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 2017/11/9
 */
@Repository
public class PasswordDaoImpl implements PasswordDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(AccountInfo accountInfo) throws Exception {
        Password password = new Password();
        BeanUtils.copyProperties(accountInfo,password);
        crudTemplate.save(password);
    }

    @Override
    public void update(AccountInfo accountInfo) throws Exception {
        Password password = new Password();
        Criteria criteria = Criteria.from(Password.class)
                .whioutId()
                .where()
                .and("account_number", Operator.EQ,accountInfo.getAccountNumber())
                .and("password_type", Operator.EQ,1)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        password.setPassword(accountInfo.getPassword());
        crudTemplate.update(password,criteria);
    }

    @Override
    public List<Password> getPasswordsByAccountNumber(String accountNumber) throws Exception {
        Password password= new Password();
        Criteria criteria = Criteria.from(Password.class)
                .whioutId()
                .where()
                .and("account_number", Operator.EQ,accountNumber)
                .and("password_type", Operator.EQ,1)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(password,criteria);
    }

    /**
     * ymt同步过来的新户密码
     * @param password
     */
    @Override
    public void updatPassword(Password password) throws Exception {
        Criteria criteria = Criteria.from(Password.class)
                .whioutId()
                .where()
                .and("account_number", Operator.EQ,password.getAccountNumber())
                .and("password_type", Operator.EQ,1)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        crudTemplate.update(password,criteria);
    }
    /**
     * ymt同步过来的新户密码
     * @param password
     */
    @Override
    public void updatPassword(Password password,String accountNumber) throws Exception {
    	Criteria criteria = Criteria.from(Password.class)
    			.whioutId()
    			.where()
    			.and("account_number", Operator.EQ,accountNumber)
    			.and("password_type", Operator.EQ,1)
    			.and("delete_flag",Operator.EQ,0)
    			.endWhere();
    	crudTemplate.update(password,criteria);
    }

    @Override
    public void savePassword(Password password) throws Exception {
        crudTemplate.save(password);
    }

    @Override
    public void updatePassWordTest(String accountNumber, String updateMobile) throws Exception {
        Criteria criteria = Criteria.from(Password.class)
                .whioutId()
                .where()
                .and("account_number", Operator.EQ,accountNumber)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        Password password = new Password();
        password.setAccountNumber(updateMobile);
        crudTemplate.update(password,criteria);
    }
}
