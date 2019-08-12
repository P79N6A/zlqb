package com.creativearts.nyd.pay.service.yinshengbao.util;

import com.creativearts.nyd.pay.model.yinshengbao.YsbQuickPayConfirmVo;
import com.creativearts.nyd.pay.model.yinshengbao.YsbQuickPayNotifyResponseVo;
import com.creativearts.nyd.pay.model.yinshengbao.YsbSendMessageVo;
import com.tasfe.framework.support.model.ResponseData;

public interface YsbQuickPayService {

    /**
     * 发送短信接口
     * @param vo
     * @return
     */
    ResponseData ysbSendMessage(YsbSendMessageVo vo);


    /**
     * 确认支付接口
     * @param vo
     * @return
     */
    ResponseData ysbQuickPayConfirm(YsbQuickPayConfirmVo vo);


    /**
     *回调通知处理
     * @param ysbQuickPayNotifyResponseVo
     * @return
     */
    String handleCallBack(YsbQuickPayNotifyResponseVo ysbQuickPayNotifyResponseVo);
}
