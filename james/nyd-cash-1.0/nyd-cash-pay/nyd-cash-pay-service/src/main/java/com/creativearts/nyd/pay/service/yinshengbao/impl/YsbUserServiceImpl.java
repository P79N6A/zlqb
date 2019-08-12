package com.creativearts.nyd.pay.service.yinshengbao.impl;

import com.creativearts.nyd.pay.service.yinshengbao.YsbUserService;
import com.nyd.pay.dao.mapper.YsbUserMapper;
import com.nyd.pay.entity.YsbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class YsbUserServiceImpl implements YsbUserService {

   @Autowired
   private YsbUserMapper ysbUserMapper;

    @Override
    public void save(YsbUser user) throws Exception {
        ysbUserMapper.insert(user);
    }

    @Override
    public List<YsbUser> findByUserId(Map<String, String> params) {
        return ysbUserMapper.selectByUserIdAndCardNo(params);
    }

//    @Override
//    public List<YsbUser> findByUserId(String userid, String cardno) throws Exception {
//        return ysbUserMapper.selectByUserId(userid,cardno);
//    }
}
