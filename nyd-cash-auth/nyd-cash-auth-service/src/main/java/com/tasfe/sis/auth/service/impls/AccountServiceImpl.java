package com.tasfe.sis.auth.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.auth.entity.Login;
import com.tasfe.sis.auth.entity.LoginRole;
import com.tasfe.sis.auth.entity.Role;
import com.tasfe.sis.auth.model.*;
import com.tasfe.sis.auth.service.AccountRoleService;
import com.tasfe.sis.auth.service.AccountService;
import com.tasfe.zh.base.service.CacheService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hefusang on 2017/8/22.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private CrudTemplate crudTemplate;

    /*@Autowired
    private CrudService crudService;*/

    @Autowired
    private AccountRoleService accountRoleService;

    @Autowired
    private CacheService cacheService;

    @Override
    public Login getAccount(LoginInfos loginInfos) throws Exception {
        Login record = new Login();
        record.setAccount(loginInfos.getAccount());
        record.setPwd(loginInfos.getPwd());
        List<Login> logins = crudTemplate.find(record, Criteria.from(Login.class));
        if (logins.size() > 0)
            return logins.get(0);
        return null;
    }

    @Override
    public Long add(AccountInfos accountInfos) throws Exception {
        Login login = new Login();
        login.setAccount(accountInfos.getAccount());
        login.setStatus(new Integer(accountInfos.getStatus()));
        login.setPwd(accountInfos.getPwd());
        login.setTyp(new Integer(accountInfos.getTyp()));
        login.setNick(accountInfos.getNick());
        crudTemplate.save(login);
        List<Login> logins = crudTemplate.find(login, Criteria.from(Login.class));
        if (logins.size() > 0) {
            return logins.get(0).getId();
        }
        return null;
    }

    @Override
    public Login selectByAccount(String account) throws Exception {
        Login record = new Login();
        record.setAccount(account);
        List<Login> logins = crudTemplate.find(record, Criteria.from(Login.class));
        if (logins.size() > 0) {
            return logins.get(0);
        }
        return null;
    }

    @Override
    public List<AccountModel> selectAccount(QueryAccountInfos queryAccountInfos) throws Exception {
        Login record = new Login();
        record.setStatus(new Integer(queryAccountInfos.getStatus()));
        if ((StringUtils.isEmpty(queryAccountInfos.getCellPhone())) && (StringUtils.isEmpty(queryAccountInfos.getEmail()))) {
            if (StringUtils.isNotEmpty(queryAccountInfos.getAccount())) {
                record.setAccount(queryAccountInfos.getAccount());
            }
            List<Login> logins = crudTemplate.find(record, Criteria.from(Login.class));
            List<AccountModel> resList = new ArrayList<>();
            for (Login login : logins) {
                AccountModel accountModel = new AccountModel();
                BeanUtils.copyProperties(login, accountModel);
                //���ҵ�ǰ�˺�ӵ�м�����ɫ
                List<Role> roles = accountRoleService.getRolesByAccountId(login.getId());
                accountModel.setRoles(roles);
                resList.add(accountModel);
            }
            return resList;
        } else {
            return null;
        }
    }

    @Override
    public void updateAccount(UpdateAccountInfos updateAccountInfos) throws Exception {
        //修改account基本信息
        Login record = new Login();
        record.setId(new Long(updateAccountInfos.getAccountId()));
        if (StringUtils.isNotEmpty(updateAccountInfos.getPwd())) {
            record.setPwd(updateAccountInfos.getPwd());
        }
        if (StringUtils.isNotEmpty(updateAccountInfos.getStatus())) {
            record.setStatus(new Integer(updateAccountInfos.getStatus()));
        }
        if (StringUtils.isNotEmpty(updateAccountInfos.getNick())) {
            record.setNick(updateAccountInfos.getNick());
        }
        if (StringUtils.isNotEmpty(updateAccountInfos.getTyp())) {
            record.setTyp(new Integer(updateAccountInfos.getTyp()));
        }
        crudTemplate.update(record, Criteria.from(Login.class));
        //关联角色
        LoginRole record1 = new LoginRole();
        record1.setAid(new Long(updateAccountInfos.getAccountId()));
        crudTemplate.delete(record1, Criteria.from(LoginRole.class).where().and("aid", Operator.EQ).endWhere());
        if (StringUtils.isNotEmpty(updateAccountInfos.getRoleNames())) {
            AccountInfos accountInfos = new AccountInfos();
            accountInfos.setAccountId(updateAccountInfos.getAccountId());
            accountInfos.setRoleNames(updateAccountInfos.getRoleNames());
            accountRoleService.correlateRoleAndAccount(accountInfos);
        }
    }

    @Override
    public void deleteAccount(DeleteAccountInfos deleteAccountInfos) throws Exception {
        crudTemplate.delete(Login.class, new Long(deleteAccountInfos.getId()));
    }

    @Override
    public String authLogin(Login login) {
        //生成code
        StringBuffer sb = new StringBuffer();
        sb.append(System.currentTimeMillis());
        sb.append(login.getAccount());
        sb.append(login.getPwd());

        sb.append(System.currentTimeMillis());
        byte[] base64 = Base64.encodeBase64(sb.toString().getBytes());
        String code = DigestUtils.md5(base64).toString();

        //String key = HljnsConstants.PREFIX_REDIS_CODE + code;
        String key = "";
        cacheService.set(key, login.getId() + "");
        cacheService.expire(key, 60 * 30);

        return code;
    }


    @Override
    public Page<Login> pagingAccount(AccountInfos accountInfos, Criteria criteria) throws Exception {
        Page page = crudTemplate.paging(accountInfos, criteria);
        return page;
    }
}
