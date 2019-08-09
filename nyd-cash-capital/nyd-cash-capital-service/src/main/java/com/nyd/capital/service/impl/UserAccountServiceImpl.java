package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.dao.ds.DataSourceContextHolder;
import com.nyd.capital.entity.UserKzjr;
import com.nyd.capital.model.dto.KzjrAccountDto;
import com.nyd.capital.model.enums.KzjrAccountStatus;
import com.nyd.capital.model.kzjr.QueryAccountRequest;
import com.nyd.capital.model.kzjr.UserKzjrInfo;
import com.nyd.capital.service.UserAccountService;
import com.nyd.capital.service.aspect.RoutingDataSource;
import com.nyd.capital.service.kzjr.KzjrService;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.support.service.impls.CrudServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/
@Service
public class UserAccountServiceImpl extends CrudServiceImpl<UserKzjrInfo, UserKzjr, Long> implements UserAccountService {

    Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);
    @Autowired
    private KzjrService kzjrService;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public KzjrAccountDto queryInfo(String userId, String accNo, Integer idType, String idNo) {

        logger.info("queryInfo入参" + accNo + "***" + userId + "****" + idNo);
        try {
            if (userId == null) {
                throw new RuntimeException("查询kzjr账户 传入的userid为空");
            }

//        Criteria criteria = Criteria.from(UserKzjr.class).where().and("user_id", Operator.EQ,userId).endWhere();
            QueryAccountRequest request = new QueryAccountRequest();
            request.setIdType(idType);
            request.setIdNo(idNo);


            JSONObject resultObj = kzjrService.queryAccount(request);
//        List<UserKzjrInfo> infos = this.find(criteria);
            logger.info("查询kzjr accountId的结果" + JSON.toJSONString(resultObj));
            KzjrAccountDto dto = new KzjrAccountDto();
            if (resultObj.getInteger("status") == 0) {
                if (resultObj.getJSONObject("data") == null) {
                    dto.setStatus(KzjrAccountStatus.NO_ACCOUNT);
                    return dto;
                } else {
                    String accountId = resultObj.getJSONObject("data").getString("accountId");
                    Integer accountOpenStatus = resultObj.getJSONObject("data").getInteger("accountOpenStatus");
                    String cardNo = resultObj.getJSONObject("data").getString("cardNo");
                    logger.info("**" + accountId + "**" + accountOpenStatus + "**" + cardNo);
                    cardNo = cardNo == null ? "" : cardNo.trim();
                    logger.info("**" + accountId + "**" + accountOpenStatus + "**" + cardNo);

                    //保存用户accountId

                    try {
                        Criteria criteria = Criteria.from(UserKzjr.class).where().and("user_id", Operator.EQ,userId)
                                .endWhere();
                        long count = this.count(criteria);
                        if(count==0) {
                            UserKzjr userKzjr = new UserKzjr();
                            userKzjr.setAccountId(accountId);
                            userKzjr.setBankAccount(cardNo);
                            userKzjr.setStatus(0);
                            userKzjr.setUserId(userId);
                            this.save(userKzjr);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info("保存异常", e);
                    }
                    if (accountOpenStatus == 1) {
                        dto.setStatus(KzjrAccountStatus.NO_ACCOUNT);
                        return dto;
                    }
                    if (accountOpenStatus == 3) {
                        dto.setStatus(KzjrAccountStatus.OPENING);
//                        dto.setStatus(KzjrAccountStatus.NO_ACCOUNT);
                        return dto;
                    }
                    if (accountOpenStatus == 2 && accNo.trim().equals(cardNo)) {
                        dto.setStatus(KzjrAccountStatus.NORMAL);

                    } else if (accountOpenStatus == 2 && !accNo.trim().equals(cardNo)) {
                        dto.setStatus(KzjrAccountStatus.NO_BANKACC);

                    }
                    dto.setP2pId(accountId);
                    return dto;
                }
            } else {
                logger.info("查询accountId结果状态非0");
                return null;
            }

        } catch (Exception ee) {
            ee.printStackTrace();
            logger.info("莫名异常" + userId, ee);
            return null;
        }

//        if(infos==null||infos.size()==0){
//            dto.setStatus(KzjrAccountStatus.NO_ACCOUNT);
//            return dto;
//        }
//        for(UserKzjrInfo info:infos){
//            if(accNo.trim().equals(info.getBankAccount())&&info.getStatus()==0){
//                dto.setStatus(KzjrAccountStatus.NORMAL);
//                return dto;
//            }
//        }
//        dto.setStatus(KzjrAccountStatus.NO_BANKACC);
//        dto.setP2pId(infos.get(0).getAccountId());
//        dto.setId(infos.get(0).getId());
//
//        return dto;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void saveInfo(UserKzjrInfo info) {
        UserKzjr userKzjr = new UserKzjr();
        userKzjr.setAccountId(info.getAccountId());
        userKzjr.setBankAccount(info.getBankAccount());
        userKzjr.setStatus(info.getStatus());
        userKzjr.setUserId(info.getUserId());
        this.save(userKzjr);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public String queryAccountIdByUserId(String userId) {

        Criteria criteria = Criteria.from(UserKzjr.class).where().and("user_id", Operator.EQ, userId).endWhere();
        List<UserKzjrInfo> infos = this.find(criteria);
        if (infos == null || infos.size() == 0) {
            throw new RuntimeException("queryAccountIdByUserId error");
        }

        return infos.get(0).getAccountId();
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void deleteByid(Long id) {
        this.delete(id);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void updateBankAccById(Long id) {
        Criteria criteria = Criteria.from(UserKzjr.class).where().and("id", Operator.EQ, id).endWhere();
        UserKzjrInfo info = new UserKzjrInfo();

        info.setBankAccount("");

        this.update(info, criteria);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void updateBankAccByP2pId(String p2pId, String bankAcc) {
        Criteria criteria = Criteria.from(UserKzjr.class).where().and("account_id", Operator.EQ, p2pId).endWhere();
        UserKzjrInfo info = new UserKzjrInfo();

        info.setBankAccount(bankAcc);

        this.update(info, criteria);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void test() {
        UserKzjrInfo userKzjrQuery = new UserKzjrInfo();
        userKzjrQuery.setUserId("173542300002");
        Criteria criteria = Criteria.from(UserKzjr.class).where().and("user_id", Operator.EQ,"123")
                .endWhere();
        long count = this.count(criteria);
        System.out.println(count);
    }

//    @Override
//    public List<UserKzjrInfo> findByCondition(Condition condition) {
//
//        return null;
//    }
}
