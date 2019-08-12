package com.nyd.batch.service.impls;

import com.nyd.batch.dao.mapper.BillMapper;
import com.nyd.batch.entity.Bill;
import com.nyd.batch.model.ExpireUser;
import com.nyd.batch.service.RepayService;
import com.nyd.batch.service.excel.ExcelKit;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Cong Yuxiang
 * 2018/1/10
 **/
@Service
public class RepayServiceImpl implements RepayService{
    Logger logger = LoggerFactory.getLogger(RepayServiceImpl.class);
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private OrderDetailContract orderDetailContract;
    @Autowired
    private OrderContract orderContract;

    @Override
    public void repayBillOnTheDay() {
        Map map = new HashMap<>();
        map.put("repayDate", DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
        List<Bill> billList = billMapper.getBillsRepayOnTheDay(map);
        List<ExpireUser> result = new ArrayList<>();
        for(Bill bill:billList){
            ExpireUser user = new ExpireUser();
            user.setNotReceiveAmount(bill.getWaitRepayAmount().toString());
            user.setReceiveAmount(bill.getAlreadyRepayAmount().toString());
            user.setPromiseTime(DateFormatUtils.format(bill.getPromiseRepaymentDate(),"yyyy-MM-dd"));
            String orderNo = bill.getOrderNo();
            OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
            int loopCount=10;
            while(orderDetailInfo==null&&loopCount>0){
                loopCount--;
                orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
            }
            if(orderDetailInfo==null){
                logger.error("repayBillOnTheDay orderDetailInfo为null"+orderNo);
                continue;
            }

            user.setName(orderDetailInfo.getRealName());
            user.setIdCard(orderDetailInfo.getIdNumber());
            user.setMobile(orderDetailInfo.getMobile());

            OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
            loopCount=2;
            while(orderInfo==null&&loopCount>0){
                loopCount--;
                orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
            }
            if(orderInfo==null){
                logger.error("repayBillOnTheDay orderInfo为null"+orderNo);
                result.add(user);
                continue;
            }
            user.setBorrowAmount(orderInfo.getLoanAmount().toString());
            result.add(user);

        }

        File file = new File("/data/cuimi/当天要还款账单"+DateFormatUtils.format(new Date(),"yyyy-MM-dd")+".xlsx");
        file.delete();
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            ExcelKit.$Builder(ExpireUser.class).toExcel(result,"侬要贷",outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(outputStream==null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
