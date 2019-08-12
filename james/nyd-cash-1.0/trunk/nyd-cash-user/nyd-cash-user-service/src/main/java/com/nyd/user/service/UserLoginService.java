package com.nyd.user.service;

import com.nyd.user.model.AccountInfo;
import com.nyd.user.model.GeeTestDto;
import com.nyd.user.model.SmsInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 17/11/3.
 */
public interface UserLoginService {
	ResponseData sendMsgCode(SmsInfo smsRequest);

	ResponseData login(AccountInfo accountInfo,String ip);

	ResponseData logout(String accountNumber);

	ResponseData register(AccountInfo accountInfo);

	ResponseData query(AccountInfo accountInfo);

	ResponseData channelRegister(AccountInfo accountInfo);

	ResponseData modifyPassword(AccountInfo accountInfo);

	ResponseData forgetPassword(AccountInfo accountInfo);

	boolean judgeTimeout(String accountNumber,String deviceId);

	ResponseData messageRegisterOrLogin(AccountInfo accountInfo, String ip);

	ResponseData passwordSet(AccountInfo accountInfo,String ip);

    /**
     * 极验预处理
     * @param dto
     * @return
     */
    ResponseData geeTestPrepare(GeeTestDto dto);

    /**
     * 极验二次验证
     * @param dto
     * @return
     */
    ResponseData geeTestSecond(GeeTestDto dto);


	/**
	 * 极验网关验证
	 * @param dto
	 * @return
	 */
	ResponseData geeTestOnePass(GeeTestDto dto);
}
