package com.nyd.user.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nyd.user.model.UserBindCardConfirm;
import com.nyd.user.model.UserBindCardReq;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public interface BindCardService {
	
    ResponseData bindCard(UserBindCardReq req);
    
    ResponseData bindCardConfirm(UserBindCardConfirm req);
    ResponseData bindCardReset(List<UserBindCardConfirm> req);

    JSONObject queryBindCardChannelCode();
    //void userBindUpdate(UserBindCardConfirm req, ResponseData resp);

}
