package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.api.service.BatchService;
import com.nyd.capital.entity.FailOrderKzjr;
import com.nyd.capital.model.kzjr.AssetSubmitRequest;
import com.nyd.capital.service.FailOrderService;
import com.nyd.capital.service.KzjrProductConfigService;
import com.nyd.capital.service.kzjr.KzjrService;
import com.nyd.capital.service.kzjr.config.KzjrConfig;
import com.nyd.capital.service.utils.Constants;
import com.tasfe.framework.redis.RedisService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/21
 **/
@Service("batchService")
public class BatchServiceImpl implements BatchService {
    Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);
    @Autowired
    private FailOrderService failOrderService;
    @Autowired
    private KzjrService kzjrService;
    @Autowired
    private KzjrConfig kzjrConfig;
    @Autowired
    private KzjrProductConfigService kzjrProductConfigService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void process() {
        Date startDate = DateUtils.addDays(new Date(), -20);
//        List<Integer> existList = new ArrayList<>();//有productCode 的duration
//        List<Integer> excludeList = new ArrayList<>();//没有productCode 的duration
//        Map<Integer,List<String>> existMap = new HashMap<>();

        List<Long> ids = new ArrayList<>(); //要删除的 记录

        try {
            List<FailOrderKzjr> detailList = failOrderService.queryKzjrFailByTime(startDate, null);
            for (FailOrderKzjr detail : detailList) {


//                KzjrProductConfig configInfo = kzjrProductConfigService.queryByPriority(detail.getDuration(),detail.getAmount());
//
//                if (configInfo == null) {
//                    logger.info("batch可用的productcode为null");
//                    continue;
//                }
//                logger.info("batch可用的productcode:"+JSON.toJSONString(configInfo));
                AssetSubmitRequest request = new AssetSubmitRequest();
//                request.setChannelCode(kzjrConfig.getChannelCode());
                request.setAccountId(detail.getAccountId());
                request.setAmount(detail.getAmount());
                request.setOrderId(detail.getOrderNo()+Constants.KZJR_SPLIT+(System.currentTimeMillis()+"").substring(2,11)+detail.getChannel());
                request.setType(2);
                request.setDuration(detail.getDuration());

//                request.setProductCode(configInfo.getProductCode());
                logger.info("资产为:" + JSON.toJSONString(request));
                JSONObject resultObj = kzjrService.assetSubmit(request);
                logger.info("资产提交结果:" + resultObj.toJSONString());
                request.setOrderId(request.getOrderId().split(Constants.KZJR_SPLIT)[0]);
                if (resultObj.getInteger("status") == 0) {
                    ids.add(detail.getId());
                    continue;
                } else if (resultObj.getInteger("status") == 5012) {
                    ids.add(detail.getId());
//                    limitProcess(configInfo.getId(), configInfo.getProductCode(), request.getAmount());
                    continue;
                } else {
//                    limitProcess(configInfo.getId(), configInfo.getProductCode(), request.getAmount());
                    logger.error("跑批失败订单sendorder error***orderId:" + request.getOrderId() + "原因:" + resultObj.toJSONString());
//                    return false;
                }
//                }
//                if (resultObj.getInteger("status") == 5011) {
//                    excludeList.add(detail.getDuration());
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ids.size() != 0) {
                failOrderService.deleteKzjrByIds(ids);
            }
        }
    }

    private void limitProcess(Long id, String productCode, BigDecimal amount) {
        try {
            String flag = redisService.acquireLock(Constants.KZJR_LOCK_PREFIX + productCode);
            //获取不到循环
            int loopCount = 120;
            while (flag == null && loopCount > 0) {
                loopCount--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag = redisService.acquireLock(Constants.KZJR_LOCK_PREFIX + productCode);
            }
            if (flag == null) {
                logger.error("提交资产返回失败 获取锁 失败");
                return;
            }
            redisTemplate.opsForValue().increment(Constants.KZJR_PREFIX + productCode, amount.doubleValue());
            double remainAmount = (double) redisTemplate.opsForValue().get(Constants.KZJR_PREFIX + productCode);
            kzjrProductConfigService.update(id, 0, 0, new BigDecimal(remainAmount));
            redisService.releaseLock(Constants.KZJR_LOCK_PREFIX + productCode);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提交资产返回失败 重新更新余额 异常");
            return;
        } finally {
            try {
                redisService.releaseLock(Constants.KZJR_LOCK_PREFIX + productCode);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("释放锁失败");
            }
        }
    }
}
