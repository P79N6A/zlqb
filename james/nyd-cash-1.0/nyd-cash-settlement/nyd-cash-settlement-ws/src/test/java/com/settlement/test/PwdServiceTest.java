package com.settlement.test;

import com.alibaba.fastjson.JSON;
import com.nyd.settlement.model.dto.PwdDto;
import com.nyd.settlement.service.PwdService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Cong Yuxiang
 * 2018/2/1
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/nyd/settlement/configs/ws/nyd-settlement-application.xml"})
public class PwdServiceTest {
    @Autowired
    private PwdService pwdService;

    @Test
    public void testValidtePwd(){
        PwdDto dto = new PwdDto();
        dto.setType(1);
        dto.setPwd("123456");
        System.out.println(JSON.toJSONString(pwdService.validatePwd(dto)));
    }
}
