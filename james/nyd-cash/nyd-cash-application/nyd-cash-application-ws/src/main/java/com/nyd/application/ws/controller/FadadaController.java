package com.nyd.application.ws.controller;

import com.alibaba.fastjson.JSONObject;
import com.nyd.application.model.request.ExTSignAutoModel;
import com.nyd.application.model.request.FadadaNotifyModel;
import com.nyd.application.model.request.GenerateContractModel;
import com.nyd.application.model.request.MuBanRequestModel;
import com.nyd.application.service.FadadaService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by hwei on 2017/11/20
 */
@RestController
@RequestMapping("/application/fadada")
public class FadadaController {
    private static Logger LOGGER = LoggerFactory.getLogger(FadadaController.class);

    @Autowired
    private FadadaService fadadaService;

    @RequestMapping(value = "/uploadPdfTemplate", method = RequestMethod.POST, produces = "application/json")
    public ResponseData uploadPdfTemplate(@RequestBody MuBanRequestModel muBanRequestModel){
        try {
            return fadadaService.uploadPdfTemplate(muBanRequestModel);
        } catch (Exception e) {
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/generateContract", method = RequestMethod.POST, produces = "application/json")
    public ResponseData generateContract(@RequestBody GenerateContractModel generateContractModel){
        try {
            return fadadaService.generateContract(generateContractModel);
        } catch (Exception e) {
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/extSignAuto", method = RequestMethod.POST, produces = "application/json")
    public ResponseData extSignAuto(@RequestBody ExTSignAutoModel exTSignAutoModel){
        try {
            return fadadaService.extSignAuto(exTSignAutoModel);
        } catch (Exception e) {
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/fadadaNotify", method = RequestMethod.POST, produces = "application/x-www-form-urlencoded")
    public void fadadaNotify(FadadaNotifyModel fadadaNotifyModel, ServletRequest request, ServletResponse response){
        try {
            LOGGER.info("FadadaController fadadaNotify request is "+ JSONObject.toJSONString(fadadaNotifyModel));
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            fadadaService.fadadaNotify(fadadaNotifyModel);
            writeMessage(httpServletResponse,200,"success");
        } catch (Exception e) {
            LOGGER.error("fadadaNotify has except !",e);
        }
    }

    @RequestMapping(value = "/viewContract", method = RequestMethod.GET, produces = "application/json")
    public ResponseData viewContract(@RequestParam String contractId){
        try {
            return fadadaService.viewContract(contractId);
        } catch (Exception e) {
            return ResponseData.error();
        }
    }


    private void writeMessage(HttpServletResponse response, int status, String content) {
        response.setStatus(status);
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.write(content);
            printWriter.flush();
            printWriter.close();
        } catch (IOException ignored) {
        }
    }

}
