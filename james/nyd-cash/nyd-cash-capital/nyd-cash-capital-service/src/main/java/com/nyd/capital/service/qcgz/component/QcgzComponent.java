package com.nyd.capital.service.qcgz.component;


import com.alibaba.fastjson.JSON;
import com.nyd.capital.service.qcgz.config.QcgzConfig;
import com.nyd.capital.service.utils.ShaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 七彩格子参数加密实体类
 *
 * @author cm
 */
@Component
public class QcgzComponent {
    Logger logger = LoggerFactory.getLogger(QcgzComponent.class);

    @Autowired
    private QcgzConfig qcgzConfig;

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
            try {
                sb.append(str).append("=").append(URLEncoder.encode(map.get(str).toString(), "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                logger.error("编码异常", e);
                e.printStackTrace();
            }
        }

        sb.append("key=");
        sb.append(qcgzConfig.getPrivateKey());
        logger.info("七彩格子加密前字符串:" + sb);
        String result = ShaUtils.getSha(sb.toString());
        logger.info("最终结果:" + result);

        return result.toUpperCase();
    }
}
