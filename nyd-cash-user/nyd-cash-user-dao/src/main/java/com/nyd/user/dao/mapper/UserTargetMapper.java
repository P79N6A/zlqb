package com.nyd.user.dao.mapper;

import com.nyd.user.entity.UserTarget;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserTargetMapper {
    List<UserTarget> selectByMobile(String mobile);

    void updateUserTargetByMobile(UserTarget userTarget);

    void insert(UserTarget userTarget);

    List<UserTarget> findByRuleOneAndTypeOne(Map<String, String> params);

    List<UserTarget> findByRuleOneAndTypeTwo(Map<String, String> params);

    List<UserTarget> findByRuleOneAndTypeThree(Map<String, String> params);

    List<UserTarget> findByRuleTwoAndTypeOne(Map<String, String> params);

    List<UserTarget> findByRuleTwoAndTypeTwo(Map<String, String> params);

    List<UserTarget> findByRuleTwoAndTypeThree(Map<String, String> params);

    List<UserTarget> findByRuleThreeAndTypeOne(Map<String, String> params);

    List<UserTarget> findByRuleThreeAndTypeTwo(Map<String, String> params);

    List<UserTarget> findByRuleThreeAndTypeThree(Map<String, String> params);

    List<UserTarget> findByRuleFourAndTypeOne(Map<String, String> params);

    List<UserTarget> findByRuleFourAndTypeTwo(Map<String, String> params);

    List<UserTarget> findByRuleFourAndTypeThree(Map<String, String> params);
    
    List<UserTarget> userTargetFindByMd5mobile(Map<String, String> params);
    
    
}
