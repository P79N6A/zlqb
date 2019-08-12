import com.creativearts.nyd.pay.model.helibao.CreateOrderVo;
import com.creativearts.nyd.pay.model.helibao.QueryOrderVo;
import com.creativearts.nyd.pay.model.yinshengbao.YsbNotifyResponseVo;
import com.creativearts.nyd.pay.service.helibao.HelibaoPayService;
import com.creativearts.nyd.pay.service.helibao.util.Disguiser;
import com.creativearts.nyd.pay.service.log.LoggerUtils;
import com.creativearts.nyd.pay.service.yinshengbao.YsbPayService;
import com.nyd.msg.model.SmsRequest;
import com.nyd.pay.api.service.PayService;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

/**
 * Cong Yuxiang
 * 2017/12/16
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/pay/configs/ws/nyd-pay-application.xml"})
public class HelibaoTest {
    @Autowired
    private HelibaoPayService helibaoPayService;


    @Autowired
    private YsbPayService ysbPayService;
//    @Autowired
//    private ISendSmsService sendSmsService;
    Logger logger = LoggerFactory.getLogger(HelibaoTest.class);

    @Autowired
    private PayService payService;
    @Test
    public void testsms(){
        ResponseData responseData = payService.selectWithholdResult();
        System.out.println(responseData);
    }

    @Test
    public void testWithHold(){
        CreateOrderVo vo = new CreateOrderVo();
        vo.setP3_orderId("bill"+System.currentTimeMillis());
        vo.setP4_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));

        vo.setP11_orderAmount(new BigDecimal("1.51"));
        helibaoPayService.withHold(vo,null);
    }
    @Test
    public void testWithHoldQuery(){
        QueryOrderVo vo = new QueryOrderVo();
        vo.setP3_orderId("bill1513410658779");
        vo.setP4_timestamp("20171216155058");
        helibaoPayService.withHoldQuery(vo);
    }

    @Test
    public void testH(){
        RepayInfo info = new RepayInfo();
        logger.info("qinglajdgas");
        info.setRepayNo("1234");
        LoggerUtils.write(info);
        info.setRepayNo("45678");
        LoggerUtils.write(info);
    }

    /**
     * 随机生成13位纯数字，用来作为member_id
     */
    @Test
    public void creatNum(){
        int length = 13;
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();
        for(int i=0;i<length;i++)
        {
            sb.append(rand.nextInt(10));
        }
        String data=sb.toString();
        System.out.println(length+" random data: "+data);
    }

    @Test
    public void test1(){
        String assemblyRespOriSign = "&QuickPayConfirmPay&8000&失败&C1800026414&101524213121201001-1525424423453-z&QUICKPAY180504170023ZNET&2018-05-04 17:00:48&1014.00&F\n" +
                "AILED&cab9436b7b6c4116b8c01239e5ce04ee&CCB&DEBIT&5616&181101600010&0f3sNB7ksllS5rUuyzSsscwzci3ysBQB";

        String responseSign = "5cf5007297035686ea72b83e35170e17";

        String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
        System.out.println(responseSign);
        System.out.println("=========================");
        System.out.println(checkSign);

    }


    @Test
    public void test2(){
        YsbNotifyResponseVo ys = new YsbNotifyResponseVo();
        ys.setAmount("2");
        ys.setMac("C782F67818F80F20311ECAED030FA0F2");
        ys.setOrderId("181450100030xj271840079");
        ys.setResult_code("00");
        ys.setResult_msg("交易成功");
        ysbPayService.callBackProcess(ys);
        System.out.println("1111111111111111");

    }
}
