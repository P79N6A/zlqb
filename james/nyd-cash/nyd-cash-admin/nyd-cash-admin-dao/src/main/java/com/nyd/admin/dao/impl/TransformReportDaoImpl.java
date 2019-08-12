package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.TransformReportDao;
import com.nyd.admin.entity.TransformReport;
import com.nyd.admin.model.TransformReportVo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TransformReportDaoImpl implements TransformReportDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Override
    public List<TransformReportVo> getTransformReportByDate(String date) throws Exception {
        TransformReportVo vo = new TransformReportVo();
//        vo.setDate(date);
        return getTransformReport(vo);
    }

    @Override
    public List<TransformReportVo> getTransformReportBySource(String source) throws Exception {
        TransformReportVo vo = new TransformReportVo();
        vo.setSource(source);
        return getTransformReport(vo);
    }

    @Override
    public List<TransformReportVo> getTransformReport(TransformReportVo vo) throws Exception {
        Criteria criteria = Criteria.from(TransformReport.class)
                .orderBy("date desc");
        return crudTemplate.find(vo,criteria);
    }
}
