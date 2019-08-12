package com.tasfe.sis.auth.service;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.sis.auth.entity.Login;
import com.tasfe.sis.auth.model.*;

import java.util.List;

/**
 * Created by hefusang on 2017/8/22.
 */
public interface AccountService {

    public Login getAccount(LoginInfos loginInfos) throws Exception;

    public Long add(AccountInfos accountInfos) throws Exception;

    public Login selectByAccount(String account) throws Exception;

    public List<AccountModel> selectAccount(QueryAccountInfos queryAccountInfos) throws Exception;

    public void updateAccount(UpdateAccountInfos updateAccountInfos) throws Exception;

    public void deleteAccount(DeleteAccountInfos deleteAccountInfos) throws Exception;

    public String authLogin(Login login);

    /**
     * @param accountInfos
     * @param criteria
     * @return
     * @throws Exception
     */
    public Page<Login> pagingAccount(AccountInfos accountInfos, Criteria criteria) throws Exception;

}
