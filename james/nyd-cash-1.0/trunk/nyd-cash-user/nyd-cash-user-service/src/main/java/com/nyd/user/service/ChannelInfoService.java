package com.nyd.user.service;

import com.nyd.user.model.ChannelInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by zhujx on 2017/12/5.
 */
public interface ChannelInfoService {

    ResponseData<ChannelInfo> getChannelInfoByUrl(String url);

}
