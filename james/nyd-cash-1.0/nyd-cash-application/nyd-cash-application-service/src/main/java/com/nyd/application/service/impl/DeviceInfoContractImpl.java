package com.nyd.application.service.impl;

import com.nyd.application.api.DeviceInfoContract;
import com.nyd.application.model.mongo.AddressBook;
import com.nyd.application.model.mongo.DeviceInfo;
import com.nyd.application.model.request.AttachmentModel;
import com.nyd.application.service.util.MongoApi;
import com.nyd.application.service.util.QiniuApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shaoqing.liu
 * @date 2018/7/11 15:57
 */
@Service(value = "deviceInfoContractNyd")
public class DeviceInfoContractImpl implements DeviceInfoContract{
    @Autowired
    private MongoApi mongoApi;
    @Autowired
    private QiniuApi qiniuApi;


    @Override
    public String getAttachmentModelUrl(String userId, String type) {
        AttachmentModel attachmentModel = mongoApi.getAttachmentModel(userId,type);
        if(attachmentModel != null){
            String url =   qiniuApi.download(attachmentModel.getFile());
            return url;
        }
        return null;
    }


}
