package com.nyd.capital.service.pocket.run;

import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.model.pocket.PocketHtmlMessage;
import com.nyd.capital.model.pocket.PocketParentResult;
import com.nyd.capital.model.pocket.PocketTermsAuthPageDto;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.tasfe.framework.support.model.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.util.concurrent.TimeUnit;


/**
 * @author liuqiu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgCodeRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MsgCodeRunnable.class);

    private MsgCodeRunnableVo vo;


    @Override
    public void run() {
        logger.info("begin submit sms code html thread");
        try {
            WebElement smsCode;
            try {
                smsCode = vo.getDriver().findElement(By.id("smsInput"));
            } catch (Exception e) {
                logger.info("did not find SMS_CODE try another");
                smsCode = vo.getDriver().findElement(By.id("SMS_CODE"));
            }
            //获取验证码DOM
            smsCode.sendKeys(vo.getCode());
            //获取同意协议单选DOM
            WebElement checkbox = vo.getDriver().findElement(By.id("mainAcceptIpt"));
            //进行勾选
            checkbox.sendKeys(Keys.SPACE);
            //获取确认DOM
            WebElement sub = vo.getDriver().findElement(By.id("sub"));
            //进行点击操作
            sub.click();
            logger.info(vo.getDriver().getPageSource());
            Thread.sleep(8000);
            //进行授权
            PocketTermsAuthPageDto authPageDto = new PocketTermsAuthPageDto();
            authPageDto.setIdNumber(vo.getIdNumber());
            authPageDto.setIsUrl("1");
            authPageDto.setRetUrl(vo.getPocketConfig().getPocketTermsAuthPageRetUrl()+"?url=" +vo.getUserId());
            authPageDto.setPaymentDeadline(vo.getPocketConfig().getPocketPayMandate());
            authPageDto.setPaymentMaxAmt(vo.getPocketConfig().getPocketPayAmount());
            authPageDto.setRepayDeadline(vo.getPocketConfig().getPocketRepayMandate());
            authPageDto.setRepayMaxAmt(vo.getPocketConfig().getPocketRepayAmount());
            ResponseData<PocketParentResult> data = vo.getPocket2Service().termsAuthPage(authPageDto);
            if (!OpenPageConstant.STATUS_ZERO.equals(data.getStatus())){
                return;
            }
            PocketParentResult result = data.getData();
            if (!OpenPageConstant.STATUS_ZERO.equals(result.getRetCode())){
                return;
            }
            String url = getPocketResultUrl(data);
            vo.getDriver().get(url);
            WebElement pass = vo.getDriver().findElement(By.id("pass"));
            //查询用户密码
            String password = vo.getUserPocketService().selectPasswordByUserId(vo.getUserId());
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] buffer = decoder.decodeBuffer(password);
            if (password == null) {
                return;
            }
            pass.sendKeys(new String(buffer));
            WebElement sub1 = vo.getDriver().findElement(By.id("sub"));
            sub1.click();
            Thread.sleep(8000);
            vo.getDriver().close();
            vo.getDriver().quit();
        } catch (Throwable e) {
            logger.error("submit sms code html thread has exception", e);
            if (vo.getDriver() != null) {
                vo.getDriver().close();
                vo.getDriver().quit();
            }
//            vo.getRedisTemplate().opsForValue().set(OpenPageConstant.ERROR_ORDER + vo.getUserId(), "1", 300, TimeUnit.MINUTES);
        }
    }

    public static String getPocketResultUrl(ResponseData<PocketParentResult> data) {
        PocketParentResult dataData = data.getData();
        String retData = dataData.getRetData();
        JSONObject jsonObject = JSONObject.parseObject(retData);
        String url = jsonObject.getString("url");
        return url;
    }

}
