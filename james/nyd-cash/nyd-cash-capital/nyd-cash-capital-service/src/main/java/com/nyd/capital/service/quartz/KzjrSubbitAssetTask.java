package com.nyd.capital.service.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.entity.FailOrderKzjr;
import com.nyd.capital.entity.KzjrProductConfig;
import com.nyd.capital.model.kzjr.AssetSubmitRequest;
import com.nyd.capital.service.FailOrderService;
import com.nyd.capital.service.KzjrProductConfigService;
import com.nyd.capital.service.kzjr.KzjrService;
import com.nyd.capital.service.kzjr.config.KzjrConfig;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Cong Yuxiang
 * 2017/12/15
 **/
@Component
public class KzjrSubbitAssetTask {
    Logger logger  = LoggerFactory.getLogger(KzjrSubbitAssetTask.class);
    @Autowired
    private FailOrderService failOrderService;
    @Autowired
    private KzjrService kzjrService;
    @Autowired
    private KzjrConfig kzjrConfig;
    @Autowired
    private KzjrProductConfigService kzjrProductConfigService;

    public void run() {
        Date startDate = DateUtils.addDays(new Date(), -10);
//        List<Integer> existList = new ArrayList<>();//有productCode 的duration
        List<Integer> excludeList = new ArrayList<>();//没有productCode 的duration
//        Map<Integer,List<String>> existMap = new HashMap<>();

        List<Long> ids = new ArrayList<>(); //要删除的 记录

        try {
            List<FailOrderKzjr> detailList = failOrderService.queryKzjrFailByTime(startDate, null);
            for (FailOrderKzjr detail : detailList) {
                if (excludeList.contains(detail.getDuration())) {
                    continue;
                }


                List<KzjrProductConfig> configs = kzjrProductConfigService.query(detail.getDuration());
                if (configs == null || configs.size() == 0) {
                    excludeList.add(detail.getDuration());
                    continue;
                }
                AssetSubmitRequest request = new AssetSubmitRequest();
                request.setChannelCode(kzjrConfig.getChannelCode());
                request.setAccountId(detail.getAccountId());
                request.setAmount(detail.getAmount());
                request.setOrderId(detail.getOrderNo());
                request.setType(2);
                request.setDuration(detail.getDuration());

                JSONObject resultObj = null;
                for (KzjrProductConfig configInfo : configs) {
//                    request.setProductCode(configInfo.getProductCode());
                    logger.info("资产为:" + JSON.toJSONString(request));
                    resultObj = kzjrService.assetSubmit(request);
                    logger.info("资产提交结果:" + resultObj.toJSONString());
                    if (resultObj.getInteger("status") == 0) {
                        ids.add(detail.getId());
                        break;
                    } else if (resultObj.getInteger("status") == 5011) {
                        //超过金额
//                    kzjrProductConfigService.update(configInfo.getId(),1);
                    } else {
                        logger.error("sendorder error***productCode:" + configInfo.getProductCode() + "orderId:" + request.getOrderId() + "原因:" + resultObj.toJSONString());
//                    return false;
                    }
                }
                if (resultObj.getInteger("status") == 5011) {
                    excludeList.add(detail.getDuration());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ids.size() != 0) {
                failOrderService.deleteKzjrByIds(ids);
            }
        }
    }
}
