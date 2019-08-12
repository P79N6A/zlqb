package com.nyd.order.dao.YmtKzjrBill.impl;

import com.nyd.order.dao.YmtKzjrBill.BillYmtDao;
import com.nyd.order.entity.YmtKzjrBill.BillYmt;
import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.dto.BillYmtDto;
import com.nyd.order.model.YmtKzjrBill.enums.BillStatusEnum;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author cm
 */
@Repository
public class BillYmtDaoImpl implements BillYmtDao{

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<BillYmtInfo> selectByOrderSno(String orderSno) throws Exception {
        Criteria criteria = Criteria.from(BillYmt.class)
                .where().and("order_sno", Operator.EQ,orderSno)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        BillYmtInfo billInfo = new BillYmtInfo();
        return crudTemplate.find(billInfo,criteria);
    }


    @Override
    public List<BillYmtInfo> selectByAssetCode(String assetCode) throws Exception {
        Criteria criteria = Criteria.from(BillYmt.class)
                .where().and("asset_code", Operator.EQ,assetCode)
                .and("bill_status",Operator.NEQ, BillStatusEnum.REPAY_SUCCESS.getCode())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        BillYmtInfo billInfo = new BillYmtInfo();
        return crudTemplate.find(billInfo,criteria);
    }


    @Override
    public void updateByOrderSno(BillYmtInfo billYmtInfo) throws Exception {
        Criteria criteria = Criteria.from(BillYmt.class)
                .whioutId()
                .where().and("order_sno", Operator.EQ,billYmtInfo.getOrderSno())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        billYmtInfo.setOrderSno(null);
        crudTemplate.update(billYmtInfo,criteria);
    }

    @Override
    public void save(BillYmt billYmt) throws Exception {
        crudTemplate.save(billYmt);
    }

    @Override
    public List<BillYmtInfo> getByTimeAndStatus(BillYmtDto billYmtDto) throws Exception {
        Criteria criteria = Criteria.from(BillYmt.class)
                .where()
                .and("promise_repayment_date", Operator.GTE,new Timestamp(billYmtDto.getStartTime().getTime()))
                .and("promise_repayment_date", Operator.LTE,new Timestamp(billYmtDto.getEndTime().getTime()))
                .and("bill_status",Operator.EQ, billYmtDto.getStatus())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        BillYmtInfo billInfo = new BillYmtInfo();
        return crudTemplate.find(billInfo,criteria);
    }

    @Override
    public List<BillYmtInfo> getUnRepayBillByUserId(String ymtUserId) throws Exception {
        Criteria criteria = Criteria.from(BillYmt.class)
                .where().and("user_id", Operator.EQ,ymtUserId)
                .and("bill_status",Operator.NEQ, BillStatusEnum.REPAY_SUCCESS.getCode())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        BillYmtInfo billInfo = new BillYmtInfo();
        return crudTemplate.find(billInfo,criteria);
    }

    @Override
    public void updateByBillNo(BillYmtInfo billYmtInfo) throws Exception {
        Criteria criteria = Criteria.from(BillYmt.class)
                .whioutId()
                .where().and("bill_no", Operator.EQ,billYmtInfo.getBillNo())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        billYmtInfo.setBillNo(null);
        crudTemplate.update(billYmtInfo,criteria);
    }

    @Override
    public List<BillYmtInfo> getBillInfoByBillNo(String billNo) throws Exception {
        Criteria criteria = Criteria.from(BillYmt.class)
                .where().and("bill_no", Operator.EQ,billNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        BillYmtInfo billInfo = new BillYmtInfo();
        return crudTemplate.find(billInfo,criteria);
    }

    @Override
    public BillYmtInfo selectByAssetCodeAndPeriod(String assetCode,int currentPeriod,String  billStatus) throws Exception{
        Criteria criteria = Criteria.from(BillYmt.class)
                .where().and("asset_code", Operator.EQ,assetCode)
                .and("bill_status",Operator.EQ, billStatus)
                .and("cur_period",Operator.EQ, currentPeriod)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        BillYmtInfo billInfo = new BillYmtInfo();
        List<BillYmtInfo> list = crudTemplate.find(billInfo,criteria);
        if(list != null && list.size() >0){
            return list.get(0);
        }
        return null;
    }
}
