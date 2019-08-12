package com.nyd.user.api;

import com.nyd.user.model.BankInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;


/**
 * Created by Dengw on 2017/11/13
 */
public interface UserBankContract {
    ResponseData<List<BankInfo>> getBankInfos(String userId);
    ResponseData<List<BankInfo>> getBankInfosByBankAccout(String bankAccout);
    
    ResponseData saveBankInfoNoJudge(BankInfo bankInfo);
    ResponseData getBankList(BankInfo bankInfo);
	ResponseData<List<BankInfo>> getBankInfosChang(String userId, Integer source);

}
