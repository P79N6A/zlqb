package com.user.test;

import com.nyd.user.model.AccountInfo;
import com.nyd.user.model.JobInfo;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.service.UserLoginService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Dengw on 2017/11/9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/user/configs/ws/nyd-user-application.xml"})
public class LoginTest {
    @Autowired
    private UserLoginService userLoginService;


    @Test
    public void testLogin() throws Exception {
        AccountInfo login = new AccountInfo();
        login.setAccountNumber("18217368679");
        //18217368679 767580
        login.setPassword("666666");
        for(int i=1;i<=100;i++) {
        	System.out.printf(userLoginService.login(login,"192.168.22."+i).toString());
        }
    }

}
