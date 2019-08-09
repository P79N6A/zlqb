package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.BusinessReportDao;
import com.nyd.admin.entity.BusinessReport;
import com.nyd.admin.model.BusinessReportVo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
@Repository
public class BusinessReportDaoImpl implements BusinessReportDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Override
    public List<BusinessReportVo> getBusinessReport(BusinessReportVo vo) throws Exception {
         Criteria criteria = Criteria.from(BusinessReport.class)
                .orderBy("date desc");
        return crudTemplate.find(vo,criteria);
    }
}
