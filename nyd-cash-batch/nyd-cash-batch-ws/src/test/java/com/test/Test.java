package com.test;

import com.alibaba.fastjson.JSON;
import com.nyd.batch.dao.mapper.CuiShouMapper;
import com.nyd.batch.entity.OverdueBill;
import com.nyd.batch.service.FinanceReportService;
import com.nyd.batch.service.MiddleService;
import com.nyd.batch.service.mail.CuimiMail;
import com.nyd.batch.service.tmp.KzjrConfig;
import com.nyd.batch.service.tmp.KzjrService;
import com.nyd.batch.service.util.DateUtil;
import com.nyd.batch.service.util.MongoApi;
import com.nyd.capital.model.kzjr.QueryAssetRequest;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Cong Yuxiang
 * 2017/12/27
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/nyd/batch/configs/ws/nyd-batch-application.xml"})
public class Test {
    @Autowired
    private FinanceReportService financeReportService;
    @Autowired
    private CuimiMail cuimiMail;
    @Autowired
    private KzjrService kzjrService;
    @Autowired
    private KzjrConfig kzjrConfig;
    @Autowired
    private MiddleService middleService;

    @Autowired
    private CuiShouMapper cuiShouMapper;

    @Autowired
    private MongoApi mongoApi;

    @org.junit.Test
    public void testReport(){
        financeReportService.generateReport();
    }
    @org.junit.Test
    public void testKzjr(){
        QueryAssetRequest request = new QueryAssetRequest();
        request.setChannelCode(kzjrConfig.getChannelCode());
        request.setOrderId("101514516668124001");
        System.out.println(kzjrService.queryAsset(request));
    }
    @org.junit.Test
    public void testFriendCircle(){
        System.out.println(JSON.toJSONString(middleService.selectByMobile("13846751657")));
    }

    public static void main(String[] args) {
        String a = "2017-12-30 16:21:01";
        String b = "2018-01-02 01:00:00";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = df.parse(a);
            endDate = df.parse(b);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i =  DateUtil.getDay(beginDate,endDate);
        System.out.println(i);
    }

    @org.junit.Test
    public void test(){
//        cuimiMail.sendMail();
//        middleService.selectTestAop();
//        System.out.println(JSON.toJSONString(mongoApi.getAddressBooks("18621812636")));
        Map map1 = new HashMap();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        map1.put("starDate", DateFormatUtils.format(c,"yyyy-MM-dd"));
        map1.put("endDate", DateFormatUtils.format(DateUtils.addDays(new Date(), -1), "yyyy-MM-dd"));
        List<OverdueBill> bills = cuiShouMapper.getOverdueStatusAll(map1);
        System.out.println(JSON.toJSONString(bills));
    }


}
