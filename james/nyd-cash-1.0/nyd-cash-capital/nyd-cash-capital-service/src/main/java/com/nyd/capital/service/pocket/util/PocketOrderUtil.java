package com.nyd.capital.service.pocket.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 订单sign生成工具
 * @author liuqiu
 */
@Component
public class PocketOrderUtil {

    @Autowired
    private PocketConfig pocketConfig;

    private static Logger logger = LoggerFactory.getLogger(PocketOrderUtil.class);

    public String createSign(String idNo) {
        String pocketOrderAccount = pocketConfig.getPocketOrderAccount();
        String pocketOrderPwd = pocketConfig.getPocketOrderPwd();
        int timestamp = getSecondTimestamp(new Date());
        try {
            String sign = Md5Util.getMD5(pocketOrderAccount + Md5Util.getMD5(pocketOrderPwd) + Md5Util.getMD5(idNo) + timestamp);
            return sign;
        }catch (Exception e){
            logger.error("口袋理财生成sign发生异常",e);
        }
        return null;
    }

    /**
     * 生成时间戳
     * @param date
     * @return
     */
    public int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }

}
