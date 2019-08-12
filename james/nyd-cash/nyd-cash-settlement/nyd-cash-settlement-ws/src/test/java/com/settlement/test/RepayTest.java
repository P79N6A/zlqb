package com.settlement.test;

import com.alibaba.fastjson.JSON;
import com.nyd.settlement.entity.repay.YmtPayFlow;
import com.nyd.settlement.model.dto.RecommendRefundDto;
import com.nyd.settlement.model.dto.repay.RepayAdviceDto;
import com.nyd.settlement.service.RepayAuditService;
import com.nyd.settlement.service.RepayService;
import com.nyd.settlement.service.YmtPayFlowService;
import com.nyd.settlement.service.YmtRefundService;
import com.tasfe.framework.support.model.ResponseData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2018/1/22
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/nyd/settlement/configs/ws/nyd-settlement-application.xml"})
public class RepayTest {
    @Autowired
    private RepayService repayService;

    @Autowired
    private RepayAuditService repayAuditService;

    @Autowired
    private YmtRefundService ymtRefundService;
    @Autowired
    private YmtPayFlowService ymtPayFlowService;

    @Test
    public void testAa(){
        String s1 = "11180711900003S1521038003646";
        YmtPayFlow ymtPayFlow = ymtPayFlowService.findByTradeNo(s1);
        System.out.println(JSON.toJSONString(ymtPayFlow));
    }




    @Test
    public void testAdviceAmount() throws Exception {
        RepayAdviceDto dto = new RepayAdviceDto();
        dto.setBillNo("101514204222640001");
        dto.setCalcuteDate("2018-01-29 12:12:12");
        System.out.println(repayService.adviceAmount(dto));
    }
    @Test
    public void testRepayCal() throws Exception {
//        Repay
//        List<Long> ids = new ArrayList<>();
//        ids.add(385l);
//        System.out.println(repayAuditService.audit(ids));
    }
    @Test
    public void test1(){
//        OverdueBill bill = repayMapper.queryOverdueBill("12");
        System.out.println("123");
    }

    @Test
    public void test2(){
//        String birthday ="2017-02-22";
//
//        SimpleDateFormat sdf = new SimpleDateFormat(("yyyy-MM-dd"));
//        java.util.Date date = null;
//        try {
//            date = sdf.parse(birthday);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(date);
//        System.out.println(new java.sql.Date(date.getTime()));


//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(sdf.format(date));

        RecommendRefundDto rd = new RecommendRefundDto();
        String s1 = "2018-03-20 16:09:31";
        rd.setRefundTime(s1);
        rd.setRefundStatus(1);
        rd.setRefundFee(new BigDecimal(0.01));//退款手续费
        rd.setRefundAmount(new BigDecimal(0.01));//退款金额
        rd.setRefundAccount("182****123");
        rd.setRefundFlowNo("2018030821001004410207346730");
        rd.setRefundChannel("zfb");
        rd.setRefundType(2);
        rd.setOrderNo("201803165588");//订单号
        rd.setName("德玛");
        rd.setMobile("18214777579");
        rd.setRefundReason("无理由退款。。。。。。。。。");
        ResponseData responseData = ymtRefundService.addRecommendRefund(rd);
        System.out.println(JSON.toJSONString(responseData));

    }

    @Test
    public void  test3(){
//        String s1 = "11180672000001S1520511566866";
//        int pos = s1.indexOf("S");
//        System.out.println(pos);
//        String ss = s1.substring(0, pos);
//        System.out.println(ss);
//        int a = 10;
//        int b = 10;
//        System.out.println(a == b);
//
//        System.out.println("*****************");
//
//        Integer c = 10;
//        Integer d = 10;
//        System.out.println(c.equals(d));
//        System.out.println("=======================");
//
//        Order o1 = new Order();
//        Order o2 = new Order();
//        System.out.println(o1.equals(o2));
//
//        System.out.println("-----------------------");
//        System.out.println(o1 == o2);

//        System.out.println(System.currentTimeMillis());
//
//        String s = MD5Utils.MD5("1521543047783");
//        System.out.println(s);

//        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//        String result = "";
//        String res = "";
//        for (int i = 0; i < 13; i++) {
//            Random rd = new Random();
//            result += chars.charAt(rd.nextInt(chars.length() - 1));
//             res = MD5Utils.MD5(result);
//        }
//        System.out.println(result);
//        System.out.println("///////////////");
//        System.out.println(res);


//        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//        String result = "";
//        String res = "";
//        long time = System.currentTimeMillis();
//        for (int i = 0; i < 16; i++) {
//            Random rd = new Random();
//            result += chars.charAt(rd.nextInt(chars.length() - 1));
//            res = MD5Utils.MD5(String.valueOf(time))+MD5Utils.MD5(result);
//        }
////        return res;
//        System.out.println(res);
//
//        System.out.println("***************");
//        System.out.println(res.length());



    }


}
