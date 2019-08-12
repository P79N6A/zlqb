
package com.nyd.member.service.rabbit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.activity.api.CouponContract;
import com.nyd.application.api.AgreeMentContract;
import com.nyd.member.api.MemberByCashCouponContract;
import com.nyd.member.api.MemberContract;
import com.nyd.member.api.MemberLogContract;
import com.nyd.member.entity.MemberByCashCoupon;
import com.nyd.member.entity.MemberLog;
import com.nyd.member.model.MemberByCashCouponModel;
import com.nyd.member.model.MemberModel;
import com.nyd.member.model.msg.MemberFeeLogMessage;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.entity.Account;
import com.nyd.user.model.dto.AccountDto;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Random;


/**
 * Created by hwei on 2018/01/08
 */

public class MFeeLogMqProcesser implements RabbitmqMessageProcesser<MemberFeeLogMessage> {
    private static Logger LOGGER = LoggerFactory.getLogger(MFeeLogMqProcesser.class);

    @Autowired
    MemberContract memberContract;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private AgreeMentContract agreeMentContract;

    @Autowired
    private CouponContract couponContract;

    @Autowired
    private UserAccountContract userAccountContract;

    @Autowired
    private MemberLogContract memberLogContract;

    @Autowired
    private MemberByCashCouponContract memberByCashCouponContract;

