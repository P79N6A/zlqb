//package com.creativearts.nyd.pay.service.quartz;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.fastjson.JSONObject;
//import com.creativearts.nyd.pay.model.changjie.QueryTradeRequest;
//import com.creativearts.nyd.pay.service.changjie.ChangJieQuickPayService;
//import com.creativearts.nyd.pay.service.changjie.util.JsonUtil;
//import com.nyd.pay.dao.ThirdPartyPaymentChannelDao;
//import com.nyd.pay.entity.ThirdPartyPaymentChannel;
//import com.tasfe.framework.support.model.ResponseData;
//
///**
// * 
// * ClassName: ChangJiejobTask <br/>
// * date: 2018年9月13日 下午6:34:23 <br/>
// *
// * @author wangzhch
// * @version 
// * @since JDK 1.8
// */
//@Component("changJiejobTask")
//public class ChangJiejobTask {
//	private Logger log = LoggerFactory.getLogger(ChangJiejobTask.class);
//	
//	@Autowired
//	private ChangJieQuickPayService changJieQuickPayService;
//	
//	@Autowired
//	private ThirdPartyPaymentChannelDao thirdPartyPaymentChannelDao;
//	
//    public void queryStatus() {
//    	try {
//			List<ThirdPartyPaymentChannel> orders = changJieQuickPayService.queryOrderStatus();
//			
//			if (null == orders || orders.size() < 1) {
//	            log.info("没有可供查询的订单");
//	        }else {
//	        	for (ThirdPartyPaymentChannel thirdPartyPaymentChannel : orders) {
//					try {
//						log.info("正在处理订单:{}",thirdPartyPaymentChannel.getTransId());
//						ResponseData data = changJieQuickPayService.queryTrade(thirdPartyPaymentChannel);
//						if(null == data || !"0".equals(data.getStatus())) {
//							continue;
//						}
//						Map<String, String> map = JsonUtil.objectToMap(data.getData());
//						if(null == map || map.isEmpty() || "F".equals(map.get("acceptStatus")) 
//								|| "P".equals(map.get("status"))) {
//							continue;
//						}
//						if("S".equals(map.get("status"))) {
//							thirdPartyPaymentChannel.setStatus(0);
//						}
//						if("F".equals(map.get("status"))) {
//							thirdPartyPaymentChannel.setStatus(1);
//						}
//						if(StringUtils.isEmpty(thirdPartyPaymentChannel.getThirdPaymentSerialNo()) && !StringUtils.isEmpty(map.get("orderTrxId"))) {
//							thirdPartyPaymentChannel.setThirdPaymentSerialNo(map.get("orderTrxId"));
//						}
//						thirdPartyPaymentChannel.setUpdateTime(new Date());//这里做了区分,如果是异步通知的,修改resTime,造艺主动查询的,修改updateTime
//						thirdPartyPaymentChannelDao.updateThirdPartyOrderStatus(thirdPartyPaymentChannel);
//						log.info("更新订单:{},更新成功",thirdPartyPaymentChannel.getTransId());
//					} catch (Exception e) {
//						log.error("更新订单:{},更新失败,异常信息:{}",thirdPartyPaymentChannel.getTransId(),e);
//					}
//				}
//	        }
//		} catch (Exception e) {
//			log.error("查询处理中的交易失败,异常信息:{}",e);
//		}
//    }
//    public static void main(String[] args) {
//    	Date date = new Date(new Date().getTime() - 30*60*1000);
//    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("");
//    	long time = new Date().getTime() - 30*60*1000;
//    	System.out.println(time);
//		System.out.println(new Date(new Date().getTime() - 30*60*1000));
//		
//		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		long now = System.currentTimeMillis();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(now);
//		System.out.println(now + " = " + formatter.format(calendar.getTime()));
//		calendar.setTimeInMillis(time);
//		System.out.println(time + " = " + formatter.format(calendar.getTime()));
//	}
//}
