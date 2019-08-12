package com.nyd.admin.service;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.FailReportVo;
import com.nyd.admin.model.dto.FailReportDto;

import java.text.ParseException;

public interface FailReportService {
    PageInfo<FailReportDto> findPage (FailReportVo vo) throws ParseException;
}
