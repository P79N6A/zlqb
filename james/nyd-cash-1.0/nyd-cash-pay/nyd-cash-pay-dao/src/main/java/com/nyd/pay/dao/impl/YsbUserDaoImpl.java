package com.nyd.pay.dao.impl;

import com.nyd.pay.dao.YsbUserDao;
import com.nyd.pay.entity.YsbUser;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class YsbUserDaoImpl implements YsbUserDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<YsbUser> selectByUserId(String userid, String cardno)  throws Exception{
        YsbUser user = new YsbUser();
        user.setUserId(userid);
        user.setCardNo(cardno);
        Criteria criteria = Criteria.from(YsbUser.class)
                .where()
                .and("userId", Operator.EQ,userid).and("cardNo", Operator.EQ, cardno)
                .endWhere();
        List<YsbUser> result = crudTemplate.find(user,criteria);
        return result;
    }

    @Override
    public void save(YsbUser user) throws Exception {
        crudTemplate.save(user);
    }

//    private List<YsbUser> getYsbUser(YsbUser ysbuser) throws Exception {
//        Criteria criteria = Criteria.from(YsbUser.class)
//                .where()
//                .and("userId", Operator.EQ,ysbuser.getUserId()).and("cardNo",Operator.EQ, ysbuser.getCardNo())
//                .endWhere();
//
//        return crudTemplate.find(ysbuser,criteria);
//    }
}
