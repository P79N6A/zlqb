package com.nyd.user.service;

import com.nyd.user.entity.Bank;
import com.nyd.user.model.BankInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by Dengw on 17/11/3.
 */
public interface BankInfoService {
    ResponseData saveBankInfo(BankInfo bankInfo);
    ResponseData saveBankInfoNoJudge(BankInfo bankInfo);
    
    void updateBank(Bank bank);

    ResponseData<List<BankInfo>> getBankInfos(String userId);
    ResponseData<List<BankInfo>> getBankInfosByBankAccout(String bankAccout);
    void saveBank(Bank bank);
    ResponseData getBankList(BankInfo bankInfo);
    ResponseData getXunlianBankList(BankInfo bankInfo);
}
