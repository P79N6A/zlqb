package com.nyd.application.api;

import com.nyd.application.model.request.AttachmentModel;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 2017/11/16
 */
public interface QiniuContract {
    ResponseData base64Upload(String file);

    ResponseData base64Upload(AttachmentModel attachmentModel);

    ResponseData download(String fileName);

    void saveFilePDFInfo(String userId) throws Exception;

    ResponseData downloadFilePDFInfo(String userId);

}
