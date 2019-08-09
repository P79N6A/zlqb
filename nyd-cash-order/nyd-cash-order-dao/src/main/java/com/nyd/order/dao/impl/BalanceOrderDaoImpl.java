package com.nyd.order.dao.impl;

import com.nyd.order.dao.BalanceOrderDao;
import com.nyd.order.dao.OrderWentongDao;
import com.nyd.order.dao.mapper.OrderWentongMapper;
import com.nyd.order.entity.BalanceOrder;
import com.nyd.order.entity.OrderWentong;
import com.nyd.order.model.OrderWentongInfo;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuqiu
 */
@Repository
public class BalanceOrderDaoImpl implements BalanceOrderDao {

    final Logger logger = LoggerFactory.getLogger(BalanceOrderDaoImpl.class);
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Override
    public void save(BalanceOrder balanceOrder) throws Exception {
       crudTemplate.save(balanceOrder);
    }
}
