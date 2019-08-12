package com.nyd.msg.dao.impl;

import com.nyd.msg.dao.ISysSmsConfigDao;
import com.nyd.msg.entity.SysSmsConfig;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SysSmsConfigDao implements ISysSmsConfigDao{
    Logger logger = LoggerFactory.getLogger(SysSmsConfigDao.class);
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;


    @Override
    public List<SysSmsConfig> queryList() {
        try {
            SysSmsConfig sysSmsConfig = new SysSmsConfig();
            sysSmsConfig.setStatus(1);
            Criteria criteria = Criteria.from(SysSmsConfig.class);
            return crudTemplate.find(sysSmsConfig,criteria);
        } catch (Exception e) {
            logger.info("查询数据库异常 {} " ,e);
            return null;
        }
    }


	@Override
	public List<SysSmsConfig> querySmsReportList() {
        try {
            SysSmsConfig sysSmsConfig = new SysSmsConfig();
            sysSmsConfig.setStatus(2);
            Criteria criteria = Criteria.from(SysSmsConfig.class);
            return crudTemplate.find(sysSmsConfig,criteria);
        } catch (Exception e) {
            logger.info("查询数据库异常 {} " ,e);
            return null;
        }
    }
    
    
}
