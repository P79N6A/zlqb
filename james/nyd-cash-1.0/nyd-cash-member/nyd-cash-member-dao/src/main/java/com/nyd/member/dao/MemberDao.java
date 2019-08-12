package com.nyd.member.dao;

import com.nyd.member.entity.Member;
import com.nyd.member.model.MemberModel;

import java.util.List;

/**
 * Created by Dengw on 2017/12/6
 */
public interface MemberDao {
    void save(Member member) throws Exception;

    void update(Member member) throws Exception;

    MemberModel getMemberByUserId(String userId) throws Exception;

    List<String> getMembers() throws Exception;

}
