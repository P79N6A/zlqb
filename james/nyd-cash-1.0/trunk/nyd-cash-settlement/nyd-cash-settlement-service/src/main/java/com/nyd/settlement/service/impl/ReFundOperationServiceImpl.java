package com.nyd.settlement.service.impl;

import com.nyd.settlement.dao.RefundDao;
import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.dao.mapper.BillOrderMapper;
import com.nyd.settlement.entity.refund.Refund;
import com.nyd.settlement.model.dto.refund.DoRefundDto;
import com.nyd.settlement.service.ReFundOperationService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hwei on 2018/1/17.
 */
@Service
public class ReFundOperationServiceImpl implements ReFundOperationService {
    @Autowired
    RefundDao refundDao;
    @Autowired
    BillOrderMapper billOrderMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void saveRefund(Refund refund) throws Exception {
        refundDao.saveRefund(refund);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void updateBillRefundAmount(DoRefundDto doRefundDto) throws Exception {
        Map<String,Object> param = new HashMap<>();
        param.put("orderNo",doRefundDto.getOrderNo());
        param.put("billNo",doRefundDto.getBillNo());
        BigDecimal alreadyRefundAmount = billOrderMapper.queryAlreadRefundAmount(param);
        if (alreadyRefundAmount!=null) {
            alreadyRefundAmount = alreadyRefundAmount.add(doRefundDto.getRefundAmount());
        } else {
            alreadyRefundAmount = doRefundDto.getRefundAmount();
        }
        param.put("alreadyRefundAmount",alreadyRefundAmount);
        billOrderMapper.updateRefundAmount(param);
    }
}
