package com.nyd.zeus.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.dao.BillDao;
import com.nyd.zeus.entity.Bill;
import com.nyd.zeus.entity.BillProduct;
import com.nyd.zeus.entity.BillRepay;
import com.nyd.zeus.model.BillInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

/**
 * Created by zhujx on 2017/11/18.
 */
@Repository
public class BillDaoImpl implements BillDao{
	
	Logger logger = LoggerFactory.getLogger(BillDaoImpl.class);

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(BillInfo billInfo) throws Exception {
        Bill bill = new Bill();
        BeanUtils.copyProperties(billInfo,bill);
        crudTemplate.save(bill);
    }

    @Override
    public void update(BillInfo billInfo) throws Exception {
        Criteria criteria = Criteria.from(Bill.class)
                .whioutId()
                .where().and("bill_no", Operator.EQ,billInfo.getBillNo())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        billInfo.setBillNo(null);
        crudTemplate.update(billInfo,criteria);

    }

    @Override
    public List<BillInfo> getObjectsByOrderNo(String orderNo) throws Exception {
        Criteria criteria = Criteria.from(Bill.class)
                .where().and("order_no", Operator.EQ,orderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        BillInfo billInfo = new BillInfo();
        return crudTemplate.find(billInfo,criteria);
    }

    @Override
    public List<BillInfo> getObjectsByYmtOrderNo(String ibankOrderNo) throws Exception {
        Criteria criteria = Criteria.from(Bill.class)
                .where().and("ibank_order_no", Operator.EQ,ibankOrderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        BillInfo billInfo = new BillInfo();
        return crudTemplate.find(billInfo,criteria);
    }

    @Override
    public List<BillInfo> getObjectsByBillNo(String billNo) throws Exception {
        Criteria criteria = Criteria.from(Bill.class)
                .where().and("bill_no", Operator.EQ,billNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        BillInfo billInfo = new BillInfo();
        return crudTemplate.find(billInfo,criteria);
    }

    @Override
    public List<BillInfo> getObjectsByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(Bill.class)
                .where().and("user_id", Operator.EQ,userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        BillInfo billInfo = new BillInfo();
        return crudTemplate.find(billInfo,criteria);
    }

    @Override
    public BillInfo getObjectByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(Bill.class)
                .where().and("user_id", Operator.EQ,userId)
                .and("bill_status",Operator.EQ,"B003")
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc").limit(0,1);
        BillInfo billInfo = new BillInfo();
        List<BillInfo> billInfos=crudTemplate.find(billInfo,criteria);
        if (null!=billInfos && billInfos.size()>0){
            return  billInfos.get(0);
        }
        return null;
    }

	@Override
	public void saveBillProduct(BillProduct product) throws Exception {
		product.setCreateTime(new Date());
		crudTemplate.save(product);
	}

	@Override
	public void saveBillRepay(BillRepay repay) {
		try {
			crudTemplate.save(repay);
		} catch (Exception e) {
			logger.error("合利宝还款流水表保存异常："+JSONObject.toJSONString(repay));
			e.printStackTrace();
		}
	}
    
    
}
