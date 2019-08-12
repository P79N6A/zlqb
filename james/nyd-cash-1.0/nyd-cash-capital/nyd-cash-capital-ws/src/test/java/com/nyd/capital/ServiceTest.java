package com.nyd.capital;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.nyd.capital.api.service.JxApi;
import com.nyd.capital.api.service.RemitService;
import com.nyd.capital.entity.KzjrProductConfig;
import com.nyd.capital.entity.UserJx;
import com.nyd.capital.model.jx.*;
import com.nyd.capital.model.kzjr.*;
import com.nyd.capital.model.pocket.*;
import com.nyd.capital.model.qcgz.*;
import com.nyd.capital.model.vo.KzjrOpenAccountVo;
import com.nyd.capital.service.*;

import com.nyd.capital.service.dld.service.DldService;

import com.nyd.capital.service.impl.KdlcFundService;
import com.nyd.capital.service.jx.JxService;
import com.nyd.capital.service.jx.business.job.JxQueryTask;
import com.nyd.capital.service.jx.util.AESUtil;
import com.nyd.capital.service.jx.util.DingdingUtil;
import com.nyd.capital.service.jx.util.PasswordUtil;

import com.nyd.capital.service.kzjr.KzjrService;
import com.nyd.capital.service.pocket.business.Pocket2Business;
import com.nyd.capital.service.pocket.business.impl.PocketHtmlService;
import com.nyd.capital.service.pocket.job.PocketTask;
import com.nyd.capital.service.pocket.service.Pocket2Service;
import com.nyd.capital.service.pocket.service.PocketService;
import com.nyd.capital.service.pocket.util.CrawlerUtil;
import com.nyd.capital.service.qcgz.QcgzService;
import com.nyd.capital.service.qcgz.config.QcgzConfig;
import com.nyd.capital.service.utils.Constants;
import com.nyd.capital.service.validate.ValidateException;
import com.nyd.capital.service.validate.ValidateUtil;

import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.api.OrderContract;

import com.nyd.order.model.CapitalOrderRelationInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.OrderWentongInfo;


import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.tasfe.framework.redis.RedisService;
import com.tasfe.framework.support.model.ResponseData;

import org.apache.commons.lang3.StringUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import java.util.Date;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/nyd/capital/configs/ws/nyd-capital-application.xml"})
//@Env(profile = "classpath:com/nyd/msg/configs/service/xml/nyd-msg-service-local-properties.xml")
public class ServiceTest {
    Logger logger = LoggerFactory.getLogger(ServiceTest.class);
    @Autowired
    ValidateUtil validateUtil;
    @Autowired
    ICacheService cacheService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DldService dldService;
    @Autowired
    private KzjrService kzjrService;
    @Autowired
    private KzjrProductConfigService kzjrProductConfigService;
    @Autowired
    private FailOrderService failOrderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RemitService remitService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private JxQueryTask jxQueryTask;

    @Autowired
    private LogKzjrService logKzjrService;


    @Autowired(required = false)
    private OrderContract orderContract;

    @Autowired(required = false)
    private UserIdentityContract userIdentityContract;

    @Autowired(required = false)
    private UserAccountContract userAccountContract;

    @Autowired(required = false)
    private UserBankContract userBankContract;

    @Autowired(required = false)
    private CapitalOrderRelationContract capitalOrderRelationContract;


//    @Autowired
//    private OrderWentongContract orderWentongContract;

    @Autowired
    private JxService jxService;
    @Autowired
    private UserJxService userJxService;
    @Autowired
    private JxApi jxApi;
    @Autowired
    private Pocket2Service pocket2Service;


    @Autowired(required = false)
    private QcgzService qcgzService;

    @Autowired(required = false)
    private QcgzConfig qcgzConfig;

    @Autowired
    private PocketService pocketService;
    @Autowired
    private Pocket2Business pocket2Business;

    @Test
    public void testPocketResult() {
        PocketQueryOrderWithdrawStatusDto pageDto = new PocketQueryOrderWithdrawStatusDto();
        pageDto.setOutTradeNo("101545729244597001");
        ResponseData<PocketParentResult> responseData = pocket2Service.queryOrderWithdrawStatus(pageDto);
        System.out.println(responseData);
    }
    @Autowired
    PocketHtmlService pocketHtmlService;
    @Test
    public void testPocketOOO() {
//        PocketAccountDetailDto userInfo = new PocketAccountDetailDto();
//        userInfo.setUserId("181571400001");
//        pocket2Business.selectUserOpenDetail(userInfo);
        PocketTermsAuthPageDto pageDto = new PocketTermsAuthPageDto();
        pageDto.setRepayMaxAmt("1000000");
        pageDto.setRepayDeadline("20200111");
        pageDto.setPaymentMaxAmt("1000000");
        pageDto.setIdNumber("430623199107262712");
        pageDto.setPaymentDeadline("20200111");
        pageDto.setIsUrl("1");
        pageDto.setRetUrl("https://www.baidu.com");
        ResponseData<PocketParentResult> responseData = pocket2Service.termsAuthPage(pageDto);
        System.out.println(responseData);
    }

