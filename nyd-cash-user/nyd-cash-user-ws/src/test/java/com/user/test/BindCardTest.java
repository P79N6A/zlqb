package com.user.test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.nyd.user.model.RefundAppCountInfo;
import com.nyd.user.model.RefundAppInfo;
import com.nyd.user.model.RefundInfo;
import com.nyd.user.model.UserBindCardReq;
import com.nyd.user.model.vo.RefundAppVo;
import com.nyd.user.service.AccountInfoService;
import com.nyd.user.service.BindCardService;
import com.nyd.user.service.RefundAppCountService;
import com.nyd.user.service.RefundAppService;
import com.nyd.user.service.RefundService;
import com.nyd.user.service.util.MongoApi;
import com.tasfe.framework.redis.RedisService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/user/configs/ws/nyd-user-application.xml"})
@Controller
public class BindCardTest {

    @Autowired
    private BindCardService  bindCardService;
    
    @Autowired
    private AccountInfoService accountInfoservice;
    
    @Autowired
    private RedisService redisService;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private RefundAppService refundAppService;
    
    @Test
    public void test01() {
    	UserBindCardReq req = new UserBindCardReq();
    	req.setBankCode("1111");
    	req.setUserId("173570900004");
    	req.setBankName("1111");
    	req.setCardNo("22222222");
    	req.setIdcardNo("21312312312312");
    	req.setPhone("124321312312");
    	req.setUsername("张三");
    	System.out.println(JSON.toJSONString(req));
    	bindCardService.bindCard(req);
    }
    @Test
    public void test02() {
    	System.out.println("当前时间：" + new Date().getTime());
    }
    @Test
    public void test03() {
    	Set<String> keys = redisTemplate.keys("login:" + "*");
    	redisTemplate.delete(keys);
    }
    @Test
    public void test04() {
    	RefundAppInfo inf = new RefundAppInfo();
    	inf.setUserId("180871900001");
    	try {
			refundAppService.getTaskListByUserId(inf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Autowired
    MongoApi mongoApi;
    @Test
    public void test05() {
    	Map<String,Object> map = new HashMap<String,Object>();
		map.put("refundNo", "21312312312312");
		map.put("appList", "测试测试");
		mongoApi.save(map, "attachment");
		List<RefundAppVo> imge =  mongoApi.getRefundImge("21312312312312");
		System.out.println(JSON.toJSONString(imge));
		Map<String,Object> map2 = new HashMap<String,Object>();
		map2.put("refundNo", "21312312312312");
		map2.put("reason", "你好2");
		System.out.println(JSON.toJSONString(mongoApi.updateRefundImge(map2)));
		List<RefundAppVo> imge3 =  mongoApi.getRefundImge("21312312312312");
		System.out.println(JSON.toJSONString(imge3));
    }
    
    @Autowired
    RefundService refundService;
    @Test
    public void test06() {
    	RefundInfo info = new RefundInfo();
    	info.setPageNum(1);
    	info.setPageSize(1);
    	try {
			refundService.queryRefund(info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    @Autowired
    RefundAppCountService refundAppCountService;
    @Test
    public void test07() {
    	RefundAppCountInfo info = new RefundAppCountInfo();
    	info.setAppCode("1001");
    	info.setCountDate(new Date());
    	info.setUpdateClikCount(1);
    	//info.setUpdateRegisterCount(1);
    	try {
			refundAppCountService.updateCount(info);
			RefundInfo re = new RefundInfo();
			re.setRefundNo("101544274543628001");
			re.setRequestStatus(1001);
			refundService.update(re);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
}
