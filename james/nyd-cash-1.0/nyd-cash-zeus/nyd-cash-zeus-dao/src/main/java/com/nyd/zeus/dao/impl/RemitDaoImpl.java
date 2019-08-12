package com.nyd.zeus.dao.impl;

import com.nyd.zeus.dao.RemitDao;
import com.nyd.zeus.entity.Remit;
import com.nyd.zeus.model.RemitInfo;
import com.nyd.zeus.model.RemitModel;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.criteria.Where;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by zhujx on 2017/11/21.
 */
@Repository
public class RemitDaoImpl implements RemitDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(RemitInfo remitInfo) throws Exception {
        Remit remit = new Remit();
        BeanUtils.copyProperties(remitInfo,remit);
        crudTemplate.save(remit);
    }

    @Override
    public List<RemitInfo> getRemitInfoByOrderNo(String orderNo) throws Exception {
        Criteria criteria = Criteria.from(Remit.class)
                .where().and("order_no", Operator.EQ,orderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        RemitInfo remitInfo = new RemitInfo();
        return crudTemplate.find(remitInfo,criteria);
    }

    @Override
    public void update(String remitNo) throws Exception {
        Criteria criteria = Criteria.from(Remit.class)
                .whioutId().where().and("remit_no",Operator.EQ,remitNo)
                .endWhere();
        RemitInfo remitInfo = new RemitInfo();
        remitInfo.setRemitStatus("0");
        crudTemplate.update(remitInfo,criteria);
    }

    @Override
    public List<RemitModel> getByCreateTimeAndStatus(int status, Date startTime, Date endTime) throws Exception {
        Where where = Criteria.from(Remit.class).where();
        if(null!=startTime){
            where.and("create_time",Operator.GTE,new Timestamp(startTime.getTime()));
        }
        if(null!=endTime){
            where.and("create_time",Operator.LT,new Timestamp(endTime.getTime()));
        }
        Criteria criteria = where.and("remit_status",Operator.EQ,status + "")
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        RemitModel remitInfo = new RemitModel();
        return crudTemplate.find(remitInfo,criteria);
    }
}
