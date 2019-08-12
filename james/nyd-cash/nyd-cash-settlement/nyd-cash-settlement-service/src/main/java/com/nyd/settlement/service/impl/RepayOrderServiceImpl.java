package com.nyd.settlement.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.dao.mapper.RepayMapper;
import com.nyd.settlement.dao.mapper.RepayOrderMapper;
import com.nyd.settlement.model.dto.repay.RepayQueryDto;
import com.nyd.settlement.model.po.repay.RepayBillPo;
import com.nyd.settlement.model.po.repay.RepayOrderPo;
import com.nyd.settlement.model.vo.repay.RepayBillVo;
import com.nyd.settlement.model.vo.repay.RepayOrderVo;
import com.nyd.settlement.service.RepayOrderService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import com.nyd.settlement.service.struct.BillStruct;
import com.nyd.settlement.service.struct.RepayOrderStruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dengw on 2018/1/17
 */
@Service
public class RepayOrderServiceImpl implements RepayOrderService {
    private static Logger LOGGER = LoggerFactory.getLogger(RepayOrderServiceImpl.class);

    @Autowired
    RepayOrderMapper repayOrderMapper;

    @Autowired
    RepayMapper repayMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MYSQL)
    @Override
    public PageInfo<RepayOrderVo> findRepayPage(RepayQueryDto dto) {
        if (StringUtils.isBlank(dto.getEndDate())) {
            dto.setEndDate(null);
        }
        if (StringUtils.isBlank(dto.getStartDate())) {
            dto.setStartDate(null);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderBy());
        List<RepayOrderPo> listPo =  repayOrderMapper.repayOrderList(dto);
        return RepayOrderStruct.INSTANCE.poPage2VoPage(new PageInfo(listPo));
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<RepayBillVo> getBillList(String orderNo) {
        List<RepayBillPo> listPo = repayMapper.queryBillList(orderNo);
        return BillStruct.INSTANCE.poList2VoList(listPo);
    }

}
