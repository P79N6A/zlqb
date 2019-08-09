package com.nyd.user.service.run;

import com.alibaba.fastjson.JSON;
import com.nyd.library.api.LibraryContract;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuqiu
 */
public class LibraryRunnable implements Runnable {
    Logger logger = LoggerFactory.getLogger(LibraryRunnable.class);
    private LibraryContract libraryService;
    private String mobile;
    private String appName;
    private String source;

    public LibraryRunnable(LibraryContract libraryService, String mobile,String appName,String source) {
        this.libraryService = libraryService;
        this.mobile = mobile;
        this.appName = appName;
        this.source = source;
    }

    @Override
    public void run() {
        try {
            logger.info("进入撞库线程中,手机号为:" + mobile);
            ResponseData responseData = libraryService.hitLibrary(mobile,appName,source);
            logger.info(JSON.toJSONString(responseData));
        } catch (Exception e) {
            logger.error("调用撞库处理接口发生异常");
        }
    }
}
