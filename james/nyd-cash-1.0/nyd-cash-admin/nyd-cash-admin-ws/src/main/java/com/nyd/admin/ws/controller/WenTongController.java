package com.nyd.admin.ws.controller;

import com.nyd.admin.model.WenTongExcelVo;
import com.nyd.admin.service.WenTongService;
import com.nyd.admin.service.excel.ExcelKit;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Peng
 * @create 2017-12-18 10:47
 **/
@RestController
@RequestMapping("/admin/wentong")
public class WenTongController {
    final Logger logger = LoggerFactory.getLogger(WenTongController.class);
    @Autowired
    private WenTongService wenTongService;

    @RequestMapping(value = "/exportForWt", method = RequestMethod.GET)
    @ResponseBody
    public void exportForWt(String startDate, String endDate, String name, String mobile, HttpServletResponse response) {
        logger.info("**********"+startDate+":"+endDate+":"+name+":"+mobile);
        List<WenTongExcelVo> wenTongExcelVos = wenTongService.queryWenTongExcelVo(startDate, endDate, name, mobile);
        ExcelKit.$Export(WenTongExcelVo.class, response)
                .toExcel(wenTongExcelVos, "对接稳通用户信息");
    }

    @RequestMapping(value = "/importExcelFromWt", method = RequestMethod.POST)
    public ResponseData importExcelFromWt(@RequestParam("file") CommonsMultipartFile file) {

        ResponseData responseData = wenTongService.importExcel(file);

        return responseData;

    }


}



