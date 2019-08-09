package com.nyd.admin.ws.controller;

import com.nyd.admin.model.TransformChartVo;
import com.nyd.admin.model.TransformReportVo;
import com.nyd.admin.model.enums.UserType;
import com.nyd.admin.service.TransformReportService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * Peng
 * 2017/12/11
 **/
@RestController
@RequestMapping("/admin/transformReport")
public class TransformReportController {

    @Autowired
    TransformReportService transformReportService;

    @RequestMapping("/queryTransformReportByCondition")
    public ResponseData queryTransformReportByCondition(@RequestBody TransformChartVo vo){
        return  transformReportService.getTransformInfo(vo);
    }

    @RequestMapping("/transformReportQuery")
    public ResponseData queryTransformReport(@RequestBody TransformReportVo vo, HttpServletRequest request) {
            String accountNo = request.getHeader("accountNo");
        try {
            return ResponseData.success(transformReportService.findPage(vo,accountNo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.error();
    }

}
