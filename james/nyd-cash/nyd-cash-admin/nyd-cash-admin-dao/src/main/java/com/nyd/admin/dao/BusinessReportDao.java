package com.nyd.admin.dao;

import com.nyd.admin.model.BusinessReportVo;

import java.util.List;

public interface BusinessReportDao {
    List<BusinessReportVo> getBusinessReport(BusinessReportVo vo)throws Exception;
}
