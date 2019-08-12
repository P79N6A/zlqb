package com.nyd.application.ws.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.application.model.request.QiNiuModel;
import com.nyd.application.service.util.QiniuApi;
import com.tasfe.framework.support.model.ResponseData;

@RestController
@RequestMapping("/application")
public class QiNiuController {
	
	@Autowired
    public QiniuApi qiniuApi;
	
	/**
     * 新增七牛获取uploadToken接口
     * @param qiNiuModel
     * @return
     */
    @RequestMapping(value = "/auth/qiNiuToken", method = RequestMethod.POST, produces = "application/json")
    public ResponseData qiNiuToken(@RequestBody QiNiuModel qiNiuModel) throws Exception{
        ResponseData responseData = ResponseData.success();
        if (StringUtils.isBlank(qiNiuModel.getBucket()) || qiNiuModel.getKeys() == null || qiNiuModel.getKeys().size() == 0) {
            responseData = ResponseData.error("参数错误！");
            return responseData;
        }
        responseData.setData(qiniuApi.uploadByApp(qiNiuModel.getBucket(), qiNiuModel.getKeys()));
        return responseData;
    }

    @RequestMapping(value = "/qiNiuToken/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData qiNiuTokenNew(@RequestBody QiNiuModel qiNiuModel) throws Exception{
        ResponseData responseData = ResponseData.success();
        if (StringUtils.isBlank(qiNiuModel.getBucket()) || qiNiuModel.getKeys() == null || qiNiuModel.getKeys().size() == 0) {
            responseData = ResponseData.error("参数错误！");
            return responseData;
        }
        responseData.setData(qiniuApi.uploadByApp(qiNiuModel.getBucket(), qiNiuModel.getKeys()));
        return responseData;
    }
}
