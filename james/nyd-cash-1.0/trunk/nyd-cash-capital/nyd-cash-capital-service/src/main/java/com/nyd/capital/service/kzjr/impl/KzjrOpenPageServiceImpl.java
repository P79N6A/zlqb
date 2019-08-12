package com.nyd.capital.service.kzjr.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.model.kzjr.KzjrOpenPageStore;
import com.nyd.capital.model.kzjr.OpenPageInfo;
import com.nyd.capital.model.kzjr.SubmitSmsInfo;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.kzjr.KzjrOpenPageService;
import com.nyd.capital.service.kzjr.config.KzjrConfig;
import com.nyd.capital.service.pocket.util.CrawlerUtil;
import com.nyd.capital.service.utils.HttpsUtils;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.KzjrPageInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Cong Yuxiang
 * 2018/5/9
 **/
@Service
public class KzjrOpenPageServiceImpl implements KzjrOpenPageService {

    public static final String PAGE_REDIS_PREFIX = "kpgr";

    final Logger logger = LoggerFactory.getLogger(KzjrOpenPageServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private KzjrConfig kzjrConfig;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private CrawlerUtil crawlerUtil;

    @Override
    public ResponseData getSmsCode(OpenPageInfo openPageInfo) {

        logger.info("开户获取短信验证码请求信息:" + JSON.toJSONString(openPageInfo));
        if (openPageInfo == null || StringUtils.isBlank(openPageInfo.getUrl()) || StringUtils.isBlank(openPageInfo.getUserId()) || StringUtils.isBlank(openPageInfo.getBankCardNo())) {
            return ResponseData.error("信息为空");
        }

        ResponseData result;

        String url = openPageInfo.getUrl();
        logger.info("请求的url" + url);
        ResponseData<WebDriver> data = crawlerUtil.getUrl(url);
        if (!OpenPageConstant.STATUS_ZERO.equals(data.getStatus())){
            return ResponseData.error();
        }
        WebDriver dr = data.getData();
        try {
            dr.get(url);
            Thread.sleep(2000);
            logger.info(dr.getPageSource());
            WebElement cardNo = dr.findElement(By.id("BIND_CARD_NO"));
//        WebElement mobile = dr.findElement(By.id("MOBILE"));
//        mobile.sendKeys("18521307454");
            cardNo.sendKeys(openPageInfo.getBankCardNo());

            Set<Cookie> cookies = dr.manage().getCookies();
            logger.info(openPageInfo.getUserId() + "cookies为" + JSON.toJSONString(cookies));
            String cookieStr = "";
            String sg = "cookie1-8618=CJADACAKCBKK";
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    cookieStr = cookie.getName() + "=" + cookie.getValue();
                }
                if ("cookie1-8618".equals(cookie.getName())) {
                    sg = cookie.getName() + "=" + cookie.getValue();
                }
//            System.out.println(cookie.getName()+"::::::"+cookie.getValue());
            }
            if (StringUtils.isBlank(cookieStr)) {
                logger.info(url + "获取的cookie为空" + cookieStr);
                if (dr != null) {
                    dr.close();
                }
                return ResponseData.error("获取的cookie为空");
            } else {
                logger.info(url + "获取的cookie为" + cookieStr);
            }
            logger.info("sg:" + sg);

            String pageUrl = dr.getCurrentUrl();

            logger.info("pageUrl为" + pageUrl);
            String seqNo = null;
            try {
                seqNo = pageUrl.split("=")[1];
            } catch (Exception e) {
                logger.info(url + "序列号异常", e);
                if (dr != null) {
                    dr.close();
                }
                return ResponseData.error("序列号异常");
            }

            logger.info("seqNo为" + seqNo);

            KzjrOpenPageStore store = new KzjrOpenPageStore();
            store.setCookie(cookieStr + ";" + sg);
            store.setSeqNo(seqNo);
            store.setBankCardNo(openPageInfo.getBankCardNo());

            redisTemplate.opsForValue().set(PAGE_REDIS_PREFIX + openPageInfo.getUserId(), JSON.toJSONString(store), 100, TimeUnit.MINUTES);

//针对获取验证码的id随机变动而做的选择判断
            WebElement getSms = null;
            try {
                getSms = dr.findElement(By.id("appGetSmsCode"));
            } catch (Exception e) {
                logger.info("did not find appGetSmsCode try another");
                getSms = dr.findElement(By.id("smsBtn"));
            }
            getSms.click();
            TakesScreenshot screenshot = (TakesScreenshot) dr;
            File screenShot = screenshot.getScreenshotAs(OutputType.FILE); //此段停止执行，提示 Connection reset
            FileUtils.copyFile(screenShot, new File("/data/images/" + openPageInfo.getUserId() + ".jpg"));

            result = ResponseData.success("成功");
            result.setCode("2");
            logger.info(openPageInfo.getUserId() + "点击结果成功");

        } catch (Exception e) {
            logger.info(url + "正常异常", e.getMessage());
//            e.printStackTrace();
            result = ResponseData.success("正常异常");
            result.setCode("4");
        } finally {
            if (dr != null) {
                dr.close();
            }
        }
        return result;

    }

    @Override
    public ResponseData submitSmsCode(SubmitSmsInfo submitSmsInfo) {
        ResponseData responseData = ResponseData.success();
        logger.info("提交验证码请求信息:" + JSON.toJSONString(submitSmsInfo));
        if (submitSmsInfo == null || StringUtils.isBlank(submitSmsInfo.getSmsCode()) || StringUtils.isBlank(submitSmsInfo.getUserId())) {
            logger.info(submitSmsInfo.getUserId() + "提交验证码信息为空");
            return ResponseData.error("提交验证码信息为空");
        }
        String redisStr = (String) redisTemplate.opsForValue().get(PAGE_REDIS_PREFIX + submitSmsInfo.getUserId());
        if (StringUtils.isBlank(redisStr)) {
            logger.info(submitSmsInfo.getUserId() + "信息已失效");
            return ResponseData.error("信息已失效");
        }
        KzjrOpenPageStore store = JSONObject.parseObject(redisStr, KzjrOpenPageStore.class);

        logger.info(submitSmsInfo.getUserId() + "从redis得到的数据为" + JSON.toJSONString(store));

        Map<String, String> headMap = new HashMap<>();
        headMap.put("Cookie", store.getCookie());
        headMap.put("Content-Type", "application/json");
        headMap.put("X-Requested-With", "XMLHttpRequest");


        JSONObject object = new JSONObject();
        object.put("SEQNO", store.getSeqNo());
        object.put("SMS_CODE", submitSmsInfo.getSmsCode());
        object.put("BIND_CARD_NO", store.getBankCardNo());

        //dbesbuat
        String wuHuan = null;
        try {
            String result = HttpsUtils.post(kzjrConfig.getBankUrl(), headMap, object);
            logger.info("提交验证码后的结果为:::" + result);
            if (result != null) {
                JSONObject resultObj = JSONObject.parseObject(result);

                if ("00000000".equals(resultObj.getString("RETCODE"))) {
                    wuHuan = "0";
                    return ResponseData.success(wuHuan);
                } else {
                    wuHuan = "1";
                    responseData.setData(wuHuan).setMsg(resultObj.getString("RETMSG"));
                    //return ResponseData.error(resultObj.getString("RETMSG"));
                }
            }else {
                logger.error("submit openPage error:网络不通");
                wuHuan = "1";
                responseData.setData(wuHuan).setMsg("开通银行电子账户失败");
                //return ResponseData.error("开通银行电子账户失败");
            }
        } catch (Exception e) {
            logger.error("submit openPage has exception", e);
            wuHuan = "1";
            responseData.setData(wuHuan).setMsg("开通银行电子账户失败");
            //return ResponseData.error("开通银行电子账户失败");
        }
        if ("1".equals(wuHuan)){
            ResponseData<KzjrPageInfo> kzjrPageInfoResponseData = null;
            try {
                kzjrPageInfoResponseData = orderContract.kzjrPageErrorGenerateOrder(submitSmsInfo.getUserId());
                logger.info("kzjr失败转单" + JSON.toJSONString(kzjrPageInfoResponseData));
            }catch (Exception e){
                logger.error("空中失败后转七彩格子时发生异常");
                return ResponseData.error("空中失败后转七彩格子时发生异常");
            }
        }
            return responseData;
    }
}
