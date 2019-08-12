package com.nyd.user.service.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.activity.api.CashCouponContract;
import com.nyd.activity.model.CashCouponLogInfo;
import com.nyd.activity.model.vo.CashCouponInfoVo;
import com.nyd.application.api.AgreeMentContract;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.UserDao;
import com.nyd.user.entity.Account;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.mq.RechargeFeeInfo;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * 充值现金券完成，后续处理逻辑
 */
public class RechargeFeeMqProcesser implements RabbitmqMessageProcesser<RechargeFeeInfo> {
    private static Logger logger = LoggerFactory.getLogger(RechargeFeeMqProcesser.class);

    @Autowired
    private CashCouponContract cashCouponContract;
    @Autowired(required = false)
    private AgreeMentContract agreeMentContract;

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private UserDao userDao;

    /**
     * 此消费者只处理充值现金券的逻辑
     *
     * @param message
     */
    @Override
    public void processMessage(RechargeFeeInfo message) {
        logger.info("RechargeFeeMqProcesser 进入充值mq处理方法 start param is：" + JSON.toJSONString(message));

        try {

            if (message != null) {
                ResponseData<CashCouponInfoVo> cashCouponData = cashCouponContract.getById(Long.valueOf(message.getCashId()));
                logger.info("获取优惠券配置信息：" + JSON.toJSON(cashCouponData));
                CashCouponLogInfo cashCouponLogInfo = new CashCouponLogInfo();
                if (cashCouponData != null && cashCouponData.getStatus().equals("0")) {
                    CashCouponInfoVo cashCouponInfoVo = cashCouponData.getData();
                    String cashNo = "";
                    try {
                        List<Account> accountList = accountDao.getAccountsByAccountNumber(message.getAccountNumber());
                        cashNo = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8) + message.getUserId(); //充值现金券的流水号：
                        cashCouponLogInfo.setCashNo(cashNo);                                                 //现金券流水号
                        cashCouponLogInfo.setCashId(cashCouponInfoVo.getId().intValue());                    //现金券id
                        Account account = new Account();
                        if (accountList != null && accountList.size() > 0) {
                            account = accountList.get(0);
                        }
                        cashCouponLogInfo.setUserId(message.getUserId());                                    //用户id
                        cashCouponLogInfo.setAccoutNumber(message.getAccountNumber());                       //手机号
                        cashCouponLogInfo.setCashFee(new BigDecimal(cashCouponInfoVo.getCashFeeType()));     //现金券金额（现金券面额）
                        cashCouponLogInfo.setRechargeFee(message.getAmount());                               //交易金额(实际支付金额)
                        cashCouponLogInfo.setRechargeFlowNo(message.getRechargeFlowNo());                    //支付流水号
                        cashCouponLogInfo.setUseType(message.getUserType());                                 //使用类型
                        cashCouponLogInfo.setOperStatus(message.getOperStatus());                            //操作是否成功 0：失败 1：成功
                        cashCouponLogInfo.setCashDescription(message.getCashDescription());                  //现金券描述
                        cashCouponLogInfo.setAppName(message.getAppName());
                        ResponseData saveResponse = cashCouponContract.saveCashCouponLog(cashCouponLogInfo);
                        if (saveResponse.getStatus() != null && saveResponse.getStatus().equals("0")) {
                            logger.info("mq 保存支付现金券记录成功 ：" + JSON.toJSONString(cashCouponLogInfo));
                        } else {
                            logger.error("mq 保存支付现金券记录失败 ：" + JSON.toJSONString(cashCouponLogInfo));
                        }

                        /**
                         * 如果RechargeFeeInfo对象中的operStatus为1,表示成功,则需要更新账户现金券余额，
                         */
                        if (message.getOperStatus() == 1) {               //表示现金券充值成功,需要更新账户余额

                            if (account.getBalance() != null) {                              //表示用户账户充值余额不为null，在原来余额上进行增加
                                account.setBalance(account.getBalance().add(cashCouponLogInfo.getCashFee()));
                            } else {                                                          //表示用户账户充值余额为null
                                account.setBalance(cashCouponLogInfo.getCashFee());
                            }
                            accountDao.updateAccountByAccountNumber(account);
                            logger.info("手机号为：" + message.getAccountNumber() + "的用户账户充值余额更新成功 ：" + JSON.toJSONString(account));
                        }
                        //充值成功签署充值协议
                        String userName = "";
                        try {
                            List<UserInfo> list = userDao.getUsersByUserId(message.getUserId());
                            if (list != null && list.size() > 0) {
                                UserInfo userInfo = list.get(0);
                                if (userInfo != null) {
                                    userName = userInfo.getRealName();
                                }
                            }
                        } catch (Exception e) {
                            logger.error("getUsersByUserId has exception! userId is " + account.getUserId(), e);
                        }
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("cashNo", cashNo);
                        jsonObject.put("userName", userName);
                        jsonObject.put("accountNumber",message.getAccountNumber());
                        try {
                            agreeMentContract.signRechargeAgreeMent(jsonObject);
                        } catch (Exception e) {
                            logger.error("signRechargeAgreeMent has exception! cashNo is " + cashNo, e);
                        }
                    } catch (Exception e) {
                        logger.error("mq RechargeFeeMqProcesser 处理充值任务失败", e);
                    }
                }
            } else {
                logger.error("message is null!");
            }
        } catch (Exception e) {
            logger.error("mq RechargeFeeMqProcesser has exception", e);
        }
    }


}
