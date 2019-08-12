package com.creativearts.nyd.pay.service.log;

import com.alibaba.fastjson.JSON;
import com.nyd.zeus.model.RepayInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cong Yuxiang
 * 2018/1/8
 **/
public class LoggerUtils {
    private static Logger logger = LoggerFactory.getLogger("pay");

    public static void write(RepayInfo repayInfo){

        logger.info(JSON.toJSONString(repayInfo));
    }
    public  static void write(String s){
        logger.info(s);
    }
}
