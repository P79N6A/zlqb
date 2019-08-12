package com.user.test;

import com.alibaba.fastjson.JSON;
import com.nyd.user.api.*;
import com.nyd.user.entity.LoginLog;
import com.nyd.user.model.dto.UserDto;
import com.nyd.user.model.dto.YmtUserDto;
import com.tasfe.framework.support.model.ResponseData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/user/configs/ws/nyd-user-application.xml"})
@Controller
public class GetYmtTestController {

    @Autowired
    private GetYmtUserDataService  getYmtUserDataService;
    @Autowired
    private UserInfoContract userInfoContract;
    @Autowired
    private UserAccountContract userAccountContract;
    @Autowired
    private UserIdentityContract userIdentityContract;
    @Autowired
    private UserSourceContract userSourceContract;

    @Test
    public void test1() throws Exception {
        YmtUserDto ymtUserDto = new YmtUserDto();
//        ymtUserDto.setUserId("201804021122");
        ymtUserDto.setIBankUserId("201804021122");
        ymtUserDto.setRealName("韩梅梅");
        ymtUserDto.setIdNumber("34082620180402184X");
        ymtUserDto.setCertificateType("10");
        ymtUserDto.setNation("汉");
        ymtUserDto.setBirth("1986年2月1日");
//        ymtUserDto.setGender();

        ymtUserDto.setAccount_type(10);
        ymtUserDto.setAccountNumber("18279294524");
        ymtUserDto.setLimitCredit(new BigDecimal(0));
        ymtUserDto.setUsableCredit(new BigDecimal(1000));
        ymtUserDto.setUsedCredit(new BigDecimal(0));
        ymtUserDto.setProductCode("cash");
        ymtUserDto.setStatus("10");
        ymtUserDto.setSource("ymt");

        ymtUserDto.setPasswordType(1);
        ymtUserDto.setPassword("123");

        ymtUserDto.setAccountName("韩梅梅");
        ymtUserDto.setBankName("浦发银行");
        ymtUserDto.setBankAccount("6222024100000019994");
        ymtUserDto.setReservedPhone("18279294524");

        ymtUserDto.setDirectContactName("李雷");
        ymtUserDto.setDirectContactRelation("男朋友");
        ymtUserDto.setDirectContactMobile("18255668895");
        ymtUserDto.setMajorContactName("韩正");
        ymtUserDto.setMajorContactRelation("父女");
        ymtUserDto.setMajorContactMobile("15766338844");

        ymtUserDto.setIdAddress("北京市");
        ymtUserDto.setSignOrg("北京市公安局");
        ymtUserDto.setEffectTime("2018.01.01-2028.01.01");
        ymtUserDto.setLivingAddress("北京市朝阳区大唐街200号");
        ymtUserDto.setLivingProvince("北京市");
        ymtUserDto.setLivingCity("北京市");
        ymtUserDto.setLivingDistrict("00001");
        ymtUserDto.setMaritalStatus("未婚");
        ymtUserDto.setHighestDegree("本科");
        ymtUserDto.setFaceRecognition("1");
        ymtUserDto.setFaceRecognitionSimilarity(new BigDecimal(90));

        ymtUserDto.setIndustry("互联网");
        ymtUserDto.setProfession("UI设计师");
        ymtUserDto.setCompany("百度互联网科技有限公司");
        ymtUserDto.setCompanyAddress("北京市朝阳区DBT大楼");
        ymtUserDto.setCompanyProvince("北京市");
        ymtUserDto.setCompanyDistrict("北京市");
        ymtUserDto.setCompanyDistrict("朝阳区");
        ymtUserDto.setPosition("工程师");
        ymtUserDto.setSalary("12K");
        ymtUserDto.setTelDistrictNo("0112");
        ymtUserDto.setTelephone("255566");
        ymtUserDto.setTelExtNo("123456");

        ymtUserDto.setAuthFlag("");
        ymtUserDto.setZmxyFlag("0");
        ymtUserDto.setMobileFlag("0");
        ymtUserDto.setOnlineBankFlag("0");
        ymtUserDto.setTbFlag("0");
        ymtUserDto.setGxbFlag("0");
        ymtUserDto.setIdCardBackPhoto("");
        ymtUserDto.setIdCardFrontPhoto("");

        System.out.println(JSON.toJSONString(ymtUserDto));
        getYmtUserDataService.saveYmtUserDataAsStep(ymtUserDto);
    }

    @Test
    public void test() {
        UserDto userDto = new UserDto();
        userDto.setAccountNumber("15000296472");
        userDto.setIdNumber("320382199006194210");
        userInfoContract.getExistUserInfo(userDto);
    }

    @Test
    public void testQuery(){
        System.out.println(JSON.toJSONString(userIdentityContract.getUserInfoByIdNo("320382199006194210")));
    }

    @Test
    public void test11(){
        ResponseData responseData = userSourceContract.selectUserSourceByMobile("17621341532");
        LoginLog  data =(LoginLog) responseData.getData();
        String appName = data.getAppName();
        System.out.println(JSON.toJSONString(responseData));
    }
}
