package com.nyd.member.dao.impl;

import com.nyd.member.dao.MemberDao;
import com.nyd.member.entity.Member;
import com.nyd.member.model.MemberModel;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dengw on 2017/12/6
 */
@Repository
public class MemberDaoImpl implements MemberDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(Member member) throws Exception {
        crudTemplate.save(member);
    }

    @Override
    public void update(Member member) throws Exception {
        Criteria criteria = Criteria.from(Member.class)
                .whioutId()
                .where()
                .and("user_id", Operator.EQ, member.getUserId())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        member.setUserId(null);
        crudTemplate.update(member,criteria);
    }

    @Override
    public MemberModel getMemberByUserId(String userId) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        MemberModel memberModel = new MemberModel();
        memberModel.setUserId(userId);
        Criteria criteria = Criteria.from(Member.class)
                .where()
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        List<MemberModel> list = crudTemplate.find(memberModel,criteria);
        if (list!=null&&list.size()>0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<String> getMembers() throws Exception {
        MemberModel memberModel = new MemberModel();
        Date date = new Date();
        Criteria criteria = Criteria.from(Member.class)
                .where()
                .and("expire_time", Operator.GT, new Timestamp(date.getTime()))
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        List<MemberModel> list = crudTemplate.find(memberModel,criteria);
        List<String> list1 = new ArrayList<>();
        for (MemberModel model : list){
            list1.add(model.getUserId());
        }
        return list1;
    }
}
