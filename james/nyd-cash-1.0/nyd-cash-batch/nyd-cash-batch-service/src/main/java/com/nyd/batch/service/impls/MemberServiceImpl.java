package com.nyd.batch.service.impls;

import com.alibaba.fastjson.JSON;
import com.nyd.batch.dao.ds.DataSourceContextHolder;
import com.nyd.batch.dao.mapper.TMemberConfigMapper;
import com.nyd.batch.dao.mapper.TMemberLogMapper;
import com.nyd.batch.dao.mapper.TMemberMapper;
import com.nyd.batch.entity.TMember;
import com.nyd.batch.entity.TMemberConfig;
import com.nyd.batch.entity.TMemberLog;
import com.nyd.batch.service.MemberService;
import com.nyd.batch.service.aspect.RoutingDataSource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Cong Yuxiang
 * 2017/12/27
 **/
@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private TMemberConfigMapper memberConfigMapper;


    @Autowired
    private TMemberLogMapper tMemberLogMapper;
    @Autowired
    private TMemberMapper tMemberMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MEMBER)
    @Override
    public List<TMemberLog> getMemberLog() {
        Map timeMap = new HashMap();
        String flagDate = DateFormatUtils.format(DateUtils.addDays(new Date(),-1),"yyyy-MM-dd");
        timeMap.put("flagDate", flagDate);
        return tMemberLogMapper.selectByTimeRange(timeMap);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MEMBER)
    @Override
    public TMember getMemberByUserId(String userid) {
        Map map = new HashMap();
        map.put("userId",userid);
        List<TMember> result = tMemberMapper.selectByUserId(map);
        if(result==null||result.size()==0){
            return null;
        }
        return result.get(0);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MEMBER)
    @Override
    public TMemberConfig getMemberConfigByType(String type) {
        Map typeMap = new HashMap();
        typeMap.put("memberType",type);
        return memberConfigMapper.selectByMemberType(typeMap);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MEMBER)
    @Override
    public List<TMemberLog> selectByUserId(String userId) {
        Map map = new HashMap();
        map.put("userId",userId);
        return tMemberLogMapper.selectByUserId(map);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MEMBER)
    @Override
    public List<TMemberLog> selectByOrderNo(String orderNo) {
        Map map = new HashMap();
        map.put("orderNo",orderNo);
        return tMemberLogMapper.selectByOrderNo(map);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MEMBER)
    @Override
    public TMemberLog selectByMemberId(String memberId) {
        if(memberId==null||memberId.trim().length()==0){
            return null;
        }
        Map map = new HashMap();
        map.put("memberId",memberId);
        List<TMemberLog> result = tMemberLogMapper.selectByMemberId(map);

        if(result==null||result.size()==0){
            return null;
        }
        return result.get(0);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MEMBER)
    @Override
    public void selectInnerTestAop() {
        Map map = new HashMap();
        map.put("memberId","8180291100001");
        List<TMemberLog> result = tMemberLogMapper.selectByMemberId(map);
        System.out.println(JSON.toJSONString(result));
    }
}
