package com.nyd.batch.service.impls;

import com.alibaba.fastjson.JSON;
import com.nyd.batch.dao.ds.DataSourceContextHolder;
import com.nyd.batch.dao.mapper.CuiShouMapper;
import com.nyd.batch.dao.mapper.FriendCircleMapper;
import com.nyd.batch.dao.mapper.TMemberLogMapper;
import com.nyd.batch.entity.Bill;
import com.nyd.batch.entity.FriendCircle;
import com.nyd.batch.entity.OverdueBill;
import com.nyd.batch.entity.TMemberLog;
import com.nyd.batch.service.MemberService;
import com.nyd.batch.service.MiddleService;
import com.nyd.batch.service.aspect.RoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2018/1/30
 **/
@Service
public class MiddleServiceImpl implements MiddleService{

    @Autowired
    private FriendCircleMapper friendCircleMapper;
    @Autowired
    private CuiShouMapper cuiShouMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TMemberLogMapper tMemberLogMapper;

    @Autowired
    private MiddleService middleService;



    /**
     * 获取电话频繁的联系人
     * @param mobile
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_WAREHOUSE)
    @Override
    public FriendCircle selectByMobile(String mobile) {
        List<FriendCircle> result = friendCircleMapper.selectByMobile(mobile);
        if(result==null||result.size()==0){
            return null;
        }
        return result.get(0);
    }

    /**
     * 获取每天的入催名单
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<OverdueBill> getCuishouBills() {
        return cuiShouMapper.getCuishouBills();
    }

    /**
     * 获取所有的 逾期名单
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<OverdueBill> getCuishouBillsAll() {
        return cuiShouMapper.getCuishouBillsAll();
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public Bill getBillByBillNo(Map map) {
        return cuiShouMapper.getBillByBillNo(map);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void selectTestAop(){
        System.out.println(cuiShouMapper.getCuishouBills());
//        memberService.selectInnerTestAop();
        middleService.selectInnerTestAop();
        System.out.println("***********************");
        System.out.println(cuiShouMapper.getCuishouBills());

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
