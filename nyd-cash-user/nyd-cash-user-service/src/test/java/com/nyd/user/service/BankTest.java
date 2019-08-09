package com.nyd.user.service;

import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserJobContract;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.JobDao;
import com.nyd.user.dao.mapper.AccountMapper;
import com.nyd.user.model.BankInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Dengw on 17/11/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/user/configs/service/xml/nyd-user-service.xml"})
public class BankTest {

    @Autowired
    BankInfoService bankInfoService;
    @Autowired
    JobDao jobDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    UserAccountContract userAccountContract;
    @Autowired
    UserJobContract userJobContract;

    @Test
    public void testCrud() throws Exception {

        BankInfo bankInfo = new BankInfo();

        bankInfo.setUserId("123456");
        bankInfo.setAccountName("dengw");
        bankInfo.setReservedPhone("13666688888");
       // bankInfoService.save(bankInfo);
        System.out.println(bankInfoService.getBankInfos("123456").toString());
       /* bank.setAccountType("A");
        bank.setAccountName("ddd");
        bank.setBankName("lait");
        bank.setReservedPhone("13599012345");
        bank.setBranchBank("abc");
        bank.setDepositBank("def");
        bank.setVerified(1);
        bank.setVerifiedType("hhh");
        bank.setVerifiedTime(new Date());*/
    }
    @Test
    public void testJob() throws Exception {
        System.out.println(jobDao.getJobsByUserId("d872fca0-5c9b-4b2c-9422-be3f811875d2"));
        System.out.println(jobDao.getJobsByCompany("baidu"));
    }
    @Test
    public void testAccount() throws Exception {
        System.out.println(accountDao.getAccountsByAccountNumber("18551094526"));
        System.out.println(accountDao.getAccountByuserId("173341400001"));
    }
    @Test
    public void testAccountList() {
        List<String> accounts = new ArrayList<>();
        accounts.add("18551094526");
        accounts.add("18094427866");
        userAccountContract.queryAccountByAccountList(accounts);
    }
    @Test
    public void testUserJob() {
        userJobContract.getJobByCompanyAdress("杨树浦路1088号");
    }
}
