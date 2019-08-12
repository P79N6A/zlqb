package com.nyd.capital.service.jx.util;


import com.alibaba.fastjson.JSON;
import com.nyd.capital.service.utils.ShaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 即信参数加密实体类
 *
 * @author liuqiu
 */
@Component
public class JxSHAUtil {
    Logger logger = LoggerFactory.getLogger(JxSHAUtil.class);

    @Autowired
    private SignUtil signUtil;

    public String getSign(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() == null || entry.getValue().trim().length() == 0) {
                continue;
            }
            list.add(entry.getKey());
        }

        Collections.sort(list);
        logger.info("排序后的list:" + JSON.toJSON(list));

        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(map.get(str));
        }
        String ss = sb.toString();
        logger.info("ss:" + ss);

        String result = signUtil.sign(ss);
        logger.info("最终结果:" + result);

        return result;
    }
}
