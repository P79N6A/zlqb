package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.UserKzjrDao;
import com.nyd.user.dao.mapper.AccountMapper;
import com.nyd.user.entity.Account;
import com.nyd.user.model.UserCashCoupon;
import com.nyd.user.model.UserKzjrInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.dto.BatchRefundTicketDto;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Dengw on 2017/11/28
 */
@Service(value = "userAccountContract")
public class UserAccountContractImpl implements UserAccountContract {
    private static Logger LOGGER = LoggerFactory.getLogger(UserAccountContractImpl.class);

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserKzjrDao userKzjrDao;
    /**
     * 查询账号来源
     * @param accountNumber
     * @return
     */
    @Override
    public ResponseData<String> getAccountSource(String accountNumber) {
        ResponseData responseData = ResponseData.success();
        try {
            List<Account> list = accountDao.getAccountsByAccountNumber(accountNumber);
            if(list != null && list.size()>0){
                String source = list.get(0).getSource();
                responseData.setData(source);
            }
        } catch (Exception e) {
            LOGGER.error("get accountInfo error! accountNumber = "+accountNumber,e);
            responseData = ResponseData.error();
            return responseData;
        }
        return responseData;
    }

    /**
     * 查询账号信息
     * @param userId
     * @return
     */
    @Override
    public ResponseData<AccountDto> getAccount(String userId) {
        ResponseData responseData = ResponseData.success();
        try {
            List<AccountDto> list = accountDao.getAccountByuserId(userId);
            if (list!=null&&list.size()>0) {
                responseData.setData(list.get(0));
            }
        } catch (Exception e) {
            LOGGER.error("get getAccount error! userId = "+userId,e);
            responseData = ResponseData.error();
            return responseData;
        }
        return responseData;
    }

    @Override
    public ResponseData<AccountDto> getAccountByIbankUserId(String iBankUserId) {
        ResponseData responseData = ResponseData.success();
        try {
            List<AccountDto> list = accountDao.getAccountByIbankUserId(iBankUserId);
            if (list!=null&&list.size()>0) {
                responseData.setData(list.get(0));
            }
        } catch (Exception e) {
            LOGGER.error("get getAccount error! userId = "+iBankUserId,e);
            responseData = ResponseData.error();
            return responseData;
        }
        return responseData;
    }

    /**
     * 根据批量账号查询userId
     * @param accountNumbers
     * @return
     */
    @Override
    public ResponseData<Set<String>> queryAccountByAccountList(List<String> accountNumbers) {
        ResponseData responseData = ResponseData.success();
        try {
            Set<String> list = accountMapper.queryAccountByAccountList(accountNumbers);
            responseData.setData(list);
        } catch (Exception e) {
            LOGGER.error("queryAccountByAccountList has except !",e);
            responseData = ResponseData.error();
            return responseData;
        }
        return responseData;
    }

    /**
     * 根据手机号查询现金券余额
     * @param accountNumber
     * @return
     */
    @Override
    public ResponseData<AccountDto> queryBalance(String accountNumber) {
        ResponseData responseData = ResponseData.success();
        UserCashCoupon userCashCoupon = new UserCashCoupon();
        try {
            List<AccountDto> list = accountDao.queryBalance(accountNumber);
            BigDecimal balance = new BigDecimal(0);
            BigDecimal returnBalance = new BigDecimal(0);
            BigDecimal totalBalance = new BigDecimal(0);
            if (list != null && list.size() > 0) {
                AccountDto accountDto = list.get(0);
                LOGGER.info("现金券余额对象："+ JSON.toJSON(accountDto));
                if (accountDto.getBalance() != null){
                    balance = accountDto.getBalance();
                    LOGGER.info("充值余额:"+balance);
//                    userCashCoupon.setBalance(balance);
                }

                if (accountDto.getReturnBalance() != null){
                    returnBalance = accountDto.getReturnBalance();
                    LOGGER.info("返回余额:"+returnBalance);
//                    userCashCoupon.setReturnBalance(returnBalance);
                }

                /**
                 * 账户余额
                 */
                totalBalance = balance.add(returnBalance);
                userCashCoupon.setTotalBalance(totalBalance);
                LOGGER.info("账户余额:"+totalBalance);
                responseData.setData(userCashCoupon);

            }

        }catch (Exception e){
            LOGGER.error("查询账户余额出错！",e);
            responseData = ResponseData.error();
            return responseData;
        }
        return responseData;
    }

    @Override
    public void updateAccountByAccountNumber(Account account) throws Exception {
        accountDao.updateAccountByAccountNumber(account);

    }

    @Override
    public ResponseData<UserKzjrInfo> getKzjrByYmtUserId(String userId) {
        LOGGER.info("根据银马头的userId获取nyd用户在空中金融开户情况 ymt userId"+userId);
        ResponseData responseData = ResponseData.success();
        try {
            List<AccountDto> accountDtos = accountDao.getAccountByIbankUserId(userId);
            if(null == accountDtos || accountDtos.size()<1){
                LOGGER.warn("银马头用户该用户不存在于侬要贷 userId:"+userId);
                return responseData;
            }
            AccountDto accountDto = accountDtos.get(0);
            String nydUserId = accountDto.getUserId();
            List<UserKzjrInfo> userKzjrInfos = userKzjrDao.getByUserId(nydUserId);
            if(null== userKzjrInfos || userKzjrInfos.size()<1){
                LOGGER.info("nyd用户空中金融未开户用户");
                return responseData;
            }
            responseData.setData(userKzjrInfos.get(0));
        } catch (Exception e) {
            LOGGER.error("查询银马头用户存在于侬要贷中的",e);
            responseData=ResponseData.error("服务器开小差");
        }
        return responseData;
    }