    @Override
    public void processMessage(MemberFeeLogMessage message) {
        if (message != null) {
            LOGGER.info("receive msg from MFeeLog, msg is " + message.toString());
            MemberModel memberModel = new MemberModel();
            BeanUtils.copyProperties(message,memberModel);
            String  memberId = message.getMemberId();
            if (StringUtils.isBlank(memberId)) {
                try {
                    memberId = idGenerator.generatorId(BizCode.MEMBER).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("memberId生成失败"+message.getUserId());
                    Random random = new Random();
                    memberId= random.nextInt()+"";
                }
            }
            memberModel.setMemberId(memberId);
            LOGGER.info(message.getUserId()+"生成的memberId为"+memberId);
            try {
                LOGGER.info("保存用户对应会员信息，所需要的对象："+JSON.toJSON(memberModel));
                memberContract.saveMember(memberModel);

                //已经使用的优惠券，使其失效（通过优惠券id和 手机号）
                if(StringUtils.isNotBlank(message.getCouponId())){
                    LOGGER.info("手机号为："+message.getMobile()+"优惠券id："+message.getCouponId());
                    com.nyd.activity.model.BaseInfo baseInfo = new com.nyd.activity.model.BaseInfo();
                    baseInfo.setAccountNumber(memberModel.getMobile());
                    baseInfo.setCouponId(memberModel.getCouponId());
                    LOGGER.info("优惠券失效的对象为："+ JSON.toJSON(baseInfo));
                    couponContract.updateCouponLog(baseInfo);
                }

            } catch (Exception e) {
                LOGGER.error("save member has exception!",e);
            }
            //发送签署会员协议（只有会员费付费成功的才去签署会员协议）
            if (StringUtils.isNotBlank(message.getUserId())&&"1".equals(message.getDebitFlag())) {
                try {
                    ResponseData responseData = agreeMentContract.signMemberAgreeMent(message.getUserId());
                    if (responseData!=null&&"0".equals(responseData.getStatus())) {
                        LOGGER.info("会员协议签署成功，userId is "+message.getUserId());
                    }
                } catch (Exception e) {
                    LOGGER.error("会员协议签署错误，userId is "+message.getUserId(),e);
                }
            }
            //签署代扣协议
            if(StringUtils.isNotBlank(message.getUserId())&&"2".equals(message.getDebitFlag())) {
                try {
                    ResponseData responseData = agreeMentContract.signMemberAgreeMent(message.getUserId());
                    if (responseData!=null&&"0".equals(responseData.getStatus())) {
                        LOGGER.info("会员协议签署成功，userId is "+message.getUserId());
                    }
                } catch (Exception e) {
                    LOGGER.error("会员协议签署错误，userId is "+message.getUserId(),e);
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("memberId",memberId);
                jsonObject.put("accountNumber",message.getMobile());
                jsonObject.put("userId",message.getUserId());
                try {
                    ResponseData daikouRes = agreeMentContract.signDaiKouAgreeMent(jsonObject);
                    LOGGER.info("签署代扣协议成功，userId is "+message.getUserId());
                } catch (Exception e) {
                    LOGGER.error("代扣协议签署错误，userId is "+message.getUserId(),e);
                }
            }

            /**
             *更改账户现金券余额
             */
            if (message.getCouponUseFee() != null  && StringUtils.isNotBlank(message.getUserId()) && "1".equals(message.getDebitFlag())){
                try {
                    Account account = new Account();
                    MemberByCashCouponModel memberByCashCouponModel = new MemberByCashCouponModel();
                    BeanUtils.copyProperties(message,memberByCashCouponModel);
//                    if (message.getPayCash() != null){
//                        memberByCashCouponModel.setPayCash(message.getPayCash());
//                    }
                    /*if (message.getPayCash().compareTo(new BigDecimal(0)) == 1){
                        memberByCashCouponModel.setPayCash(message.getPayCash());
                    }*/
                    memberByCashCouponModel.setStatus(0);                                     //操作是否成功
//                    memberByCashCouponModel.setUserId(message.getUserId());                   //用户id
//                    memberByCashCouponModel.setCouponUseFee(message.getCouponUseFee());       //表示总共用了多少金额的现金券
                    memberByCashCouponModel.setMemberId(memberId);                            //会员id
                    ResponseData<AccountDto> data = userAccountContract.getAccount(message.getUserId());
                    AccountDto dto = data.getData();
                    LOGGER.info("获取到的用户对象AccountDto："+JSON.toJSON(dto));
                    if (dto != null){
                        BeanUtils.copyProperties(dto,account);
                    }
                    LOGGER.info("获取到的用户对象account："+JSON.toJSON(account));


                    /**
                     * 我们优先扣返回余额，再扣账户充值余额
                     */
                    //账户返回余额
                    BigDecimal returnBalance = account.getReturnBalance();
                    LOGGER.info("账户返回余额"+returnBalance);

                    //账户充值余额
                    BigDecimal balance = account.getBalance();
                    LOGGER.info("账户充值余额"+balance);

                    //现金券使用金额
                    BigDecimal couponUseFee = message.getCouponUseFee();
                    LOGGER.info("现金券使用金额:"+couponUseFee);

                    if (returnBalance.compareTo(couponUseFee) == 1 || returnBalance.compareTo(couponUseFee) == 0){   //表示账户返回余额大于现金券使用金额，只要更新账户返回余额
                        LOGGER.info("账户返回余额够支付：++++++++++++++++++++++++++++++");
                        BigDecimal resultFee = account.getReturnBalance().subtract(message.getCouponUseFee());
                        LOGGER.info("账户返回余额大于现金券使用金额,账户返回余额剩下的金额："+resultFee);
                        account.setReturnBalance(resultFee);

                        memberByCashCouponModel.setReturnBalanceUse(message.getCouponUseFee());   //表示用了多少金额的账户返回的现金券支付评估费
                        memberByCashCouponModel.setBalanceUse(new BigDecimal(0));


                    }else if (returnBalance.compareTo(couponUseFee) == -1 && returnBalance.compareTo(new BigDecimal(0)) == 1 && balance.compareTo(new BigDecimal(0)) == 1){    //表示账户返回余额有钱，但是不足，先扣完账户返回余额；再扣现金券充值余额
                        LOGGER.info("两个账户都有钱：++++++++++++++++++++++++++++++");
                        memberByCashCouponModel.setReturnBalanceUse(account.getReturnBalance());  //表示用了多少金额的账户返回的现金券支付评估费

                        //减去返回余额的钱，但是还是不足，从充值账户里面进行扣去
                        BigDecimal a = message.getCouponUseFee().subtract(account.getReturnBalance());
                        LOGGER.info("账户返回余额有钱，但是不足,还要支付的现金券金额:"+a);

//                        BigDecimal b = account.getBalance().subtract(a);
//                        LOGGER.info("从账户充值余额里面扣去，剩下的现金券金额："+b);

                        if (account.getBalance().compareTo(a)  == 1  || account.getBalance().compareTo(a)  == 0){    //表示充值账户的现金券金额够进行支付
                            LOGGER.info("两个账户都有钱，加起来够支付：++++++++++++++++++++++++++++++");
                            account.setReturnBalance(new BigDecimal(0));        //返回余额现金券金额变为0
                            account.setBalance(account.getBalance().subtract(a));   //充值余额还剩下的

                            memberByCashCouponModel.setBalanceUse(a);                         //表示用了多少金额的充值余额的现金券支付评估费


                        }else {             //表示充值账户的现金券金额不够进行支付，还需要另付现金
                            LOGGER.info("两个账户都有钱，加起来不够支付：++++++++++++++++++++++++++++++");
                            account.setReturnBalance(new BigDecimal(0));    //返回余额现金券金额变为0
                            account.setBalance(new BigDecimal(0));          //充值账户余额变为0
                            memberByCashCouponModel.setBalanceUse(account.getBalance());     //表示用了多少金额的充值余额的现金券支付评估费
                        }


                    }else if (returnBalance.compareTo(couponUseFee) == -1 && account.getReturnBalance().compareTo(new BigDecimal(0)) == 1 && account.getBalance().compareTo(new BigDecimal(0)) == 0){
                        LOGGER.info("返回余额账户有钱，但不够支付，充值账户没钱：++++++++++++++++++++++++++++++");
                        memberByCashCouponModel.setReturnBalanceUse(account.getReturnBalance());  //表示用了多少金额的账户返回余额的现金券支付评估费
                        account.setReturnBalance(new BigDecimal(0));
                        account.setBalance(new BigDecimal(0));
                        memberByCashCouponModel.setBalanceUse(new BigDecimal(0));

                    }else if (account.getBalance().compareTo(couponUseFee) == -1 && account.getBalance().compareTo(new BigDecimal(0)) == 1 && account.getReturnBalance().compareTo(new BigDecimal(0)) == 0){
                        LOGGER.info("充值账户有钱，但不够支付，余额账户没钱：++++++++++++++++++++++++++++++");
                        memberByCashCouponModel.setReturnBalanceUse(new BigDecimal(0));  //表示用了多少金额的账户返回余额的现金券支付评估费
                        memberByCashCouponModel.setBalanceUse(account.getBalance());
                        account.setReturnBalance(new BigDecimal(0));
                        account.setBalance(new BigDecimal(0));

                    }else if (account.getBalance().compareTo(couponUseFee) == 1 || account.getBalance().compareTo(couponUseFee) == 0 && account.getReturnBalance().compareTo(new BigDecimal(0)) == 0){
                        LOGGER.info("充值账户有钱，够支付，余额账户没钱：++++++++++++++++++++++++++++++");
                        memberByCashCouponModel.setReturnBalanceUse(new BigDecimal(0));  //表示用了多少金额的账户返回余额的现金券支付评估费
                        memberByCashCouponModel.setBalanceUse(message.getCouponUseFee());
                        account.setReturnBalance(new BigDecimal(0));
                        account.setBalance(account.getBalance().subtract(message.getCouponUseFee()));

                    }

                    LOGGER.info("要更新的账户对象："+JSON.toJSON(account));
                    userAccountContract.updateAccountByAccountNumber(account);
                    LOGGER.info("手机号为："+message.getMobile()+"的用户账户余额更新成功");

                    LOGGER.info("要保存现金券使用明细对象："+JSON.toJSON(memberByCashCouponModel));
                    memberByCashCouponContract.saveCashCouponUserDetail(memberByCashCouponModel);
                    LOGGER.info("memberId为："+memberId+"现金券使用详情保存成功");

                }catch (Exception e){
                    LOGGER.info("手机号为："+message.getMobile()+"的用户账户余额更新失败",e);

                }
            }else if (message.getCouponUseFee() != null  && StringUtils.isNotBlank(message.getUserId()) && !"1".equals(message.getDebitFlag())){
                /**
                 * 保存失败的记录
                 */
                try {
                    Account account = new Account();
                    MemberByCashCouponModel memberByCashCouponModel = new MemberByCashCouponModel();
//                    if (message.getPayCash() != null){
//                        memberByCashCouponModel.setPayCash(message.getPayCash());
//                    }
//                    memberByCashCouponModel.setUserId(message.getUserId());                   //用户id
//                    memberByCashCouponModel.setCouponUseFee(message.getCouponUseFee());       //表示总共用了多少金额的现金券
                    memberByCashCouponModel.setMemberId(memberId);                            //会员id
                    BeanUtils.copyProperties(message,memberByCashCouponModel);
                    memberByCashCouponModel.setStatus(1);
                    ResponseData<AccountDto> data = userAccountContract.getAccount(message.getUserId());
                    AccountDto dto = data.getData();
                    LOGGER.info("获取到的用户对象AccountDto："+JSON.toJSON(dto));
                    if (dto != null){
                        BeanUtils.copyProperties(dto,account);
                    }
                    LOGGER.info("获取到的用户对象account："+JSON.toJSON(account));


                    /**
                     * 我们优先扣返回余额，再扣账户充值余额
                     */
                    //账户返回余额
                    BigDecimal returnBalance = account.getReturnBalance();
                    LOGGER.info("账户返回余额"+returnBalance);

                    //账户充值余额
                    BigDecimal balance = account.getBalance();
                    LOGGER.info("账户充值余额"+balance);

                    //现金券使用金额
                    BigDecimal couponUseFee = message.getCouponUseFee();
                    LOGGER.info("现金券使用金额:"+couponUseFee);

                    if (returnBalance.compareTo(couponUseFee) == 1 || returnBalance.compareTo(couponUseFee) == 0){   //表示账户返回余额大于现金券使用金额，只要更新账户返回余额
                        LOGGER.info("账户返回余额够支付(fail)：++++++++++++++++++++++++++++++");
                        BigDecimal resultFee = account.getReturnBalance().subtract(message.getCouponUseFee());
                        LOGGER.info("账户返回余额大于现金券使用金额,账户返回余额剩下的金额(fail)："+resultFee);
                        account.setReturnBalance(resultFee);

                        memberByCashCouponModel.setReturnBalanceUse(message.getCouponUseFee());   //表示用了多少金额的账户返回的现金券支付评估费
                        memberByCashCouponModel.setBalanceUse(new BigDecimal(0));


                    }else if (returnBalance.compareTo(couponUseFee) == -1 && returnBalance.compareTo(new BigDecimal(0)) == 1 && balance.compareTo(new BigDecimal(0)) == 1){    //表示账户返回余额有钱，但是不足，先扣完账户返回余额；再扣现金券充值余额
                        LOGGER.info("两个账户都有钱(fail)：++++++++++++++++++++++++++++++");
                        memberByCashCouponModel.setReturnBalanceUse(account.getReturnBalance());  //表示用了多少金额的账户返回的现金券支付评估费

                        //减去返回余额的钱，但是还是不足，从充值账户里面进行扣去
                        BigDecimal a = message.getCouponUseFee().subtract(account.getReturnBalance());
                        LOGGER.info("账户返回余额有钱，但是不足,还要支付的现金券金额(fail):"+a);

//                        BigDecimal b = account.getBalance().subtract(a);
//                        LOGGER.info("从账户充值余额里面扣去，剩下的现金券金额："+b);

                        if (account.getBalance().compareTo(a)  == 1 || account.getBalance().compareTo(a)  == 0){    //表示充值账户的现金券金额够进行支付
                            LOGGER.info("两个账户都有钱，加起来够支付(fail)：++++++++++++++++++++++++++++++");
                            account.setReturnBalance(new BigDecimal(0));        //返回余额现金券金额变为0
                            account.setBalance(account.getBalance().subtract(a));   //充值余额还剩下的

                            memberByCashCouponModel.setBalanceUse(a);                         //表示用了多少金额的充值余额的现金券支付评估费


                        }else {             //表示充值账户的现金券金额不够进行支付，还需要另付现金
                            LOGGER.info("两个账户都有钱，加起来不够支付(fail)：++++++++++++++++++++++++++++++");
                            account.setReturnBalance(new BigDecimal(0));    //返回余额现金券金额变为0
                            account.setBalance(new BigDecimal(0));          //充值账户余额变为0
                            memberByCashCouponModel.setBalanceUse(account.getBalance());     //表示用了多少金额的充值余额的现金券支付评估费
                        }


                    }else if (returnBalance.compareTo(couponUseFee) == -1 && account.getReturnBalance().compareTo(new BigDecimal(0)) == 1 && account.getBalance().compareTo(new BigDecimal(0)) == 0){
                        LOGGER.info("返回余额账户有钱，但不够支付，充值账户没钱(fail)：++++++++++++++++++++++++++++++");
                        memberByCashCouponModel.setReturnBalanceUse(account.getReturnBalance());  //表示用了多少金额的账户返回余额的现金券支付评估费
                        account.setReturnBalance(new BigDecimal(0));
                        account.setBalance(new BigDecimal(0));
                        memberByCashCouponModel.setBalanceUse(new BigDecimal(0));

                    }else if (account.getBalance().compareTo(couponUseFee) == -1 && account.getBalance().compareTo(new BigDecimal(0)) == 1 && account.getReturnBalance().compareTo(new BigDecimal(0)) == 0){
                        LOGGER.info("充值账户有钱，但不够支付，余额账户没钱(fail)：++++++++++++++++++++++++++++++");
                        memberByCashCouponModel.setReturnBalanceUse(new BigDecimal(0));  //表示用了多少金额的账户返回余额的现金券支付评估费
                        memberByCashCouponModel.setBalanceUse(account.getBalance());
                        account.setReturnBalance(new BigDecimal(0));
                        account.setBalance(new BigDecimal(0));

                    }else if (account.getBalance().compareTo(couponUseFee) == 1 || account.getBalance().compareTo(couponUseFee) == 0 && account.getReturnBalance().compareTo(new BigDecimal(0)) == 0){
                        LOGGER.info("充值账户有钱，够支付，余额账户没钱(fail)：++++++++++++++++++++++++++++++");
                        memberByCashCouponModel.setReturnBalanceUse(new BigDecimal(0));  //表示用了多少金额的账户返回余额的现金券支付评估费
                        memberByCashCouponModel.setBalanceUse(message.getCouponUseFee());
                        account.setReturnBalance(new BigDecimal(0));
                        account.setBalance(account.getBalance().subtract(message.getCouponUseFee()));

                    }

                    //失败了，不需要更新账户余额
                   /* userAccountContract.updateAccountByAccountNumber(account);
                    LOGGER.info("手机号为："+message.getMobile()+"的用户账户余额更新成功 ："+JSON.toJSONString(account));*/

                    LOGGER.info("要保存现金券使用明细对象："+JSON.toJSON(memberByCashCouponModel));
                    memberByCashCouponContract.saveCashCouponUserDetail(memberByCashCouponModel);
                    LOGGER.info("memberId为："+memberId+"现金券使用详情保存成功（此笔交易为失败）");

                }catch (Exception e){
                    LOGGER.info("手机号为："+message.getMobile()+"的用户现金券使用详情保存失败",e);

                }


            }


        } else {
            LOGGER.error("message is null!");
        }
    }

}

