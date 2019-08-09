package com.nyd.application.ws.controller;

import com.nyd.application.model.request.LivenessModel;
import com.nyd.application.service.AttachmentService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Dengw on 2017/11/27
 * 保存图片、附件
 */
@RestController
@RequestMapping("/application")
public class AttachmentController {
    @Autowired
    AttachmentService attachmentService;

    @RequestMapping(value = "/liveness", method = RequestMethod.POST, produces = "application/json")
    public ResponseData saveLiveness (@RequestBody LivenessModel livenessModel) {
        ResponseData responseData = attachmentService.saveLivenessInfo(livenessModel);
        return responseData;
    }

    @RequestMapping(value = "/download/file", method = RequestMethod.POST, produces = "application/json")
    public ResponseData downloadFile (@RequestBody Map<String, String> map) {
        ResponseData responseData = ResponseData.success();
        String fileName = map.get("fileName");
        if(fileName == null){
            return responseData;
        }
        return attachmentService.downloadFile(fileName);
    }
}
