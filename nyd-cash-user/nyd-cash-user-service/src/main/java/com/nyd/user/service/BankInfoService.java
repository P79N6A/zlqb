package com.nyd.user.service;

import java.util.List;

import com.nyd.user.entity.Bank;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.ChannelBankData;
import com.nyd.user.model.ChannelBankInfo;
import com.tasfe.framework.support.model.ResponseData;

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
    
    ResponseData<List<ChannelBankInfo>> getBankListV2();
    ResponseData<ChannelBankData> getBankChannel(ChannelBankInfo info);
}