    @Test
    public void testPocketQueryAccount() {
        PocketAccountOpenEncryptPageDto pageDto = new PocketAccountOpenEncryptPageDto();
        pageDto.setName(gbEncoding("刘秋贵"));
        pageDto.setMobile("13018077177");
        pageDto.setGender("M");
        pageDto.setRetUrl("https://www.baidu.com");
//        PocketPasswordResetPageDto pageDto = new PocketPasswordResetPageDto();
//        pageDto.setIdNo("421127199008130818");
//        pageDto.setIsUrl("1");
//        pageDto.setRetUrl("www.baidu.com");
//        PocketQueryAccountOpenDetailByMobileDto dto = new PocketQueryAccountOpenDetailByMobileDto();
//        dto.setMobile("13018077177");
//        PocketComplianceBorrowPageDto mobileDto = new PocketComplianceBorrowPageDto();
//        mobileDto.setBorrowCost("36");
//        mobileDto.setCardNo("6214852123928213");
//        mobileDto.setCounterFee("10000");
//        mobileDto.setIdNumber("421127199008130818");
//        mobileDto.setLoanInterests("100");
//        mobileDto.setLoanMethod("1");
//        mobileDto.setLoanMoney("10000");
//        mobileDto.setLoanPurpose(gbEncoding("还高利贷"));
//        mobileDto.setLoanTerm("1");
//        mobileDto.setName(gbEncoding("刘秋贵"));
//        mobileDto.setNotifyUrl("www.baidu.com");
//        mobileDto.setReturnUrl("www.baidu.com");
//        mobileDto.setOrderId("11111121111");
//        mobileDto.setPhone("13018077177");
//        mobileDto.setPlanRepaymentMoney("1100");
//        mobileDto.setRepayerType("1");
//        ResponseData responseData = pocket2Service.complianceBorrowPage(mobileDto);
//        ResponseData responseData = pocket2Service.queryAccountOpenDetailByMobile(dto);
        ResponseData<PocketParentResult> responseData = pocket2Service.accountOpenEncryptPage(pageDto);
        PocketParentResult data = responseData.getData();
        String retData = data.getRetData();
        JSONObject jsonObject = JSONObject.parseObject(retData);
        String html = jsonObject.getString("html");
        System.out.println(responseData);
    }

