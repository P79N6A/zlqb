package com.nyd.user.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nyd.user.dao.UserBindDao;
import com.nyd.user.dao.mapper.UserBindMapper;
import com.nyd.user.entity.UserBind;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;

@Repository
public class UserBindDaoImpl implements UserBindDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    
    @Autowired
    UserBindMapper userBindMapper;

    @Override
    public List<UserBind> selectByUserId(String userid, String cardno)  throws Exception{
    	UserBind user = new UserBind();
        user.setUserId(userid);
        user.setCardNo(cardno);
        Criteria criteria = Criteria.from(UserBind.class)
                .where()
                .and("userId", Operator.EQ,userid).and("cardNo", Operator.EQ, cardno)
                .endWhere();
        List<UserBind> result = crudTemplate.find(user,criteria);
        return result;
    }

    @Override
    public void save(UserBind user) throws Exception {
        crudTemplate.save(user);
    }

	@Override
	public void update(UserBind user) {
		userBindMapper.update(user);
	}

}
