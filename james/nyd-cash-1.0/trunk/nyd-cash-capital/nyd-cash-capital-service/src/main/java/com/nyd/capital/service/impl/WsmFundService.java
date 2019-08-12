package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nyd.capital.model.*;
import com.nyd.capital.model.enums.RemitStatus;
import com.nyd.capital.service.FundService;
import com.nyd.capital.service.ICacheService;
import com.nyd.capital.service.utils.Constants;
import com.nyd.capital.service.validate.*;
import com.nyd.capital.service.wsm.WsmBase;
import com.nyd.capital.service.wsm.WsmConfig;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.user.api.UserContactContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.api.UserJobContract;
import com.nyd.user.model.ContactInfos;
import com.nyd.user.model.JobInfo;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 微神马
 * Cong Yuxiang
 * 2017/11/13
 **/
@Service
public class WsmFundService implements FundService{
    Logger logger = LoggerFactory.getLogger(WsmFundService.class);
    @Autowired
    private WsmBase wsmBase;
    @Autowired
    private WsmConfig wsmConfig;
    @Autowired
    private ValidateUtil validateUtil;
    @Autowired
    private ICacheService cacheService;


//    @Autowired(required = false)
//    private RemitContract remitContract;

    @Autowired(required = false)
    private OrderContract orderContract;

    @Autowired(required = false)
    private OrderDetailContract orderDetailContract;

    @Autowired
    private UserContactContract userContactContract;

    @Autowired
    private UserJobContract userJobContract;

    @Autowired
    private UserIdentityContract userIdentityContract;

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    @Override
    public ResponseData sendOrder(List orderList) {
        try {
            wsmBase.initHttpsURLConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error();
        }
        String resultStr = "";
        System.out.println("=============================");
        Iterator<WsmRequest> ite=orderList.iterator();
        while(ite.hasNext())//判断下一个元素之后有值
        {

            WsmRequest request = ite.next();
            resultStr=resultStr+"******"+"商户订单号:"+request.getShddh();
            resultStr=resultStr+"******"+"商户号(mid):"+request.getMid();
            resultStr=resultStr+"******"+"商户商品编号(wsm提供):"+request.getCpm();
            String orderData = JSON.toJSONString(request, SerializerFeature.WriteNullStringAsEmpty);
            System.out.println(orderData);
            // 发起请求
            String result = wsmBase.post(wsmConfig.getSendOrderUrl(), orderData);
            String errorMsg = "";
            resultStr=resultStr+"******"+"提交订单返回结果:"+result;
            System.out.println(resultStr);
            if(result.equals("error")){

                return ResponseData.error();
            }
            else if(result.equals("success")){

                return ResponseData.success();
            }

        }

        return ResponseData.error();
    }

