package com.nyd.user.api;

import com.nyd.user.entity.Account;
import com.nyd.user.model.UserKzjrInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.dto.BatchRefundTicketDto;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;
import java.util.Set;

/**
 * Created by Dengw on 2017/11/28
 */
public interface UserAccountContract {
    ResponseData<String> getAccountSource(String accountNumber);

    ResponseData<AccountDto>  getAccount(String userId);

    ResponseData<AccountDto> getAccountByIbankUserId(String iBankUserId);

    /**
     * 根据手机号列表查询userId
     * @param accountNumbers
     * @return
     */
    ResponseData<Set<String>> queryAccountByAccountList(List<String> accountNumbers);


    /**
     * 根据手机号查询现金券余额
     * @param accountNumber
     * @return
     */
    ResponseData<AccountDto> queryBalance(String accountNumber);

    /**
     * 更新账户总余额
     */
    void updateAccountByAccountNumber(Account account) throws Exception;

    /**
     * 根据用户id获取用户在空中金融开户信息
     * @param userId ymt的用户userId
     * @return
     */
    ResponseData<UserKzjrInfo> getKzjrByYmtUserId(String userId);


    /**
     * 根据手机号找到对应人的账户信息
     * @param accountNumber
     * @return
     */
    ResponseData<AccountDto> selectByAccountNumber(String accountNumber);


    /**
     * 更新账户
     * @param account
     * @return
     * @throws Exception
     */
    ResponseData updateAccount(Account account) throws Exception;

    /**
     * 侬要贷批量退券,更新账户余额
     * @param batchRefundTicketDto
     * @return
     */
    ResponseData BatchRefundTicket(BatchRefundTicketDto batchRefundTicketDto);


}
