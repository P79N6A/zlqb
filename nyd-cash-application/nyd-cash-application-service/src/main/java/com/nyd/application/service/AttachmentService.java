package com.nyd.application.service;

import com.nyd.application.model.request.LivenessModel;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 2017/11/27
 */
public interface AttachmentService {
    ResponseData saveLivenessInfo(LivenessModel livenessModel);

    ResponseData downloadFile(String fileName);
}
