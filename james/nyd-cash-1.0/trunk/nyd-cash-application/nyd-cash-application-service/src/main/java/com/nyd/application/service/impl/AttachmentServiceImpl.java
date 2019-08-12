package com.nyd.application.service.impl;

import com.nyd.application.model.enums.AttachmentType;
import com.nyd.application.model.request.AttachmentModel;
import com.nyd.application.model.request.LivenessModel;
import com.nyd.application.service.AttachmentService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Dengw on 2017/11/27
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {
    private static Logger LOGGER = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    @Autowired
    QiniuContractImpl qiniuContractImpl;

    @Override
    public ResponseData saveLivenessInfo(LivenessModel livenessModel) {
        ResponseData responseData = ResponseData.success();
        try {
            LOGGER.info("begin to save liveness photo,userId is "+livenessModel.getUserId());
            //保存活体识别最好照片
            if (StringUtils.isNotBlank(livenessModel.getBestImage())) {
                AttachmentModel bestImage = new AttachmentModel();
                bestImage.setUserId(livenessModel.getUserId());
                bestImage.setType(AttachmentType.BESTIMAGE.getType());
                bestImage.setFileName(AttachmentType.BESTIMAGE.getDescription());
                bestImage.setFile(livenessModel.getBestImage());
                qiniuContractImpl.base64Upload(bestImage);
            }
            //保存活体识别照片
            if (StringUtils.isNotBlank(livenessModel.getImageEnv())) {
                AttachmentModel imageEnv = new AttachmentModel();
                imageEnv.setUserId(livenessModel.getUserId());
                imageEnv.setType(AttachmentType.IMAGEENV.getType());
                imageEnv.setFileName(AttachmentType.IMAGEENV.getDescription());
                imageEnv.setFile(livenessModel.getImageEnv());
                qiniuContractImpl.base64Upload(imageEnv);
            }
            //保存身份证抠图
            if (StringUtils.isNotBlank(livenessModel.getImageRef1())) {
                AttachmentModel imageRef1 = new AttachmentModel();
                imageRef1.setUserId(livenessModel.getUserId());
                imageRef1.setType(AttachmentType.IMAGEREF1.getType());
                imageRef1.setFileName(AttachmentType.IMAGEREF1.getDescription());
                imageRef1.setFile(livenessModel.getImageRef1());
                qiniuContractImpl.base64Upload(imageRef1);
            }
            LOGGER.info("save liveness photo success !");
        } catch (Exception e) {
            LOGGER.error("save liveness photo failed ! userId = "+livenessModel.getUserId(),e);
        }
        return responseData;
    }

    @Override
    public ResponseData downloadFile(String fileName) {
        ResponseData responseData = ResponseData.success();
        try{
            responseData = qiniuContractImpl.download(fileName);
        } catch (Exception e) {
            LOGGER.error("download file failed ! fileName = "+fileName,e);
        }
        return responseData;
    }

}
