package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.SendMsgRecordDao;
import com.nyd.admin.entity.SendMsgRecord;
import com.nyd.admin.model.Info.SendMsgRecordInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class SendMsgRecordDaoImpl implements SendMsgRecordDao {

    @Resource(name = "mysql")
    private CrudTemplate crudTemplate;


    @Override
    public List<SendMsgRecordInfo> findByStatus() throws Exception {
        Criteria criteria =Criteria.from(SendMsgRecord.class)
                .where()
                .and("status", Operator.EQ,0)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        SendMsgRecordInfo sendSmsRecordInfo = new SendMsgRecordInfo();
        return crudTemplate.find(sendSmsRecordInfo,criteria);
    }

    @Override
    public void update(SendMsgRecordInfo sendMsgRecordInfo) throws Exception {
        Criteria criteria =Criteria.from(SendMsgRecord.class)
                .whioutId()
                .where()
                .and("mobile", Operator.EQ,sendMsgRecordInfo.getMobile())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        sendMsgRecordInfo.setMobile(null);
        crudTemplate.update(sendMsgRecordInfo,criteria);
    }
}
