package com.nyd.member;

import com.nyd.member.api.MemberContract;
import com.nyd.member.api.MemberLogContract;
import com.nyd.member.dao.MemberConfigDao;
import com.nyd.member.dao.MemberDao;
import com.nyd.member.entity.Member;
import com.nyd.member.model.MemberModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hwei on 2017/12/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/nyd/member/configs/ws/nyd-member-application.xml"})
public class TestMember {
    @Autowired
    MemberDao memberDao;
    @Autowired
    MemberConfigDao memberConfigDao;
    @Autowired
    MemberContract memberContract;
    @Autowired
    MemberLogContract memberLogContract;

    @Test
    public void testMemberDao() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // +30
        calendar.add(Calendar.DAY_OF_MONTH,30);
        date = calendar.getTime();
        Member member = new Member();
        member.setUserId("1234");
        member.setMobile("13022341234");
        member.setMemberType("1");
        member.setExpireTime(date);
        try {
            memberDao.save(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMemberInfoDao() throws Exception {
//        System.out.println(memberDao.getMemberByUserId("1234").toString());
        System.out.println(memberConfigDao.getMermberConfigByType("2"));
    }

    @Test
    public void testSaveMemberAndMemberLog() {
        MemberModel memberModel = new MemberModel();
        memberModel.setUserId("173391600002");
        memberModel.setMobile("13022341234");
        memberModel.setMemberType("1");
        memberContract.saveMember(memberModel);
    }

    @Test
    public void testUpdateMemberLog() {
        memberLogContract.updateMemberLog("173391600001");
    }
}
