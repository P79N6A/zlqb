package com.nyd.application.service;


import com.nyd.application.model.request.IndexModel;
import com.nyd.application.model.request.IndexRequestModel;
import com.nyd.application.model.request.JiGuangDto;
import com.nyd.application.model.request.MessageModel;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 2017/11/28
 */
public interface IndexService {
    ResponseData<IndexModel> getIndexInfo(IndexRequestModel model);

    ResponseData saveMessage(MessageModel model);

    ResponseData getWebInfo();

    ResponseData pushBindClient(JiGuangDto jiGuangDto);
}
