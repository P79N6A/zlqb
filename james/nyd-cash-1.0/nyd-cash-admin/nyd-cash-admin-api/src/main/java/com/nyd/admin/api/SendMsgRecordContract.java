package com.nyd.admin.api;

import com.nyd.admin.model.Vo.MsgSendResultVo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.Date;

/**
 * @author cm
 *
 */
public interface SendMsgRecordContract {

    ResponseData<MsgSendResultVo> sendMsg();

    /**
     * 发送注册未登录短信
     * @param nowDate
     * @return
     */
    ResponseData<MsgSendResultVo> sendUnloginMsg(Date nowDate);
}
