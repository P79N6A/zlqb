package com.nyd.zeus.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.nyd.zeus.dao.PaymentRiskRecordDao;
import com.nyd.zeus.entity.PaymentRiskRecord;
import com.nyd.zeus.model.PaymentRiskRecordVo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;

/**
 * Created by zhujx on 2017/11/18.
 */
@Repository
public class PaymentRiskRecordExtendDaoImpl implements PaymentRiskRecordDao{
	public final static String STYLE_1 = "yyyy-MM-dd HH:mm:ss";
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(PaymentRiskRecordVo vo) throws Exception {
    	PaymentRiskRecord record = new PaymentRiskRecord();
        BeanUtils.copyProperties(vo,record);
        crudTemplate.save(record);
    }

    @Override
    public void update(PaymentRiskRecordVo vo) throws Exception {
    	PaymentRiskRecord record = new PaymentRiskRecord();
        BeanUtils.copyProperties(vo,record);
        Criteria criteria = Criteria.from(PaymentRiskRecord.class)
                .whioutId()
                .where().and("id", Operator.EQ,vo.getId())
                .endWhere();
        record.setUpdateTime(new SimpleDateFormat(STYLE_1).format(new Date()));
        crudTemplate.update(record,criteria);

    }


    @Override
    public PaymentRiskRecordVo getObjectById(String Id) throws Exception {
        Criteria criteria = Criteria.from(PaymentRiskRecord.class)
                .where().and("id", Operator.EQ,Id)
                .endWhere()
                .orderBy("request_time desc").limit(0,1);
        
        PaymentRiskRecord vo = new PaymentRiskRecord();
        List<PaymentRiskRecord> list=crudTemplate.find(vo,criteria);
        if (null!=list && list.size()>0){
        	PaymentRiskRecord record = list.get(0);
        	PaymentRiskRecordVo recordVo = new PaymentRiskRecordVo(); 
            BeanUtils.copyProperties(record,recordVo);
            return  recordVo;
        }
        return null;
    }

	@Override
	public List<PaymentRiskRecordVo> getReadyData() throws Exception {
		Criteria criteria = Criteria.from(PaymentRiskRecord.class)
                .where().and("status", Operator.EQ,"0")
                .endWhere()
                .orderBy("request_time");
        
		PaymentRiskRecordVo vo = new PaymentRiskRecordVo();
        List<PaymentRiskRecordVo> list=crudTemplate.find(vo,criteria);
        if (null!=list && list.size()>0){
        	return list;
        }
        return null;
	}
	@Override
	public List<PaymentRiskRecordVo> getProcessData() throws Exception {
		Criteria criteria = Criteria.from(PaymentRiskRecord.class)
				.where().and("status", Operator.EQ,"-1")
				.endWhere()
				.orderBy("request_time");
		
		PaymentRiskRecordVo vo = new PaymentRiskRecordVo();
		List<PaymentRiskRecordVo> list=crudTemplate.find(vo,criteria);
		if (null!=list && list.size()>0){
			return list;
		}
		return null;
	}
}
