package com.nyd.user.dao;

import com.nyd.user.entity.Bank;
import com.nyd.user.model.BankInfo;

import java.util.List;

/**
 * Created by Dengw on 17/11/1.
 */
public interface BankDao {
    void save(BankInfo bankInfo) throws Exception;
    
    void updateBankByBankAccount(Bank bank) throws Exception;

    List<BankInfo> getBanksByUserId(String userId) throws Exception;

    List<BankInfo> getBanksByBankAccount(String bankAccount) throws Exception;

    void saveBank(Bank bank) throws Exception;

	List<BankInfo> getBanksByUserIdAndSource(String userId, Integer source)
			throws Exception;
}
