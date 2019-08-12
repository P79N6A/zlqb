package com.nyd.capital.service.jx.business;

import com.nyd.capital.model.jx.*;
import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.support.model.ResponseData;
import org.openqa.selenium.WebDriver;

/**
 * @author liuqiu
 */
public interface JxBusiness {

    /**
     * 打开中网国投的页面
     * @param request
     * dr
     * @return
     */
    ResponseData openJxHtml(OpenJxHtmlRequest request);

    /**
     * 获取江西银行开户短信验证码
     *  @param request
     * @return
     */
    ResponseData submitJxMsgCode(SubmitJxMsgCode request);

    /**
     * 五合一开户回调
     * @param kzjrCode
     * @return
     */
    ResponseData jxReturnForHtml(String kzjrCode);


    /**
     * 提现通知
     * @param returnForWithdrawRequest
     * @return
     */
    ResponseData jxReturnForWithdraw(ReturnForWithdrawRequest returnForWithdrawRequest);

    /**
     * 用于修复数据
     * @param returnForWithdrawRequest
     * @return
     */
    ResponseData onlyForWithdraw(ReturnForWithdrawRequest returnForWithdrawRequest);

    /**
     * 查询用户在jx的账户情况
     * @param message
     * @return
     */
    ResponseData selectJxAccount(OrderMessage message) throws Exception;
}
