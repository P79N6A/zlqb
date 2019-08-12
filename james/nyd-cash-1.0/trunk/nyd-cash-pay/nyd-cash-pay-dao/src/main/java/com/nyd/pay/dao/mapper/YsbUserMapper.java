package com.nyd.pay.dao.mapper;

import com.nyd.pay.entity.YsbUser;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface YsbUserMapper {
    void insert(YsbUser user);

    List<YsbUser> selectByUserIdAndCardNo(Map<String, String> params);

}
