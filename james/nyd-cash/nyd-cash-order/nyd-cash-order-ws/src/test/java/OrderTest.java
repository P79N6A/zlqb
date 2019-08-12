import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.nyd.order.api.OrderChannelContract;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderExceptionContract;
import com.nyd.order.api.OrderWentongContract;
import com.nyd.order.service.consts.OrderConsts;
import com.nyd.order.service.util.DateUtil;
import com.nyd.order.service.util.OrderProperties;

/**
 * Cong Yuxiang
 * 2018/4/11
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/order/configs/ws/nyd-order-application.xml"})
public class OrderTest {
//    @Autowired
//    private BillContract billContract;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderWentongContract orderWentongContract;
    @Autowired
    private OrderProperties orderProperties;
    @Autowired
    private OrderExceptionContract orderExceptionContract;
    @Autowired
    private OrderChannelContract orderChannelContract;
    @Test
    public void testBill(){
//        System.out.println(JSON.toJSONString(billContract.getBillInfos("173562000001")));
//        BorrowConfirmDto dto = JSONObject.parseObject((String) redisTemplate.opsForValue().get(OrderConsts.REDIS_LOAN_KEY + "181021400001"),BorrowConfirmDto.class);
//        System.out.println(JSON.toJSONString(dto));
//
//        redisTemplate.opsForValue().set("jjjjkkk",JSON.toJSONString(dto));
//
//        BorrowConfirmDto dto1 = JSONObject.parseObject((String) redisTemplate.opsForValue().get("jjjjkkk"),BorrowConfirmDto.class);
//        System.out.println(JSON.toJSONString(dto1));
        String s = (String) redisTemplate.opsForValue().get(OrderConsts.REDIS_LOAN_KEY + "345346");
        System.out.println(s);
    }

    @Test
    public void testChannel(){
//        for(int i = 0;i<100;i++) {
//            ResponseData<String> channel = orderWentongContract.getChannel();
//            String channelData = channel.getData();
//            System.out.println(channelData);
//        }
        System.out.println(orderProperties.getWithholdPhone());
//        orderWentongContract.getOrderWTByTime("2018-05-06 00:00","2018-05-08 00:00",null,null);
    }
    @Test
    public void test1() {
    	Map<String,Object> map = new HashMap<String,Object>();
		map.put("pageNum", 1);
		map.put("pageSize", 20);
		orderExceptionContract.queryOrderExceptionList(map);
    }
    @Test
    public void test2() {
    	System.out.println(JSON.toJSONString(orderChannelContract.getChannel()));
    }
    @Test
    public void test3() {
    	Date d = new Date();
    	 int expireDay = DateUtil.getDayDiffUp(DateUtils.addDays(d, -10), new Date());
			int memberDay = Integer.valueOf(orderProperties.getMemberDays());
			if (expireDay > memberDay) {
			}
    }
    
    @Test
    public void test4() {
    	BigDecimal d1 = new BigDecimal(300000);
    	BigDecimal d2 = new BigDecimal(299500);
    	BigDecimal d3 = new BigDecimal(1000);
    	System.out.println(d1.compareTo(d2.add(d3)) <=0);
    	
    }
}
