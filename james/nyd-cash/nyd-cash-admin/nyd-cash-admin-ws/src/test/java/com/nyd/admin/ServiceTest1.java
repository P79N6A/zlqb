package com.nyd.admin;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.Info.OrderExceptionRequst;
import com.nyd.admin.model.dto.BatchCouponDto;
import com.nyd.admin.model.dto.BatchUserDto;
import com.nyd.admin.service.ReturnPremiumService;
import com.nyd.admin.service.WenTongService;
import com.nyd.admin.ws.controller.WenTongController;
import com.nyd.order.model.OrderExceptionInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/nyd/admin/configs/ws/nyd-admin-application.xml"})
//@Env(profile = "classpath:com/nyd/msg/configs/service/xml/nyd-msg-service-local-properties.xml")
public class ServiceTest1 {
    @Autowired
    WenTongService wenTongService;

    @Autowired
    WenTongController wenTongController;

    @Autowired
    private ReturnPremiumService returnPremiumService;



    @Test
    public void testPriory(){
        wenTongService.queryWenTongExcelVo("","","","");
    }



    @Test
    public void testBankAPI(){

    }

    @Test
    public void testExcel(){

    }

    @Test
    public void testBatchCoupons(){
        BatchCouponDto b = new BatchCouponDto();
        b.setUpdateBy("吴朝贤");
        b.setTicketAmount(new BigDecimal(12.6));
        b.setRefundTicketType(1);
        BatchUserDto bt = new BatchUserDto();
        bt.setUserName("陆远");
        bt.setMobile("18094427866");
        bt.setUserId("173542300002");
        bt.setPremiumId("b3af57c89f76467681ea9a2e76e3d18c");
        List<BatchUserDto> list = new ArrayList<>();
        list.add(bt);
        b.setPremiumIdList(list);
        ResponseData responseData = returnPremiumService.batchCoupons(b);
        System.out.println("*********批量发券结果*****" + responseData.getData());
        System.out.println("*********错误*****" + responseData.getMsg());
    }

    public static void main(String[] args) {
    	OrderExceptionRequst re = new OrderExceptionRequst();
    	//re.setAuditStatus("1");
    	List<String> orders = new ArrayList<String>();
    	orders.add("111111");
    	orders.add("2222222");
    	re.setOrderNos(orders);
    	re.setFundCode("dld");
    	re.setOrderStatus(1001);
    	/*re.setEndDate(new Date());
    	re.setStartDate(new Date());*/
    	re.setPageNum(1);
    	re.setPageSize(1);
    	System.out.println(JSON.toJSONString(re));
    	OrderExceptionInfo info = new OrderExceptionInfo();
    	info.setAccountNumber("12312312");
    	info.setAuditStatus(1);
    	info.setBankAccount("3254234234");
    	info.setBankName("3423423");
    	info.setFailTime(new Date());
    	info.setLoanAmount(BigDecimal.ZERO);
    	info.setOrderNo("订单号");
    	info.setOrderStatus(1000);
    	info.setRealName("真实姓名");
    	System.out.println(JSON.toJSONString(info));
    	List<OrderExceptionInfo> a= new ArrayList<OrderExceptionInfo>();
    	a.add(info);
    	ResponseData wa = ResponseData.success();
    	wa.setData(a);
    	System.out.println(JSON.toJSONString(wa));
    	PageInfo page = new PageInfo(a);
    	Integer total = 1;
    	page.setTotal(total);
    	ResponseData da = ResponseData.success();
    	da.setData(page);
    	System.out.println(JSON.toJSONString(da));
    	System.out.println(JSON.toJSONString(da));
    	da.setData(a);
    	System.out.println(JSON.toJSONString(da));
    	
    	
    	
}
}
