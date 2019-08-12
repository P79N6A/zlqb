package com.nyd.capital.service.pocket.util;

import com.tasfe.framework.support.model.ResponseData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liuqiu
 */
@Component
public class CrawlerUtil {

    private static Logger logger = LoggerFactory.getLogger(CrawlerUtil.class);

    @Autowired
    private PocketConfig pocketConfig;

    public ResponseData getUrl(String url) {
        logger.info("begin create a headless browser,url:" + url);
        try {
            logger.info("创建无头浏览器进行爬虫,url:" + url);
            DesiredCapabilities dcaps = new DesiredCapabilities();
            //ssl证书支持
            dcaps.setCapability("acceptSslCerts", true);
            //截屏支持
            dcaps.setCapability("takesScreenshot", true);
            //css搜索支持
            dcaps.setCapability("cssSelectorsEnabled", true);
            //js支持ChromUtil
            dcaps.setJavascriptEnabled(true);
            //驱动支持
            dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, pocketConfig.getDriverLocation());
            WebDriver dr = null;
            dr = new PhantomJSDriver(dcaps);
            dr.get(url);
            return ResponseData.success(dr);
        } catch (Throwable e) {
            logger.error("create a headless browser has exception,url:" + url, e);
            return ResponseData.error("create a headless browser has exception");
        }
    }
}
