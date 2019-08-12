package com.nyd.user.service;

import com.nyd.user.model.AccountInfo;
import com.nyd.user.model.JobInfo;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Dengw on 2017/11/9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/user/configs/service/nyd-user-service-application.xml"})
public class LoginTest {
    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private IdentityInfoService identityInfoService;

    @Autowired
    private JobInfoService jobInfoService;

    @Autowired
    private StepInfoService stepInfoService;


    @Test
    public void testSendMsg() throws Exception {
        //userLoginService.sendMsgCode("18217368679","1");
    }

    @Test
    public void testregister() throws Exception {
        AccountInfo register = new AccountInfo();
        register.setAccountNumber("18217368679");
        register.setPassword("123456");
        //18217368679 767580
        register.setSmsCode("987707");
        System.out.printf(userLoginService.register(register).toString());
    }

    @Test
    public void testLogin() throws Exception {
        AccountInfo login = new AccountInfo();
        login.setAccountNumber("18217368679");
        //18217368679 767580
        login.setPassword("666666");
        System.out.printf(userLoginService.login(login,"192.168.22.2").toString());
    }

    @Test
    public void testLogout() throws Exception {
        System.out.printf(userLoginService.logout("3e20eb87-b704-433d-a67d-8894519b0c98").toString());
    }

    @Test
    public void testjudgeTimeout() throws Exception {
        userLoginService.judgeTimeout("3e20eb87-b704-433d-a67d-8894519b0c98","");
    }

    @Test
    public void testModifyPassword() throws Exception {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountNumber("18217368679");
        //18217368679 767580
        //accountInfo.setMsgCode("987707");
        accountInfo.setPassword("1111111");
        accountInfo.setNewPassword("666666");
        //accountInfo.setNewPassword("111111");
        System.out.printf(userLoginService.modifyPassword(accountInfo).toString());
    }

    @Test
    public void testSaveIdentity() throws Exception {
        UserInfo userInfo = new UserInfo();
        UserDetailInfo userDetailInfo = new UserDetailInfo();
        //userInfo.setAccountId("4e41e07f-e7b7-4cff-85eb-d7bf3037717d");
        //userInfo.setUserId("21c2c008-d4c3-42f2-b05f-10575071043e");
        //userInfo.setNickName("Iron Man");
        //userDetailInfo.setUserId("21c2c008-d4c3-42f2-b05f-10575071043e");
        //userDetailInfo.setBirth("19890916");
        userDetailInfo.setIdAddress("中国上海");
        userDetailInfo.setSignOrg("上海市公安局");
        //identityInfoService.saveUserInfo(userInfo);
    }

    @Test
    public void testGetIdentity() throws Exception {
        System.out.printf(identityInfoService.getIdentityInfo("21c2c008-d4c3-42f2-b05f-10575071043e").toString());
    }

    @Test
    public void testSaveJob() throws Exception {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setUserId("21c2c008-d4c3-42f2-b05f-10575071043e");
        jobInfo.setIndustry("公务员");
        jobInfo.setCompany("中央政治局常委会");
        jobInfo.setSalary("10000000");
        jobInfo.setCompanyProvince("北京市");
        jobInfo.setCompanyCity("北京市");
        jobInfo.setCompanyDistrict("西城区");
        jobInfo.setCompanyAddress("中南海1号");
        jobInfo.setTelDistrictNo("010");
        jobInfo.setTelephone("56881688");
        jobInfo.setTelExtNo("001");
        jobInfoService.saveJobInfo(jobInfo);
    }

    @Test
    public void testGetJob() throws Exception {
        System.out.printf(jobInfoService.getJobInfo("21c2c008-d4c3-42f2-b05f-10575071043e").toString());
    }

}
