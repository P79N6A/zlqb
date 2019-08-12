package com.nyd.settlement.service.impl;

import com.nyd.settlement.dao.OrderCancelDao;
import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.service.KzjrService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Peng
 * @create 2018-01-25 21:03
 **/
@Service
public class KzjrServiceImpl implements KzjrService {
    private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KzjrServiceImpl.class);

    @Autowired
    OrderCancelDao orderCancelDao;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void deleteKzjr(String orderNo) {
        LOGGER.info("删除空中金融放款失败记录，orderNo is"+orderNo);
        try {
            orderCancelDao.deleteFailKzjr(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("删除空中金融放款失败表失败！！" + e);
        }
    }
}
