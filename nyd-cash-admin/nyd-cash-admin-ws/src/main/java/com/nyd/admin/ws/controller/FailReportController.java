package com.nyd.admin.ws.controller;

import com.nyd.admin.model.FailReportVo;
import com.nyd.admin.service.FailReportService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * @author Peng
 * @create 2017-12-15 10:23
 **/
@RestController
@RequestMapping("/admin/failReport")
public class FailReportController {
    @Autowired
    FailReportService failReportService;

    @RequestMapping("/failReportQuery")
    public ResponseData queryFailReport(@RequestBody FailReportVo vo) {
        try {
            return ResponseData.success(failReportService.findPage(vo));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResponseData.error();
    }
}