    @Override
    public boolean saveLoanResult(String result) {
        try {
            String s = URLDecoder.decode(result,"UTF-8");
            System.out.println(s);
            WsmResponse wmsCallBack = JSON.parseObject(s, WsmResponse.class);
            cacheService.deleteKey(Constants.WSM+wmsCallBack.getShddh());

            System.out.println("回调");
            System.out.println(JSON.toJSONString(wmsCallBack));
            RemitInfo remitInfo = new RemitInfo();
            if(wmsCallBack.getState().equals("success")){
                //待完善
                OrderInfo orderInfo = orderContract.getOrderByOrderNo(wmsCallBack.getShddh()).getData();
                RemitMessage remitMessage = new RemitMessage();
                remitMessage.setRemitStatus("0");
                remitMessage.setOrderNo(wmsCallBack.getShddh());

                remitMessage.setRemitAmount(orderInfo.getLoanAmount());
                rabbitmqProducerProxy.convertAndSend("remit.nyd",remitMessage);

                remitInfo.setOrderNo(wmsCallBack.getShddh());
                remitInfo.setRemitStatus("0");
                remitInfo.setContractLink(wmsCallBack.getContract_link());
                remitInfo.setRemitBankName(wmsCallBack.getBank());
                remitInfo.setRemitProcedureFee(wmsCallBack.getService_cost());
                remitInfo.setRemitTime(DateUtils.parseDate(wmsCallBack.getPay_time(),"yyyy-MM-dd HH:mm:ss"));
//                remitContract.save(remitInfo);
                return true;
            }else {
                //待完善
                remitInfo.setOrderNo(wmsCallBack.getShddh());
                remitInfo.setRemitStatus("1");
//                remitInfo.setContractLink(wmsCallBack.getContract_link());
//                remitInfo.setRemitBankName(wmsCallBack.getBank());
                remitInfo.setErrorCode(wmsCallBack.getErrorcode());
//                remitInfo.setRemitProcedureFee(wmsCallBack.getService_cost());
//                remitInfo.setRemitTime(DateUtils.parseDate(wmsCallBack.getPay_time(),"yyyy-MM-dd HH:mm:ss"));
//                remitContract.save(remitInfo);
                RemitMessage remitMessage = new RemitMessage();
                remitMessage.setRemitStatus("1");
                remitMessage.setOrderNo(wmsCallBack.getShddh());

                rabbitmqProducerProxy.convertAndSend("pay.nyd",remitMessage);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
           return false;
        }
    }

    @Override
    public String queryOrderInfo(WsmQuery query) {
        try {
            wsmBase.initHttpsURLConnection();
        } catch (Exception e) {
            e.printStackTrace();
           return "证书未通过";
        }
        System.out.println("==============查询订单===============");

        String shddhData = JSON.toJSONString(query.getShddhList());
//			String mid = mid;
        shddhData = shddhData+"&mid="+query.getMid();
        String orderQueryResault = wsmBase.postQuery(wsmConfig.getCheckOrderUrl(), shddhData);

        System.out.println("--------------------------");
        System.out.println(orderQueryResault);
        System.out.println("--------------------------");

        JSONArray orderQueryList = JSONArray.parseArray(orderQueryResault); // 首先把字符串转成 JSONArray  对象
        if(orderQueryList.size()>0){
            for(int i=0;i<orderQueryList.size();i++){

                JSONObject job = orderQueryList.getJSONObject(i);  		// 遍历 jsonarray 数组，把每一个对象转成 json 对象
                String state = job.get("state").toString();
                String errorcode = job.get("errorcode").toString();
                if(state.equals("error")){
                    //频率将额外可重新查询
                    if(errorcode.equals("1028")){
                        System.out.println("订单查询过频，限制每5秒查询一次，每次可查询多个订单号，稍后再继续查询。shddh："+job.get("shddh")) ;
                    }
                    //订单在处理中，可稍后再重新查询
                    else if(errorcode.equals("1023")){
                        System.out.println("订单处理中，稍后再继续查询。shddh："+job.get("shddh")) ;
                    }
                    //订单不存在
                    else if(errorcode.equals("1022")){
                        System.out.println("订单不存在。shddh："+job.get("shddh")) ;
                    }
                    //其他状况，订单出现问题，对应code码查看错误原因
                    else{
                        System.out.println("订单出错，不需要再次查询了，查了也是同样的结果，检查错误信息修改订单吧。shddh："+job.get("shddh")) ;
                    }
                }
            }
        }
        return orderQueryResault;
    }

    @Override
    public List generateOrdersTest() {
        List list = new ArrayList();
        WsmRequest request = new WsmRequest();
        request.setMid(wsmConfig.getMid());
        request.setShmyc(wsmConfig.getShmyc());
        request.setCpm(wsmConfig.getCpm());
        request.setTimestamp(wsmBase.getSecondTimestamp());
        request.setFkxx(getFkxxTest());


        /**=============支付明细=================**/
        List zfmxList = new ArrayList();
        WsmZfmxPublic wsmZfmxPublic = new WsmZfmxPublic();
        wsmZfmxPublic.setKhhbh("1001");
        wsmZfmxPublic.setSheng("辽宁");
        wsmZfmxPublic.setShi("大连");
        wsmZfmxPublic.setValue("10");
        wsmZfmxPublic.setJjfwf("1");
        wsmZfmxPublic.setYbdk("1");
        wsmZfmxPublic.setXm("测试金融");
        wsmZfmxPublic.setZhmc("中国银行星海支行");
        wsmZfmxPublic.setYhkh("6222620410005461772");
        validateUtil.process(wsmZfmxPublic);
        WsmZfmxPrivate wsmZfmxPrivate = new WsmZfmxPrivate();
        wsmZfmxPrivate.setKhhbh("1005");
        wsmZfmxPrivate.setValue("50000");
        wsmZfmxPrivate.setXm("测试");
        wsmZfmxPrivate.setYhkh("6222620410005461772");
        wsmZfmxPrivate.setSfzh("210211199809214379");
        wsmZfmxPrivate.setJjfwf("2");
        wsmZfmxPrivate.setYbdk("2");
        validateUtil.process(wsmZfmxPrivate);
        zfmxList.add(wsmZfmxPublic);
        zfmxList.add(wsmZfmxPrivate);
        /**=====================================**/


        request.setZfmx(JSON.toJSONString(zfmxList));
        request.setXm("测试数据");
        request.setSfzh("210211198703177415");
        request.setShddh(CreateShddhByUUId());
        request.setSflx("1");
        request.setSjh("13898478285");

        request.setDzxx("重庆重庆市九龙坡区杨家坪团结路11号6-1");
        request.setZgxl("1");
        request.setJkzk("1");
        request.setHyzk("1");
        request.setGsmc("造艺科技有限公司");
        request.setGsdh("0411-86603893");
        request.setYhklx("1");
        request.setKh("6228480402564890018");
        request.setKhh("1005");
        request.setSqje(50010);
        request.setDkyt("1");
        request.setQx(1);
        request.setOn_line("1");
        request.setLxrxm1("测试");
        request.setLxrdh1("17184033038");
        request.setLxrgx1("1");
        request.setQyszsheng("辽宁");
        request.setQyszshi("大连");
        request.setTs(30);
        request.setCasqxyqysj("2017-10-10 12:00:00");
        validateUtil.process(request);
        try {
            request.setSign(wsmBase.generateSendSign(request));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        cacheService.setKey(Constants.WSM+request.getShddh(),"1");
        list.add(request);
        return list;
    }

    @Override
    public List generateOrders(String userId,String orderNo,Integer channel) throws ValidateException {
        long start = System.currentTimeMillis();
        OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
        OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
        ContactInfos contactInfos = userContactContract.getContactInfo(userId).getData();
        UserInfo userInfo = userIdentityContract.getUserInfo(userId).getData();
        JobInfo jobInfo = userJobContract.getJobInfo(userId).getData();

        UserDetailInfo userDetailInfo = userIdentityContract.getUserDetailInfo(userId).getData();
        logger.info("######dubbo耗费时间:"+(System.currentTimeMillis()-start));

        ValidatorUtils.validateObject(orderDetailInfo, new OrderDetailInfoValidator());
        ValidatorUtils.validateObject(orderInfo, new OrderInfoValidator());
        ValidatorUtils.validateObject(contactInfos, new ContactInfosValidator());
        ValidatorUtils.validateObject(userInfo, new UserInfoValidator());
        ValidatorUtils.validateObject(jobInfo, new JobinfoValidator());
        ValidatorUtils.validateObject(userDetailInfo, new UserDetailInfoValidator());


        List list = new ArrayList();
        WsmRequest request = new WsmRequest();
        request.setMid(wsmConfig.getMid());
        request.setShmyc(wsmConfig.getShmyc());
        request.setCpm(wsmConfig.getCpm());
        request.setTimestamp(wsmBase.getSecondTimestamp());
        request.setFkxx(getFkxx());

        /*=============支付明细=================*/
        List zfmxList = new ArrayList();

        WsmZfmxPrivate wsmZfmxPrivate = new WsmZfmxPrivate();
        String bh = Constants.getBh(orderInfo.getBankName());
        if(bh==null){
            logger.error("没法获取银行编号"+orderInfo.getBankName());
            throw new RuntimeException("没法获取银行编号"+orderInfo.getBankName());
        }
        wsmZfmxPrivate.setKhhbh(bh);
        wsmZfmxPrivate.setValue(orderInfo.getLoanAmount().intValue()*100+"");
        wsmZfmxPrivate.setXm(userInfo.getRealName());
        wsmZfmxPrivate.setYhkh(orderInfo.getBankAccount());
        wsmZfmxPrivate.setSfzh(userInfo.getIdNumber());
        wsmZfmxPrivate.setJjfwf("2");
        wsmZfmxPrivate.setYbdk("2");
        validateUtil.process(wsmZfmxPrivate);
//        zfmxList.add(wsmZfmxPublic);
        zfmxList.add(wsmZfmxPrivate);
        /*=====================================*/
        request.setZfmx(JSON.toJSONString(zfmxList));
        request.setXm(userInfo.getRealName());
        request.setSfzh(userInfo.getIdNumber());
        request.setShddh(orderNo);
        request.setSflx("1");
        request.setSjh(orderDetailInfo.getMobile());

        request.setDzxx(userDetailInfo.getIdAddress());
        request.setZgxl(Constants.eduMap.get(userDetailInfo.getHighestDegree())==null?"10":Constants.eduMap.get(userDetailInfo.getHighestDegree()));
        request.setJkzk("1");
        request.setHyzk(Constants.maritalMap.get(userDetailInfo.getMaritalStatus())==null?"1":Constants.maritalMap.get(userDetailInfo.getMaritalStatus()));
        request.setGsmc(jobInfo.getCompany());
        request.setGsdh("00000000");
        request.setJjfwxyqysj("2017-12-06 13:14:15");
        request.setJjfwxyckdz("http://58.222.45.48/file3.data.weipan.cn/40903281/584cbbd8d3ffa45c694f929905c530fcf12c9ed2?ip=1512542492,203.156.237.77&ssig=x0goDWlVLB&Expires=1512543092&KID=sae,l30zoo1wmz&fn=%E5%90%88%E5%90%8C%E8%8C%83%E6%9C%AC%E5%A4%A7%E5%85%A8.doc&skiprd=2&se_ip_debug=203.156.237.77&corp=2&from=1221134&wsrid_tag=5a278c6d_jf4_30555-1233&wsiphost=local");
        request.setCasqxyqysj("2017-12-06 13:14:15");
        request.setCasqxyckdz("http://58.222.45.48/file3.data.weipan.cn/40903281/584cbbd8d3ffa45c694f929905c530fcf12c9ed2?ip=1512542492,203.156.237.77&ssig=x0goDWlVLB&Expires=1512543092&KID=sae,l30zoo1wmz&fn=%E5%90%88%E5%90%8C%E8%8C%83%E6%9C%AC%E5%A4%A7%E5%85%A8.doc&skiprd=2&se_ip_debug=203.156.237.77&corp=2&from=1221134&wsrid_tag=5a278c6d_jf4_30555-1233&wsiphost=local");
        request.setDkxyckdz("http://58.222.45.48/file3.data.weipan.cn/40903281/584cbbd8d3ffa45c694f929905c530fcf12c9ed2?ip=1512542492,203.156.237.77&ssig=x0goDWlVLB&Expires=1512543092&KID=sae,l30zoo1wmz&fn=%E5%90%88%E5%90%8C%E8%8C%83%E6%9C%AC%E5%A4%A7%E5%85%A8.doc&skiprd=2&se_ip_debug=203.156.237.77&corp=2&from=1221134&wsrid_tag=5a278c6d_jf4_30555-1233&wsiphost=local");
        request.setDkxyqysj("2017-12-06 13:14:15");
        request.setYhklx("1");
        request.setKh(orderInfo.getBankAccount());
        request.setKhh(Constants.getBh(orderInfo.getBankName()));
        request.setSqje(orderInfo.getLoanAmount().intValue()*100);
        request.setDkyt("1");
        request.setQx(1);
        request.setOn_line("1");
        request.setLxrxm1(contactInfos.getDirectContactName());
        request.setLxrdh1(contactInfos.getDirectContactMobile());
//        request.setLxrgx1(contactInfos.getDirectContactRelation());//确定 关系是什么
        request.setLxrgx1(Constants.relationMap.get(contactInfos.getDirectContactRelation()));
        request.setQyszsheng(userDetailInfo.getLivingProvince());
        request.setQyszshi(userDetailInfo.getLivingCity());
        request.setTs(orderInfo.getBorrowTime());
//        request.setCasqxyqysj("2017-10-10 12:00:00");
        validateUtil.process(request);
        try {
            request.setSign(wsmBase.generateSendSign(request));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(request));
//        cacheService.setKey(Constants.WSM+request.getShddh(),"1");
        list.add(request);
        return list;
    }

    public String getFkxx(){
        Map rowData = new LinkedHashMap();
        rowData.put("地址类别", "2");
        rowData.put("设备MAC","");
        rowData.put("坐标经纬度","");
        rowData.put("社交账号","");
        rowData.put("社交账号类型","");
        rowData.put("月化利率","");
        rowData.put("地址信息是否校验","0");
        rowData.put("身份证多项信息是否验证","1");
        rowData.put("身份证多项信息验证渠道","中城信源");
        rowData.put("工作信息是否验证","0");
        rowData.put("工作信息验证渠道","");
        rowData.put("学历/学籍信息是否验证","1");
        rowData.put("学历/学籍信息验证渠道","神州融");
        rowData.put("联系人信息是否验证","1");
        rowData.put("联系人信息验证渠道","集奥");
        rowData.put("通讯录是否读取","1");
        rowData.put("是否获取电信运营商数据","1");
        rowData.put("是否获取电商数据","1");
        rowData.put("是否获取社保数据","0");
        rowData.put("是否获取公积金数据","0");
        rowData.put("身份证号、姓名、银行卡号一致性验证","1");
        rowData.put("身份证号、姓名、银行卡号一致性验证渠道","神州融");
        rowData.put("网贷黑名单是否命中","0");
        rowData.put("网贷黑名单验证渠道","前海");
        rowData.put("法院黑名单是否命中","0");
        rowData.put("法院黑名单验证渠道","法海");
        rowData.put("公安信息黑名单是否命中","2");
        rowData.put("公安信息黑名单验证渠道","");
        rowData.put("第三方支付交易编号","");
        return JSON.toJSONString(rowData);
    }
    public String getFkxxTest(){
        Map rowData = new LinkedHashMap();
        rowData.put("姓名", "测试");
        rowData.put("性别","男");
        return JSON.toJSONString(rowData);
    }
    //创建商户订单号,测试使用
    public static String CreateShddhByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }
}
