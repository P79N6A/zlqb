package com.nyd.capital.service.utils;

import com.alibaba.fastjson.JSON;
import com.nyd.zeus.model.RemitInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cong Yuxiang
 * 2018/1/8
 **/
public class LoggerUtils {
    private static Logger logger = LoggerFactory.getLogger("remit");

    public static void write(RemitInfo remitInfo){

        logger.info(JSON.toJSONString(remitInfo));
    }
    public  static void write(String s){
        logger.info(s);
    }
}