    /**
     * 根据手机号找到对应人的账户信息
     * @param accountNumber
     * @return
     */
    @Override
    public ResponseData<AccountDto> selectByAccountNumber(String accountNumber) {
        ResponseData responseData = ResponseData.success();
        if (!StringUtils.isNotBlank(accountNumber)){
            ResponseData.error("请告知用户的手机号,否则不知道怎么查");
        }
        try {
            List<AccountDto> list =  accountDao.findByAccountNumber(accountNumber);
            LOGGER.info("根据手机号查找对应人信息:"+JSON.toJSON(list.get(0)));
            if (list != null && list.size() > 0){
                responseData.setData(list.get(0));
            }

        }catch (Exception e){
            LOGGER.error("根据手机号查找对应人信息出错",e);
        }
        return responseData;
    }

    /**
     * 更新账户
     * @param account
     * @return
     * @throws Exception
     */
    @Override
    public ResponseData updateAccount(Account account) throws Exception {
        LOGGER.info("更新账户请求参数:"+JSON.toJSON(account));
        ResponseData responseData = ResponseData.success();
        if (!StringUtils.isNotBlank(account.getAccountNumber())){
            ResponseData.error("请告知用户的手机号,否则无法更新");
        }
        try {
            accountDao.updateAccountByAccountNumber(account);
            LOGGER.info("更新账户成功");
        }catch (Exception e){
            LOGGER.error("更新账户失败",e);
            return ResponseData.error("更新账户失败");
        }
        return responseData;
    }


    /**
     * 批量退券更新账户余额
     * @param batchRefundTicketDto
     * @return
     */
    @Override
    public ResponseData BatchRefundTicket(BatchRefundTicketDto batchRefundTicketDto) {
        LOGGER.info("批量退券请求参数:"+JSON.toJSON(batchRefundTicketDto));
        ResponseData response = ResponseData.success();
//        List<String> list = batchRefundTicketDto.getAccountNumbers();
        List<BatchRefundTicketDto.BatchObject> list = batchRefundTicketDto.getAccountNumbers();

        if (batchRefundTicketDto.getReturnTicketAmount().compareTo(new BigDecimal(0)) == -1){
            ResponseData.error("退券金额请大于0元,谢谢");
        }
        if (batchRefundTicketDto.getReturnTicketType() == null){
            ResponseData.error("请明确退券类型,谢谢");
        }
        if (list == null && list.size() == 0){
            ResponseData.error("请先确定要给谁退券,谢谢");
        }

        //返回余额更新失败的集合:
        List<String> errorPhones = new ArrayList<String>();
        try {
//            for (BatchRefundTicketDto.BatchObject accountNumber : list){
            for (int i= 0 ; i < list.size(); i++){

                /**
                 * 表示退小银券,更新账户返回余额
                 */
                if (batchRefundTicketDto.getReturnTicketType() == 1){
                    BatchRefundTicketDto.BatchObject batchObject = list.get(i);
                    String accountNumber = batchObject.getAccountNumber();
                    String premiumId = batchObject.getPremiumId();
                    //1.先查询一下这个的账户信息
                    ResponseData<AccountDto> data = selectByAccountNumber(accountNumber);
                    if ("0".equals(data.getStatus())){

                        //2.更新账户返回余额
                        Account account = new Account();
                        AccountDto accountDto = data.getData();
                        BigDecimal returnBalance = new BigDecimal(0);
                        if (accountDto.getReturnBalance() != null){
                             returnBalance = accountDto.getReturnBalance();
                             LOGGER.info("账户返回余额:"+returnBalance);
                        }

                        BigDecimal returnTicketAmount = batchRefundTicketDto.getReturnTicketAmount();
                        LOGGER.info("发放金额:"+returnTicketAmount);
                        BigDecimal totalReturnBalance = returnBalance.add(returnTicketAmount);
                        LOGGER.info("返回账户总余额:"+totalReturnBalance);
                        account.setReturnBalance(totalReturnBalance);
                        account.setAccountNumber(accountNumber);
                        LOGGER.info("更新对象:"+JSON.toJSON(account));
                        ResponseData resp = updateAccount(account);
                        if ("0".equals(resp.getStatus())){
                            LOGGER.info("手机号:"+accountNumber+"账户返回余额更新成功");
                        }else {
                            errorPhones.add(premiumId);
                        }

                    }

                }

            }
            LOGGER.info("退小银券更新失败的唯一标识集合:"+JSON.toJSON(errorPhones));
            response.setData(errorPhones);

        }catch (Exception e){
            LOGGER.error("批量退券出错啦",e);
        }

        return response;
    }
}
