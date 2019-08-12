package com.nyd.admin.ws.controller;

import com.nyd.admin.model.DspReportVo;
import com.nyd.admin.model.enums.DspSourceEnum;
import com.nyd.admin.service.DspReportService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * hwei
 * 2017/12/11
 **/
@RestController
@RequestMapping("/admin")
public class DspReportController {
    @Autowired
    DspReportService dspReportService;

    @RequestMapping(value = "/dspReport/querySuccess", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryDspSuccess(@RequestBody DspReportVo vo) {
        try {
            return dspReportService.queryDspSucess(vo);
        } catch (Exception e) {
            return ResponseData.error("服务器开小差了");
        }
    }

    @RequestMapping(value = "/dspReport", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryDspType() {
        ResponseData responseData = ResponseData.success();
        responseData.setData(DspSourceEnum.toList());
        return responseData;
    }

}
