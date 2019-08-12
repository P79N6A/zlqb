package com.nyd.batch.service.impls;

import com.nyd.batch.dao.ds.DataSourceContextHolder;
import com.nyd.batch.dao.mapper.BillMapper;
import com.nyd.batch.entity.Bill;
import com.nyd.batch.entity.OverdueBill;
import com.nyd.batch.service.BillBatchService;
import com.nyd.batch.service.BillTaskService;
import com.nyd.batch.service.aspect.RoutingDataSource;
import com.nyd.batch.service.enums.BillStatusEnum;
import com.nyd.batch.service.quartz.BillTask;
import com.nyd.zeus.model.OverdueBillInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhujx on 2017/11/20.
 * 跑批任务实现
 */
@Service
public class BillTaskServiceImpl implements BillTaskService {

    private static Logger LOGGER = LoggerFactory.getLogger(BillTask.class);

    @Autowired
    BillMapper billMapper;

    @Autowired
    BillBatchService billBatchService;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void doBillTask() {
        //1、查询账单数据
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("billStatus", BillStatusEnum.REPAY_ING.getCode());
        Date date = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DAY_OF_YEAR,-1);//日期减一天
        date = rightNow.getTime();


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        map.put("startDate",df.format(date));
        map.put("endDate",df.format(date));

        List<Bill> billList = billMapper.getBillLs(map);

        LOGGER.info("billList:" + billList.size());

        //2、处理业务数据
        //2.1跑批检索出正常账单中的逾期数据，并插入逾期账单中
        if(billList != null && billList.size() > 0){
            for(Bill bill : billList){
                try {
                    billBatchService.updateBillInfoAndOverdueBillInfo(bill);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //3、查询逾期账单数据
        map = new HashMap<String,Object>();
        map.put("billStatus", BillStatusEnum.REPAY_OVEDUE.getCode());
        List<OverdueBill> overdueBills = billMapper.getOverdueBillLs(map);

        //4、更新逾期账单数据
        if(overdueBills != null && overdueBills.size() > 0){
            for(OverdueBill overdueBill : overdueBills){
                OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
                BeanUtils.copyProperties(overdueBill,overdueBillInfo);
                //更新逾期账单
                billBatchService.updateOverdueBillInfo(overdueBillInfo);
            }
        }
    }
}
