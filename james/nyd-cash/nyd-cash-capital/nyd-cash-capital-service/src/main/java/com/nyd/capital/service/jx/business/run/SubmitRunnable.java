package com.nyd.capital.service.jx.business.run;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.entity.UserJx;
import com.nyd.capital.model.jx.OpenJxHtmlRequest;
import com.nyd.capital.service.UserJxService;
import com.nyd.capital.service.jx.config.JxConfig;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.jx.util.PasswordUtil;
import com.nyd.capital.service.pocket.util.CrawlerUtil;
import com.tasfe.framework.support.model.ResponseData;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author liuqiu
 */
public class SubmitRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SubmitRunnable.class);
    private boolean flag;
    private OpenJxHtmlRequest request;
    private String url;
    private String stage;
    private UserJxService userJxService;
    private JxConfig jxConfig;
    private RedisTemplate redisTemplate;
    private CrawlerUtil crawlerUtil;

    public SubmitRunnable(boolean flag, OpenJxHtmlRequest request, String url, String stage, UserJxService userJxService, JxConfig jxConfig, RedisTemplate redisTemplate,CrawlerUtil crawlerUtil) {
        this.flag = flag;
        this.request = request;
        this.url = url;
        this.stage = stage;
        this.userJxService = userJxService;
        this.jxConfig = jxConfig;
        this.redisTemplate = redisTemplate;
        this.crawlerUtil = crawlerUtil;
    }


    private ResponseData accredit(WebDriver dr, OpenJxHtmlRequest request) throws Throwable {
        logger.info("爬虫设置开通签章" + JSON.toJSONString(request));
        WebElement law_state = dr.findElement(By.id("law_state"));
        Thread.sleep(1000);
        law_state.click();
        Thread.sleep(2000);
        WebElement toLaw = dr.findElement(By.id("toLaw"));
        toLaw.click();
        return ResponseData.success();
    }

    private ResponseData payment(WebDriver dr, OpenJxHtmlRequest request) throws Throwable {
        logger.info("爬虫设置缴费授权" + JSON.toJSONString(request));
        WebElement pay_state = dr.findElement(By.id("pay_state"));
        Thread.sleep(1000);
        pay_state.click();
        Thread.sleep(3000);
        //logger.info("设置缴费授权的源码为:" + dr.getPageSource());
        WebElement regiser_but = dr.findElement(By.className("regiser_but"));
        regiser_but.click();
        Thread.sleep(4000);
        //输入交易密码
        WebElement pass = dr.findElement(By.id("pass"));
        //从数据库中去查密码并解密
        String password = userJxService.selectPasswordByUserId(request.getUserId());
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buffer = decoder.decodeBuffer(password);
        if (password == null) {
            return ResponseData.error("查询用户密码失败:" + JSON.toJSONString(request));
        }
        pass.sendKeys(new String(buffer));
        WebElement sub = dr.findElement(By.id("sub"));
        sub.click();
        return ResponseData.success();
    }

    private ResponseData repayment(WebDriver dr, OpenJxHtmlRequest request) throws Throwable {
        logger.info("爬虫设置还款授权" + JSON.toJSONString(request));
        WebElement rePay_state = dr.findElement(By.id("rePay_state"));
        Thread.sleep(1000);
        rePay_state.click();
        Thread.sleep(3000);
        //logger.info("设置还款授权的源码为:" + dr.getPageSource());
        WebElement regiser_but = dr.findElement(By.className("regiser_but"));
        regiser_but.click();
        Thread.sleep(4000);
        WebElement pass = dr.findElement(By.id("pass"));
        //从数据库中去查密码并解密
        String password = userJxService.selectPasswordByUserId(request.getUserId());
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buffer = decoder.decodeBuffer(password);
        if (password == null) {
            return ResponseData.error("查询用户密码失败:" + JSON.toJSONString(request));
        }
        pass.sendKeys(new String(buffer));
        WebElement sub = dr.findElement(By.id("sub"));
        sub.click();
        return ResponseData.success();
    }

    @Override
    public void run() {
        logger.info("进入了线程池");
        WebDriver driver = null;
        try {
            if (OpenPageConstant.OPEN_PAGE_STAGE_TRADE.equals(stage)) {
                if (flag) {
                    ResponseData data2 = crawlerUtil.getUrl(url);
                    driver = (WebDriver) data2.getData();
                }
                tradePassword(driver, request, flag);
                stage = OpenPageConstant.OPEN_PAGE_STAGE_PAYMENT;
                flag = false;
                Thread.sleep(10000);
            }
            if (OpenPageConstant.OPEN_PAGE_STAGE_PAYMENT.equals(stage)) {
                if (flag) {
                    ResponseData data2 = crawlerUtil.getUrl(url);
                    driver = (WebDriver) data2.getData();
                }
                payment(driver, request);
                stage = OpenPageConstant.OPEN_PAGE_STAGE_REPAYMENT;
                flag = false;
                Thread.sleep(10000);
            }
            if (OpenPageConstant.OPEN_PAGE_STAGE_REPAYMENT.equals(stage)) {
                if (flag) {
                    ResponseData data1 = crawlerUtil.getUrl(url);
                    driver = (WebDriver) data1.getData();
                }
                repayment(driver, request);
                stage = OpenPageConstant.OPEN_PAGE_STAGE_ACCREDIT;
                flag = false;
                Thread.sleep(10000);
            }
            if (OpenPageConstant.OPEN_PAGE_STAGE_ACCREDIT.equals(stage)) {
                if (flag) {
                    ResponseData data1 = crawlerUtil.getUrl(url);
                    driver = (WebDriver) data1.getData();
                }
                accredit(driver, request);
                Thread.sleep(3000);
                //做最后的勾选和点击借款
                //获取勾选DOM
                WebElement checkbox = driver.findElement(By.id("checkbox"));
                //进行勾选
                checkbox.sendKeys(Keys.SPACE);
                //获取前往申请借款DOM
                WebElement openUp = driver.findElement(By.id("openUp"));
                //点击
                openUp.click();
            }

        } catch (Throwable e) {
            logger.error("进行自动开户发生异常" + e);
            logger.error("缴费页面爬虫出现发生异常", e);
            //转到七彩格子
            try {
                if (redisTemplate.hasKey("kzjr2qcgz" + request.getUserId())) {
                    logger.info("失败转单有值" + request.getUserId());
                } else {
                    redisTemplate.opsForValue().set("kzjr2qcgz" + request.getUserId(), "1",24*60,TimeUnit.MINUTES);
                }
            } catch (Exception e1) {
                logger.error("即信失败转单发生异常", e1);
            }
        } finally {
            if (driver != null) {
                driver.close();
            }
        }
    }


    /**
     * 设置交易密码
     * @param dr
     * @param request
     * @param flag
     * @return
     */
    public ResponseData tradePassword(WebDriver dr, OpenJxHtmlRequest request, boolean flag) {
        try {
            logger.info("爬虫设置交易密码并加密存入数据库" + JSON.toJSONString(request));
            if (flag) {
                WebElement setPass_state = dr.findElement(By.id("setPass_state"));
                Thread.sleep(1000);
                setPass_state.click();
                //logger.info("江西银行设置交易密码的页面的源码是:" + dr.getPageSource());
                Thread.sleep(3000);
            }
            //获取身份证号DOM
            WebElement idNo = dr.findElement(By.id("idNo"));
            idNo.sendKeys(request.getIdCardNumber());
            //设置交易密码
            WebElement encPin1 = dr.findElement(By.id("encPin1"));
            //通过密码生成工具生成密码并加密保存到数据库
            String password = PasswordUtil.getPassword();
            BASE64Encoder encoder = new BASE64Encoder();
            String base64Res = encoder.encode(password.getBytes());
            //存进数据库
            UserJx userJx = new UserJx();
            userJx.setUserId(request.getUserId());
            userJx.setPassword(base64Res);
            userJx.setMobile(request.getMobile());
            try {
                logger.info("将用户密码加密存入库");
                userJxService.savePassword(userJx);
            } catch (Exception e) {
                logger.error("将用户密码加密存入库发生异常");
                return ResponseData.error("将用户密码加密存入库发生异常");
            }
            encPin1.sendKeys(password);
            //确认密码
            WebElement encPin2 = dr.findElement(By.id("encPin2"));
            encPin2.sendKeys(password);
            //页面确认按钮
            WebElement sub1 = dr.findElement(By.id("sub"));
            //点击确认
            sub1.click();
        } catch (Throwable e) {
            logger.error("爬虫设置密码时发生错误", e);
            return ResponseData.error("爬虫设置密码时发生错误");
        }
        return ResponseData.success();
    }

}
