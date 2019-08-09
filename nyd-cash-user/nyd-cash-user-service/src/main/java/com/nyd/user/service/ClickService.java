package com.nyd.user.service;

import com.nyd.user.model.ClickInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by zhujx on 2017/12/12.
 */
public interface ClickService {

    ResponseData saveClickInfo(ClickInfo clickInfo);
}
