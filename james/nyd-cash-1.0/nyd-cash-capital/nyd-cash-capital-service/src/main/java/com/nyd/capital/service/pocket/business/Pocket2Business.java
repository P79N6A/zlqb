package com.nyd.capital.service.pocket.business;

import com.nyd.capital.model.jx.SubmitJxMsgCode;
import com.nyd.capital.model.pocket.Pocket2CallbackDto;
import com.nyd.capital.model.pocket.Pocket2JobDto;
import com.nyd.capital.model.pocket.PocketAccountDetailDto;
import com.nyd.capital.model.pocket.PocketSendCodeDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author liuqiu
 */
public interface Pocket2Business {

    /**
     * 获取江西银行开户页面
     *
     * @param userId
     * @return
     */
    ResponseData<String> getHtml(String userId,String mobile);

    /**
     * 获取江西银行开户验证码
     *
     * @param request
     * @return
     */
    ResponseData getJxBankCode(PocketSendCodeDto request);

    /**
     * 提交江西银行开户页面
     *
     * @param request
     * @return
     */
    ResponseData submitJxMsgCode(SubmitJxMsgCode request);

    /**
     * 查询用户口袋理财开户情况
     *
     * @param dto
     * @param ifAgain
     * @return
     */
    ResponseData selectUserOpenDetail(PocketAccountDetailDto dto, boolean ifAgain);

    /**
     * 口袋理财回调处理
     *
     * @param dto
     * @return
     */
    String pocketCallback(Pocket2CallbackDto dto);

    /**
     * 跑批
     * @param dto
     * @return
     */
    String pocketJob(Pocket2JobDto dto);
}
