package com.nyd.member.dao.impl;

import com.nyd.member.dao.MemberLogDao;
import com.nyd.member.entity.MemberLog;
import com.nyd.member.model.MemberLogModel;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Dengw on 2017/12/6
 */
@Repository
public class MemberLogDaoImpl implements MemberLogDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(MemberLog memberLog) throws Exception {
        crudTemplate.save(memberLog);
    }

    @Override
    public void update(String userId) throws Exception {
        Criteria criteria = Criteria.from(MemberLog.class)
                .whioutId()
                .where()
                .and("user_id", Operator.EQ, userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("start_time desc")
                .limit(0,1);
        MemberLogModel memberLogModel = new MemberLogModel();
        List<MemberLogModel> list = crudTemplate.find(memberLogModel,criteria);
        if (list!=null&&list.size()>0) {
            MemberLogModel model = list.get(0);
            Criteria update = Criteria.from(MemberLog.class)
                    .whioutId()
                    .where()
                    .and("user_id", Operator.EQ, userId)
                    .and("start_time", Operator.EQ, new Timestamp(model.getStartTime().getTime()))
                    .and("delete_flag",Operator.EQ,0)
                    .endWhere();
            MemberLog memberLog = new MemberLog();
            memberLog.setDebitFlag("1");
            crudTemplate.update(memberLog,update);
        }
    }

    @Override
    public MemberLogModel getMemberLogByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(MemberLog.class)
                .whioutId()
                .where()
                .and("user_id", Operator.EQ, userId)
                .and("debit_flag",Operator.EQ,1)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("start_time desc")
                .limit(0,1);
        MemberLogModel memberLogModel = new MemberLogModel();
        List<MemberLogModel> list = crudTemplate.find(memberLogModel,criteria);
        if (list!=null&&list.size()>0) {
            return list.get(0);
        }
        return null;
    }
    @Override
    public MemberLogModel getMemberLogWithMemberIdByUserId(String userId) throws Exception {
    	Criteria criteria = Criteria.from(MemberLog.class)
    			.whioutId()
    			.where()
    			.and("user_id", Operator.EQ, userId)
    			.and("debit_flag",Operator.EQ,2)
    			.and("delete_flag",Operator.EQ,0)
    			.endWhere()
    			.orderBy("start_time desc")
    			.limit(0,1);
    	MemberLogModel memberLogModel = new MemberLogModel();
    	List<MemberLogModel> list = crudTemplate.find(memberLogModel,criteria);
    	if (list!=null&&list.size()>0) {
    		return list.get(0);
    	}
    	return null;
    }

    @Override
    public void updateMemberLogByUserId(MemberLog memberLog) throws Exception {
        Criteria criteria = Criteria.from(MemberLog.class)
                .whioutId()
                .where()
                .and("user_id",Operator.EQ,memberLog.getUserId())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        memberLog.setUserId(null);
        crudTemplate.update(memberLog,criteria);
    }

    @Override
    public MemberLogModel getMemberLogByUserIdAndDibitFlag(String userId, String debitFlag) throws Exception {
        Criteria criteria = Criteria.from(MemberLog.class)
                .whioutId()
                .where()
                .and("user_id", Operator.EQ, userId)
                .and("debit_flag",Operator.EQ,debitFlag)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("start_time desc")
                .limit(0,1);
        MemberLogModel memberLogModel = new MemberLogModel();
        List<MemberLogModel> list = crudTemplate.find(memberLogModel,criteria);
        if (list!=null&&list.size()>0) {
            return list.get(0);
        }
        return null;
    }


    @Override
    public List<MemberLogModel> getMemberLog(String userId, String debitFlag) throws Exception {
        Criteria criteria = Criteria.from(MemberLog.class)
            .whioutId()
            .where()
            .and("user_id", Operator.EQ, userId)
            .and("debit_flag",Operator.EQ,debitFlag)
            .and("delete_flag",Operator.EQ,0)
            .endWhere();
        MemberLogModel memberLogModel = new MemberLogModel();
        return crudTemplate.find(memberLogModel,criteria);
    }

	@Override
	public MemberLogModel findMemberLogByUserId(String userId) throws Exception {
		 Criteria criteria = Criteria.from(MemberLog.class)
	                .whioutId()
	                .where()
	                .and("user_id", Operator.EQ, userId)	                
	                .and("delete_flag",Operator.EQ,0)
	                .endWhere()
	                .orderBy("start_time desc")
	                .limit(0,1);
	        MemberLogModel memberLogModel = new MemberLogModel();
	        List<MemberLogModel> list = crudTemplate.find(memberLogModel,criteria);
	        if (list!=null&&list.size()>0) {
	            return list.get(0);
	        }
	        return null;
	}


}
