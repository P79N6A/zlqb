package com.nyd.admin.ws.controller;

import com.nyd.admin.model.BusinessChartVo;
import com.nyd.admin.model.BusinessReportVo;
import com.nyd.admin.service.BusinessReportService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * Peng
 * 2017/12/11
 **/
@RestController
@RequestMapping("/admin/businessReport")
public class BusinessReportController {

    @Autowired
    BusinessReportService businessReportService;


    @RequestMapping("/businessReportQuery")
    public ResponseData queryTransformReport(@RequestBody BusinessReportVo vo) {
        try {
            return ResponseData.success(businessReportService.findPage(vo));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResponseData.error();
    }

    @RequestMapping("/businessChart")
    public ResponseData queryTransformChart(@RequestBody BusinessChartVo vo) {
        return businessReportService.getBusinessChart(vo);
    }
}
