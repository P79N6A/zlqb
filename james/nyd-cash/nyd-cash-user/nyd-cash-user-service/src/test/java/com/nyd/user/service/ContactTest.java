package com.nyd.user.service;

import com.nyd.user.api.UserContactContract;
import com.nyd.user.model.ContactInfos;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Dengw on 2017/11/8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/user/configs/service/xml/nyd-user-service.xml"})
public class ContactTest {
    @Autowired
    private ContactInfoService contactInfoService;
    @Autowired
    UserContactContract userContactContract;

    @Test
    public void testCrud() throws Exception {
        ContactInfos contactInfo = new ContactInfos();
        contactInfo.setUserId("654321");
        contactInfo.setDirectContactRelation("朋友");
        contactInfo.setDirectContactName("A");
        contactInfo.setDirectContactMobile("111");
        contactInfo.setMajorContactRelation("同学");
        contactInfo.setMajorContactName("B");
        contactInfo.setMajorContactMobile("110");
        //contactInfoService.save(contactInfo);
        System.out.println(contactInfoService.getContactInfo("654321"));
    }

    @Test
    public void testContact(){
        System.out.println(userContactContract.getContactInfo("妞妞","15856142359"));
    }
    
    public static void main(String[] args) {
		String id = "130321199709034448";
		id = id.substring(6, 14);
		Date date1 = DateUtils.addYears(new Date(), -18);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dat = sdf.format(date1);
		System.out.println(id);
		System.out.println(dat);
		System.out.println(id.compareTo(dat));
		String id1 = "130321200012304448";
		id1 = id1.substring(6, 14);
		Date date11 = DateUtils.addYears(new Date(), -18);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		String dat1 = sdf1.format(date1);
		System.out.println(id1);
		System.out.println(dat1);
		System.out.println(id1.compareTo(dat1));
		String id2 = "130321200309034448";
		id2 = id2.substring(6, 14);
		Date date12 = DateUtils.addYears(new Date(), -18);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String dat2 = sdf2.format(date12);
		System.out.println(id2);
		System.out.println(dat2);
		System.out.println(id2.compareTo(dat2));
	}
	
}
