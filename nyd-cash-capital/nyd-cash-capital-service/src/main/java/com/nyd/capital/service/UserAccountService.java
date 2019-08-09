package com.nyd.capital.service;

import com.nyd.capital.entity.UserKzjr;
import com.nyd.capital.model.dto.KzjrAccountDto;
import com.nyd.capital.model.kzjr.UserKzjrInfo;
import com.tasfe.framework.support.service.CrudService;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/
public interface UserAccountService extends CrudService<UserKzjrInfo,UserKzjr,Long>{

    KzjrAccountDto queryInfo(String userId, String accNo,Integer idType,String idNo);

    void saveInfo(UserKzjrInfo info);

    String queryAccountIdByUserId(String userId);

    void deleteByid(Long id);

    void updateBankAccById(Long id);
    void updateBankAccByP2pId(String p2pId,String bankAcc);


    void test();
}