    /*
     * 中文转unicode编码
     */
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    @Test
    public void testPocketW() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo("101533106721047001");
        orderInfo.setUserId("182131400011");
//        orderInfo.setOrderNo("101538050178721001");
//        orderInfo.setUserId("182091400001");
        pocketService.withdraw(orderInfo);
    }
    @Autowired
    private KdlcFundService kdlcFundService;

    @Test
    public void testPocketA() throws ValidateException {
        String userId = "181571400001";
        String orderNo = "101543821402364015";
//        List list = kdlcFundService.generateOrders(userId, orderNo, 0);
//        ResponseData sendOrder = kdlcFundService.sendOrder(list);
        PocketHtmlMessage message = new PocketHtmlMessage();
        message.setOrderNo(orderNo);
        message.setUserId(userId);
        pocketHtmlService.confirmOrderHtml(message);
//        System.out.println(sendOrder);
    }

    @Test
    public void testPocketP() throws ValidateException {
        PocketWithdrawDto dto = new PocketWithdrawDto();
        dto.setIsUrl("1");
        dto.setOutTradeNo("101543821402364007");
        dto.setRetUrl("https://www.baidu.com");
        ResponseData<PocketParentResult> withdraw = pocket2Service.withdraw(dto);
        System.out.println(withdraw);
    }
    @Autowired
    private CrawlerUtil crawlerUtil;
    @Test
    public void testPocketPq() throws ValidateException, InterruptedException, IOException {
        ResponseData<WebDriver> url = crawlerUtil.getUrl("http://test.asset.koudailc.com/asset-page/to/account-open-new?key=6e20999d03c99de025223eabfb831713");
        WebDriver driver = url.getData();
        WebElement encPin = driver.findElement(By.id("encPin"));
        //查询用户密码
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buffer = null;
        buffer = decoder.decodeBuffer("MzEzNzcw");
        encPin.sendKeys(new String(buffer));
        WebElement sub = driver.findElement(By.id("sub"));
        sub.click();
        Thread.sleep(6000);
        driver.close();
    }



    @Test
    public void testPocketO() {
//        OrderInfo orderInfo = new OrderInfo();
//        orderInfo.setOrderNo("101533106721047001");
//        orderInfo.setUserId("182131400011");
////        orderInfo.setOrderNo("101538050178721001");
////        orderInfo.setUserId("182091400001");
        PocketQueryOrderWithdrawStatusDto dto = new PocketQueryOrderWithdrawStatusDto();
        dto.setOutTradeNo("101544174203256001");
        ResponseData<PocketParentResult> pocketParentResultResponseData = pocket2Service.queryOrderWithdrawStatus(dto);
        System.out.println(pocketParentResultResponseData);
    }

    @Test
    public void test11() {
        SubmitAssetRequest submitAssetRequest = new SubmitAssetRequest();

        submitAssetRequest.setBidType("1");
        submitAssetRequest.setBankCardNo("6217002870039791536");
        submitAssetRequest.setBankName("中国建设银行");
        submitAssetRequest.setPeriodsType(1);
//        submitAssetRequest.setAmount(new BigDecimal(1000));
        submitAssetRequest.setIdCardNumber("420117199105248734");
        submitAssetRequest.setMobile("18807111634");
        submitAssetRequest.setSex(1);
        submitAssetRequest.setMarriageState(0);
        submitAssetRequest.setName("吏红");
        submitAssetRequest.setPeriods(14);
        submitAssetRequest.setChannelCode("NYD");
//        submitAssetRequest.setRates(new BigDecimal(23.76));
        submitAssetRequest.setOrderId("101537895995716001");
        ResponseData data = qcgzService.assetSubmit(submitAssetRequest);
        if ("0".equals(data.getStatus())) {
            System.out.println(JSON.toJSON(data.getData()));

        }
    }

    @Test
    public void testQ() throws IOException {
        OrderWentongInfo orderWentongInfo = new OrderWentongInfo();
        orderWentongInfo.setLoanTime("qq");
        orderWentongInfo.setLoanTime("2018-05-01 12:12:12");
        orderWentongInfo.setOrderNo("123234234");
        orderWentongInfo.setUserId("2222");
        orderWentongInfo.setMobile("15618612345");
//        orderWentongContract.save(orderWentongInfo);

//    @Autowired
//    private TFailorderKzjrMapper tFailorderKzjrMapper;


//    @Test
//    public void testQ(){
//        OrderWentongInfo orderWentongInfo = new OrderWentongInfo();
//        orderWentongInfo.setLoanTime("qq");
//        orderWentongInfo.setLoanTime("2018-05-01 12:12:12");
//        orderWentongInfo.setOrderNo("123234234");
//        orderWentongInfo.setUserId("2222");
//        orderWentongInfo.setMobile("15618612345");
//        orderWentongContract.save(orderWentongInfo);
//
    }

    @Test
    public void saveLog() {
//        LogKzjr logKzjr = new LogKzjr();
//        logKzjr.setChannel("kzjr");
//        logKzjr.setMobile("15618624753");
//        logKzjr.setStatus(5);
//        logKzjrService.save(logKzjr);
//        TimeRangeVo vo = new TimeRangeVo();
//        vo.setStartDate(new Date());
//        vo.setEndDate(new Date());
//        tFailorderKzjrMapper.queryByTime(vo);
    }

    @Test
    public void testUpadate() {
        userAccountService.test();
//        userAccountService.updateBankAccById(1l);
    }
    @Autowired
    private PocketTask task;
    @Test
    public void testJob() {
        task.withdrawalJob();
    }

    @Test
    public void testTask() {
        jxQueryTask.queryStatusFromJx();
//        userAccountService.updateBankAccById(1l);
    }

    @Test
    public void testOpenPage() {
        KzjrOpenAccountVo vo = new KzjrOpenAccountVo();
        vo.setUserId("1234");
        vo.setName("闫新宇");
        vo.setMobile("15021786051");
        vo.setCardNo("6236682830001540771");
        vo.setIdType(1);
        vo.setIdNo("140426199305010822");
        vo.setReturnUrl("http://www.baidu.com");
        remitService.accountOpenPage(vo);
//        vo.setReturnUrl(kzjrConfig.getReturnUrl()+"?r="+userId);
    }

    @Test
    public void testInsert() {
        TestPo po = new TestPo();
        po.setXm("cyx");
        po.setSjh("15618624753");
        po.setSfz("1234567");
        System.out.println(JSON.toJSONString(po));
        validateUtil.process(po);
        System.out.println(JSON.toJSONString(po));
    }

    @Test
    public void testSer() {
        cacheService.setKey("123");
    }

    @Test
    public void testRestTemplat() {
//        KzjrVo vo = new KzjrVo();
////        vo.setAge(12);
//        vo.setChannelCode("20171211101081528645410816");
//        vo.setName("cyx");
//        vo.setPrivateKey("391ab7be95364f94");
//        Map<String,Object> map = new HashMap<>();
//        map.put("channelCode","20171211101081528645410816");
//        map.put("name","cyx");
////        map.put("age",12);
//        map.put("privateKey","391ab7be95364f94");
//        HttpHeaders headers = new HttpHeaders();
////                MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
////        headers.setContentType(type);
////                headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//                JSONObject jsonObj = JSONObject.fromObject(vo);
//        HttpEntity<Map> formEntity = new HttpEntity<Map>(map, headers);
//        String result =  restTemplate.postForObject("https://xd-stage.kzlicai.com/sign",map,String.class);
//        System.out.println(result);

//        RestTemplate restTemplate=new RestTemplate();
        String url = "http://www.testXXX.com";
        /* 注意：必须 http、https……开头，不然报错，浏览器地址栏不加 http 之类不出错是因为浏览器自动帮你补全了 */
        String bodyValTemplate = "channelCode=20171211101081528645410816&name=cyx&age=12";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(bodyValTemplate, headers);
        ResponseEntity<String> s = restTemplate.exchange("https://xd-stage.kzlicai.com/sign", HttpMethod.POST, entity, String.class);
        System.out.println(JSON.toJSONString(s));
    }


    @Test
    public void testKzjr() {
        /**
         * {"data":{"result":"ok"},"status":0}
         */
        SendSmsRequest request = new SendSmsRequest();
//        request.setChannelCode("20171218103609227830087680");
        request.setBizType(2);
        request.setMobile("18352262263");
        request.setCardNo("6217922200724624");
        System.out.println(kzjrService.sendSmsCode(request).toJSONString());
    }

    @Test
    public void testOpenAccount() throws UnsupportedEncodingException {
        /*
        {"msg":"该身份证号已开户","status":5005}
        {"msg":"短信验证码失效","status":5015}
        {"msg":"前导业务授权码或手机验证码错误","status":5002}
        {"data":{"accountId":"20171213101812221683851264"},"status":0}
         */
        OpenAccountRequest request = new OpenAccountRequest();
        request.setChannelCode("20171211101081528645410816");
        request.setIdType(1);
        request.setIdNo("140106199301019836");
        request.setMobile("11108572659");
        request.setName("李测试账户9");
        request.setSmsCode("111111");
        request.setCardNo("6222620410005461772");

        System.out.println(kzjrService.accountOpen(request));
    }

    /**
     * {"status":0}  {"data":{"accountId":"20180226128908928410181632","idNo":"130102199201011178","mobile":"17621526388","name":"张八","cardNo":"6222620410005461772"},"status":0}
     */
    @Test
    public void testQueryAccount() {
        QueryAccountRequest request = new QueryAccountRequest();
//        request.setChannelCode("20171218103609227830087680");
        request.setIdType(1);
        request.setIdNo("654003197107181880");
        System.out.println(kzjrService.queryAccount(request));
    }

    @Test
    public void testAssetSubmit() {
/*
{"msg":"渠道订单号已存在","status":5012}
      失败  {"msg":"资产期限与产品期限不符合","status":5008}20171214102148769134567424
      {"msg":"今日资产进件金额累计已超过产品日最大金额","status":5011}
      成功 {"data":{"projectCode":"20171212101380514065309696","productRemain":999900.0},"status":0}
*/
        AssetSubmitRequest request = new AssetSubmitRequest();
        request.setChannelCode("20171211101081528645410816");
        request.setAccountId("20171214102147578761732096");
//        request.setProductCode("20171212101373462634053632");
        request.setAmount(new BigDecimal("100.0"));
        request.setOrderId("3284567911");
        request.setType(2);
        request.setDuration(14);

        System.out.println(kzjrService.assetSubmit(request));
    }

    @Test
    public void testProductList() {
/*
        {"data":[{"productCode":"20171212101373462634053632","duration":14,"stepAmount":100.0,"singleMinAmount":100.0,"singleMaxAmount":2000.0,"dayMaxAmount":1000000.0,"dayRemainAmount":1000000.0}],"status":0}
*/
        GetProductRequest request = new GetProductRequest();
        request.setChannelCode("20171211101081528645410816");
        System.out.println(kzjrService.productList(request));
    }

    @Test
    public void testRemitQuery() {
        QueryRemitResult remitResult = new QueryRemitResult();
        remitResult.setChannelCode("20171218103609227830087680");
        remitResult.setOrderId("101523736757244001S2373683490");
        System.out.println(kzjrService.queryMatchResult(remitResult));
    }

    @Test
    public void testRemitQueryBatch() {
        QueryRemitResultBatch batch = new QueryRemitResultBatch();
//        QueryRemitResult remitResult = new QueryRemitResult();
        batch.setChannelCode("20171218103609227830087680");
        batch.setDay("20171221");
        batch.setPageNum(1);
        batch.setPageSize(20);
        System.out.println(kzjrService.queryMatchResultList(batch));
    }

    @Test
    public void testQueryAssetList() {
        /*
        {"data":{"list":[{"orderId":"1234567","productCode":"20171212101373462634053632","amount":100.0,"duration":14,"accountId":"20171211101185844441931776","status":0},{"orderId":"2234567","productCode":"20171212101373462634053632","amount":100.0,"duration":14,"accountId":"20171211101185844441931776","status":0}],"total":2},"status":0}
         */
        QueryAssetListRequest request = new QueryAssetListRequest();
        request.setChannelCode("20171218103609227830087680");
        request.setDay("20180114");
        request.setPageNum(1);
        request.setPageSize(100);
        System.out.println(kzjrService.queryAssetList(request));
    }

    @Test
    public void testQueryAsset() {
        /*
        {"data":{"orderId":"1234567","productCode":"20171212101373462634053632","amount":100.0,"duration":14,"accountId":"20171211101185844441931776","status":0},"status":0}
         */
        QueryAssetRequest request = new QueryAssetRequest();
        request.setChannelCode("20171218103609227830087680");
        request.setOrderId("101525418586122001S2541865870");
        System.out.println(kzjrService.queryAsset(request));
    }

    @Test
    public void test1() throws ParseException {
//        kzjrProductConfigService.update(1L,1);
        kzjrProductConfigService.query(14);
        System.out.println(JSON.toJSONString(kzjrProductConfigService.query(14)));

//        failOrderService.queryByTime(DateUtils.parseDate("2017-12-14 00:00:00","yyyy-MM-dd HH:mm:ss"),null);
    }

    @Test
    public void bindTest() {
        BindCardRequest request = new BindCardRequest();
        request.setMobile("18352262263");
        request.setSmsCode("365754");
        request.setChannelCode("20171218103609227830087680");
        request.setAccountId("20171221104708306806976512");
        request.setBankCardNo("6217001180008353594");
        System.out.println(kzjrService.bindCard(request));

    }

    @Test
    public void unBindTest() {
        UnBindCardRequest request = new UnBindCardRequest();

        request.setChannelCode("20171211101081528645410816");
        request.setAccountId("20171214102189635672891392");
//        request.setBankCardNo("6221882900053647806");
        System.out.println(kzjrService.unBindCard(request));

    }

    @Test
    public void batchAssetTest() {
        BatchAssetSubmitRequest request = new BatchAssetSubmitRequest();
        request.setChannelCode("20171211101081528645410816");
        BatchAssetDetail detail = new BatchAssetDetail();
        detail.setAccountId("20171214102147578761732096");
        detail.setProductCode("20171212101373462634053632");
        detail.setAmount(new BigDecimal("100.0"));
        detail.setOrderId("3284597911");
        detail.setType(1);
        detail.setDuration(14);
        List<BatchAssetDetail> list = new ArrayList<>();
        list.add(detail);
        request.setBatchData(list);
        System.out.println(kzjrService.batchAssetSubmit(request));
    }

    @Test
    public void testService() {
        List<Long> ids = new ArrayList<>();
        ids.add(18l);
        ids.add(19l);
        failOrderService.deleteKzjrByIds(ids);
    }

    @Test
    public void testHaha() {
//        redisTemplate.opsForValue().set("1234","hahah");
//        System.out.println(redisTemplate.hasKey("1234"));
//        System.out.println(redisTemplate.opsForValue().get("1234"));
        double d = 33.00;
        redisTemplate.opsForValue().set("zz", d);
        double remian = (double) redisTemplate.opsForValue().get("zz");
        redisTemplate.opsForValue().set("zz", remian - 10);
        System.out.println(redisTemplate.opsForValue().get("zz"));
        System.out.println(remian);
    }


    @Test
    public void testPriory() {
        KzjrProductConfig config = kzjrProductConfigService.queryByPriority(7, new BigDecimal(500));
        System.out.println(JSON.toJSONString(config));
    }

    @Test
    public void testRest() {
        String geoResult = restTemplate.getForObject("http://gc.ditu.aliyun.com/geocoding?a={address}", String.class, "苏州市");
        System.out.println(geoResult);
    }

    @Test

    public void testSubmitAsset() {
        SubmitAssetRequest request = new SubmitAssetRequest();
        request.setChannelCode("NYD");
        request.setOrderId("2018072320180727");
        request.setName("李四");
        request.setBidType("1");
        request.setSex(1);
        request.setMobile("18214667879");
        request.setIdCardNumber("34082619911191816");
        request.setBankName("中国建设银行");
        request.setBankCardNo("6217002870039269459");
        request.setPeriods(14);
        request.setPeriodsType(1);
        /*request.setAmount(new BigDecimal(1000).toPlainString());
        request.setRates(new BigDecimal(20).toPlainString());*/
        ResponseData responseData = qcgzService.assetSubmit(request);
        if ("0".equals(responseData.getStatus())) {
            SubmitAssetResponse.Datas data = (SubmitAssetResponse.Datas) responseData.getData();
            System.out.println(JSON.toJSON(data));
            String assetId = data.getAssetId();
            System.out.println(assetId);
        }
    }


    @Test
    public void testSubmitLoanApply() {
        LoanApplyRequest request = new LoanApplyRequest();
        request.setChannelCode("NYD");
        request.setAssetId("463");
        request.setBankName("中国建设银行");
        request.setBankCardNo("6217002870039269459");
        ResponseData responseData = qcgzService.submitLoanApply(request);
        if ("0".equals(responseData.getStatus())) {
            Object data = responseData.getData();
            System.out.println(JSON.toJSON(data));
        }
    }

    @Test
    public void testQueryLoanApplyResult() {
        QueryLoanApplyResultRequest request = new QueryLoanApplyResultRequest();
        request.setChannelCode("NYD");
        request.setAssetId("459");
        ResponseData responseData = qcgzService.queryLoanApplyResult(request);
        if ("0".equals(responseData.getStatus())) {
            Object data = responseData.getData();
            System.out.println(JSON.toJSON(data));
        }
    }

    @Test
    public void testNotify() {
        LoanSuccessNotifyRequest request = new LoanSuccessNotifyRequest();
        request.setChannelCode("NYD");
        request.setAssetId("12345");
        request.setLoanTime("2018-07-25 10:25:00");
//        request.setLoanResult(0);
        request.setSign("asdf123wsd");
        String result = JSON.toJSONString(request);
        LoanSuccessNotifyRequest object = JSON.parseObject(result, LoanSuccessNotifyRequest.class);
        System.out.println("object:" + JSON.toJSON(object));
    }

    @Test
    public void testA() {
        String userId = "181931400001";
        String orderNo = "101531382690222001";
        Integer channel = 1;
        List list = new ArrayList();
        try {

            OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
            logger.info("订单信息:" + JSON.toJSON(orderInfo));

            UserInfo userInfo = null;
            ResponseData<UserInfo> responseData = userIdentityContract.getUserInfo(userId);
            if ("0".equals(responseData.getStatus())) {
                userInfo = responseData.getData();
                logger.info("userId用户身份证信息:" + JSON.toJSON(userInfo));
            }

            AccountDto accountDto = null;
            ResponseData<AccountDto> data = userAccountContract.getAccount(userId);
            if ("0".equals(data.getStatus())) {
                accountDto = data.getData();
                logger.info("userId用户账号信息:" + JSON.toJSON(userInfo));
            }

            BankInfo bankInfo = null;
            ResponseData<List<BankInfo>> bankInfos = userBankContract.getBankInfos(userId);
            if ("0".equals(data.getStatus())) {
                List<BankInfo> bankInfoList = bankInfos.getData();
                bankInfo = bankInfoList.get(0);
            }

            int loopCount = 10;
            while (orderInfo == null && loopCount > 0) { // 防止dubbo出现错误
                orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
                loopCount--;
            }
            if (orderInfo == null) {
                logger.error("userId:" + userId + "orderNo:" + orderNo + "orderInfo为null");
            }

            SubmitAssetRequest request = new SubmitAssetRequest();
            //渠道号
            request.setChannelCode(qcgzConfig.getChannelCode());
            //侬要贷单号
            request.setOrderId(orderNo);

            //姓名
            if (StringUtils.isNotBlank(userInfo.getRealName())) {
                request.setName(userInfo.getRealName());
            }

            // 性别   1 :表示男性    2: 表示女性
            if (StringUtils.isNotBlank(userInfo.getGender())) {
                if ("男".equals(userInfo.getGender())) {
                    request.setSex(1);
                } else if ("女".equals(userInfo.getGender())) {
                    request.setSex(2);
                }
            }

            //手机号
            if (StringUtils.isNotBlank(accountDto.getAccountNumber())) {
                request.setMobile(accountDto.getAccountNumber());
            }

            //身份证号
            if (StringUtils.isNotBlank(userInfo.getIdNumber())) {
                request.setIdCardNumber(userInfo.getIdNumber());
            }

            //银行名称
            if (StringUtils.isNotBlank(bankInfo.getBankName())) {
                request.setBankName(bankInfo.getBankName());
            }

            //银行卡号
            if (StringUtils.isNotBlank(bankInfo.getBankAccount())) {
                request.setBankCardNo(bankInfo.getBankAccount());
            }

            //借款期限
            request.setPeriods(orderInfo.getBorrowTime());

            //期限类型
            request.setPeriodsType(orderInfo.getBorrowPeriods());

            //借款金额
            if (orderInfo.getLoanAmount() != null) {
//                request.setAmount(orderInfo.getLoanAmount().toPlainString());
            }

            //借款利率
            if (orderInfo.getAnnualizedRate() != null) {
//                request.setRates(orderInfo.getAnnualizedRate());
            }

            list.add(request);
            logger.info("list:" + JSON.toJSON(list));
        } catch (Exception e) {
            logger.error("generateOrders has error", e);
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++) {
            SubmitAssetRequest request = (SubmitAssetRequest) list.get(i);
            logger.info("推送资产为:" + JSON.toJSONString(request));
            try {
                //资产提交
                String assetId = "";
                ResponseData responseData = qcgzService.assetSubmit(request);
                if ("0".equals(responseData.getStatus())) {
                    SubmitAssetResponse.Datas data = (SubmitAssetResponse.Datas) responseData.getData();
                    assetId = data.getAssetId();
                    logger.info("推送资产成功assetId:" + assetId);

                    CapitalOrderRelationInfo capitalOrderRelationInfo = new CapitalOrderRelationInfo();
                    capitalOrderRelationInfo.setAssetId(assetId);
                    capitalOrderRelationInfo.setChannelCode("qcgz");
                    capitalOrderRelationInfo.setOrderNo(request.getOrderId());
                    capitalOrderRelationInfo.setState(2);
                    capitalOrderRelationContract.saveCapitalOrderRelation(capitalOrderRelationInfo);
                    logger.info("save capitalOrderRelationInfo success");
                }

            } catch (Exception e) {
                logger.error("推送资产到七彩格子出错啦!", e);
            }
        }


    }

    @Test
    public void testB() {
        Map<String, String> relationMap = Constants.relationMap;
        String s = relationMap.get("父母");
        System.out.println(s);
        System.out.println(Constants.WSM);
    }


    /**
     * 测试客户信息上送
     */
    @Test
    public void testDld() {
        dldService.registerUserByUserId("182141600001", "11182141400008");
    }

    /**
     * 测试dld 借款
     */
    //注册1809143772100011
    @Test
    public void testLoan() {
        dldService.loanSubmitByOrderNo("181571400001", "101535545784650001", "1809176266400011");
    }

    /**
     * 测试获取合同下载地址
     */
    @Test
    public void testLoanContractUrl() {
        dldService.getContractDownloadUrlByOrderNo("20180918104321644");
    }

    /**
     * 测试查询订单详情
     */
    @Test
    public void testLoanQuery() {
        dldService.loanOrderQueryByOrderNo("20180918104321644");
    }


    public void testjxFiveComprehensive() {
        Gson gson = new Gson();
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = df.format(date);
        String aa = str.substring(0, 10);
        String bb = str.substring(11, 19);


        JxFiveComprehensiveRequest request = new JxFiveComprehensiveRequest();
        request.setVersion("10");
        request.setTxCode("brokers/members/authorizedDelegations/v2");
        request.setInstCode("5004");
        request.setChannel("zwgt");
        request.setTxDate(aa);
        request.setTxTime(bb);
        request.setSeqNo("111111");
        request.setAcqRes("acqres");
        //request.setMobile("17621169600");
        request.setMobile("15002709239");
        //request.setRealName("吴欢");
        request.setRealName("刘聪");
        //request.setIdCardNumber("360430198909012520");
        request.setIdCardNumber("421127199210045027");
        request.setReturnUrl("203.156.231.16:39300/nyd/capital/jx/callback");
        //request.setBankCardNumber("6217001210025524461");
        request.setBankCardNumber("6230522320001858073");
        ResponseData responseData = jxService.jxFiveComprehensive(request);
        if ("0".equals(responseData.getStatus())) {
            JxFiveComprehensiveResponse jxFiveComprehensiveResponse = (JxFiveComprehensiveResponse) responseData.getData();
            System.out.println(JSON.toJSON(jxFiveComprehensiveResponse));
        }

    }

    @Test
    public void testjxOpenStage() {
        JxQueryPushStatusRequest jxQueryPushStatusRequest = new JxQueryPushStatusRequest();
        jxQueryPushStatusRequest.setBankCardNumber("6212261202031872709");
        jxQueryPushStatusRequest.setIdCardNumber("320826199002086411");
        jxQueryPushStatusRequest.setMobile("18072963167");
        jxQueryPushStatusRequest.setRealName("张鹏");
        ResponseData responseData = jxService.queryPushStatus(jxQueryPushStatusRequest);
        System.out.println(responseData);
    }

    @Test
    public void testjxPushAudite() {
        JxPushAuditRequest jxPushAuditRequest = new JxPushAuditRequest();
        //jxPushAuditRequest.setAddress();
        ResponseData responseData = jxService.pushAudit(jxPushAuditRequest);
        System.out.println(responseData);
    }

    @Test
    public void testqueryPushAuditResult() {
        JxQueryPushAuditResultRequest jxQueryPushAuditResultRequest = new JxQueryPushAuditResultRequest();
        jxQueryPushAuditResultRequest.setLoanOrderId(5185L);
        ResponseData responseData = jxService.queryPushAuditResult(jxQueryPushAuditResultRequest);
        System.out.println(responseData);
    }

    @Test
    public void testqueryPushAuditConfirm() {
        JxPushAuditConfirmRequest jxPushAuditConfirmRequest = new JxPushAuditConfirmRequest();
        jxPushAuditConfirmRequest.setOutOrderId("13018077177");
        jxPushAuditConfirmRequest.setLoanOrderId(5185L);
        ResponseData responseData2 = jxService.pushAuditConfirm(jxPushAuditConfirmRequest);
        System.out.println(responseData2);
    }

    @Test
    public void testqueryResult() {
        JxLoanQueryRequest jxLoanQueryRequest = new JxLoanQueryRequest();
        jxLoanQueryRequest.setLoanId(String.valueOf(101441L));
        ResponseData responseData = jxService.jxLoanQuery(jxLoanQueryRequest);
        System.out.println(responseData);
    }

    @Test
    public void testWithDraw() {
       /* JxWithDrawRequest jxWithDrawRequest = new JxWithDrawRequest();
        jxWithDrawRequest.setMemberId("20180802232814596710");
        jxWithDrawRequest.setAmount(new BigDecimal(1000));
        jxWithDrawRequest.setLoanId(String.valueOf(101441L));
        ResponseData data = jxService.jxWithDraw(jxWithDrawRequest);
        JxWithDrawResponse jxWithDrawResponse = (JxWithDrawResponse)data.getData();
        String url = jxWithDrawResponse.getUrl();
        System.out.println(url);*/
    }

    @Test
    public void testWith() {
        ResponseData userJxByUserId = jxApi.getUserJxByUserId("181312000001");
        System.out.println(userJxByUserId);
    }

    @Test
    public void testSave() throws IOException {
        String password = PasswordUtil.getPassword();
        byte[] encrypt = AESUtil.encrypt(password, "zykj");
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Res = encoder.encode(encrypt);
        //存进数据库
        UserJx userJx = new UserJx();
        userJx.setPassword(base64Res);
        userJx.setMobile("13916415399");
        userJx.setUserId("181651800001");
        userJxService.savePassword(userJx);
        String bytes = userJxService.selectPasswordByUserId("181651800001");
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buffer = decoder.decodeBuffer(bytes);
        byte[] decrypt = AESUtil.decrypt(buffer, "zykj");
        System.out.println(new String(decrypt));
    }

    @Test
    public void testDingDing() throws IOException {
        DingdingUtil.getErrMsg("测试一下");
    }
    
    @Autowired
    private CapitalService capitalService;
    @Test
    public void test1110() {
//    	try {
//			capitalService.reSendCapitalAfterRiskByOrderNo("101543932538422001");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    }
    


}
