package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.activity.api.CashCouponContract;
import com.nyd.activity.dao.CashCouponDao;
import com.nyd.activity.model.CashCouponInfo;
import com.nyd.activity.model.vo.CashCouponInfoVo;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.model.UserCashCoupon;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.service.CashCouponService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CashCouponServiceImpl implements CashCouponService{
    private static Logger logger = LoggerFactory.getLogger(CashCouponServiceImpl.class);

    @Autowired
    private UserAccountContract userAccountContract;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CashCouponContract cashCouponContract;


    /**
     * 获取所有现金券类型和账户总余额
     * @param accountNumber
     * @return
     */
    @Override
    public ResponseData getAllInformation(String accountNumber) {

        ResponseData responseData = ResponseData.success();
        if(!StringUtils.isNotBlank(accountNumber)){
            responseData = ResponseData.error("参数为空");
            return responseData;
        }

        UserCashCoupon userCashCoupon = new UserCashCoupon();
        try {
            List<AccountDto> accountDtoList = accountDao.queryBalance(accountNumber);
            BigDecimal balance = new BigDecimal(0);
            BigDecimal returnBalance = new BigDecimal(0);
            BigDecimal totalBalance = new BigDecimal(0);
            if (accountDtoList != null && accountDtoList.size() > 0) {
                AccountDto accountDto = accountDtoList.get(0);
                logger.info("现金券余额对象："+ JSON.toJSON(accountDto));
                if (accountDto.getBalance() != null){
                    balance = accountDto.getBalance();
                    logger.info("充值余额:"+balance);
                }

                if (accountDto.getReturnBalance() != null){
                    returnBalance = accountDto.getReturnBalance();
                    logger.info("返回余额:"+returnBalance);
                }

                /**
                 * 账户余额
                 */
                totalBalance = balance.add(returnBalance);
                userCashCoupon.setTotalBalance(totalBalance);
                logger.info("账户余额:"+totalBalance);
                responseData.setData(userCashCoupon);
            }

            /**
             * 现金券列表：
             */
            ResponseData<List<CashCouponInfoVo>> allCashCoupon = cashCouponContract.getAllCashCoupon();
            List<CashCouponInfoVo> list = allCashCoupon.getData();
            logger.info("现金券列表："+ JSON.toJSON(list));
            /*List<CashCouponInfoVo> list = new ArrayList<CashCouponInfoVo>();
            if(cashCouponInfos != null && cashCouponInfos.size()>0){
                cashCouponInfos.stream().forEach(cashCouponInfo->{
                    list.add(infoToInfoVo(cashCouponInfo)) ;
                });
            }*/
            userCashCoupon.setCashCouponInfoVos(list);
            logger.info("获取所有现金券类型和账户总余额："+ JSON.toJSONString(responseData));
        }catch (Exception e){
            logger.error("获取现金券配置信息出错 ",e);
            responseData = ResponseData.error("服务器开小差");
        }
        return responseData;
    }

    private  CashCouponInfoVo infoToInfoVo(CashCouponInfo cashCouponInfo){
        if(cashCouponInfo == null){
            return null;
        }
        CashCouponInfoVo vo = new CashCouponInfoVo();
        vo.setId(cashCouponInfo.getId());
        vo.setCashName(cashCouponInfo.getCashName());
        vo.setCashFeeType(cashCouponInfo.getCashFeeType().toString());
        vo.setCashFeeUnit(cashCouponInfo.getCashFeeUnit());
        vo.setActivityFlag(cashCouponInfo.getActivityFlag().toString());
        vo.setActivityAndroidFee(cashCouponInfo.getActivityAndroidFee().toString());
        vo.setActivityIosFee(cashCouponInfo.getActivityIosFee().toString());
        vo.setActivityFeeUnit(cashCouponInfo.getActivityFeeUnit());
        return vo;
    }
}
