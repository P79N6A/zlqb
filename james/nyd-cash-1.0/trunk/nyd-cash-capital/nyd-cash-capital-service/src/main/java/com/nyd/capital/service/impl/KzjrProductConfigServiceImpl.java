package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.dao.ds.DataSourceContextHolder;
import com.nyd.capital.dao.mappers.TKzjrProductConfigMapper;
import com.nyd.capital.entity.KzjrProductConfig;
import com.nyd.capital.model.kzjr.KzjrProductConfigInfo;
import com.nyd.capital.service.KzjrProductConfigService;
import com.nyd.capital.service.aspect.RoutingDataSource;
import com.nyd.capital.service.utils.Constants;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.redis.RedisService;
import com.tasfe.framework.support.service.impls.CrudServiceImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
//import tk.mybatis.mapper.entity.Condition;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/14
 **/
@Service
public class KzjrProductConfigServiceImpl extends CrudServiceImpl<KzjrProductConfigInfo,KzjrProductConfig,Long> implements KzjrProductConfigService{

    Logger logger = LoggerFactory.getLogger(KzjrProductConfigServiceImpl.class);

    @Autowired
    private TKzjrProductConfigMapper tKzjrProductConfigMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public void saveInfo(KzjrProductConfigInfo info) {

    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public List<KzjrProductConfig> query(Integer duration) {
//        long id = 0l;
//        try {
//            id = DateUtils.parseDate(userDate,"yyyy-MM-dd").getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Criteria criteria = Criteria.from(KzjrProductConfig.class).where().and("user_date", Operator.EQ,id).and("duration",Operator.EQ,duration).and("status",Operator.EQ,0).endWhere();
//
//        List<KzjrProductConfigInfo> infos = this.find(criteria);
//        KzjrProductConfig config = new KzjrProductConfig();
//        config.setUseDate(DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
//        config.setDuration(duration);
//        config.setStatus(0);
        Map map = new HashMap();
        logger.info("当前时间:"+new Date());
        map.put("useDate",DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
        map.put("duration",duration);
        map.put("status",0);


        List<KzjrProductConfig> infos = tKzjrProductConfigMapper.selectByCon(map);

//        System.out.println(JSON.toJSONString(infos));
        return infos;
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public void update(Long id,Integer status,Integer fullStatus,BigDecimal remainAmount){
        Criteria criteria = Criteria.from(KzjrProductConfig.class).where().and("id",Operator.EQ,id).endWhere();
        KzjrProductConfigInfo info = new KzjrProductConfigInfo();
        if(status!=null) {
            info.setStatus(status);
        }
        if(fullStatus!=null){
            info.setFullStatus(fullStatus);
        }
        if(remainAmount!=null){
            info.setRemainAmount(remainAmount);
        }
        this.update(info,criteria);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public KzjrProductConfig queryByPriority(Integer duration, BigDecimal amount) {
        Map map = new HashMap();
        map.put("useDate",DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
        map.put("duration",duration);
        map.put("status",0);
        map.put("fullStatus",0);
        List<KzjrProductConfig> list = tKzjrProductConfigMapper.selectByPriority(map);

        if(list==null||list.size()==0){
            return null;
        }
        logger.info("查出的productcode:"+ JSON.toJSONString(list));
        long start = System.currentTimeMillis();
        for(KzjrProductConfig config:list){
            String productCode = config.getProductCode();
            String flag = redisService.acquireLock(Constants.KZJR_LOCK_PREFIX+productCode);
            //获取不到循环
            int loopCount =120;
            while (flag==null&&loopCount>0){
                loopCount--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag = redisService.acquireLock(Constants.KZJR_LOCK_PREFIX+productCode);
            }
            if(flag == null){
                logger.error("获取锁失败");
                continue;
            }
            try {
                logger.info("begin");
                Long zid = config.getId();
                logger.info("zid为"+zid);
                if(zid==null){
                    zid = 0l;
                }
                String redisKey = Constants.KZJR_PREFIX + productCode+"_"+zid;
                logger.info("redisKey为"+redisKey);
                if (redisTemplate.hasKey(redisKey)) {
                    logger.info("00000");
                    double remainAmount = (double) redisTemplate.opsForValue().get(redisKey);
                    logger.info("remainAmout:"+remainAmount+",amount:"+amount.doubleValue());
                    if ((remainAmount - amount.doubleValue()) < 0) {
                        logger.info("111");
                        //todo
//                    if(remainAmount<500) {
//                        try {
//                            update(config.getId(), 0, 0, new BigDecimal(remainAmount));
//                            redisTemplate.delete(Constants.KZJR_PREFIX + productCode);
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            logger.error(productCode+"kzjr haskey更新数据库异常1"+e.getMessage());
//                        }
//                    }
                        redisService.releaseLock(Constants.KZJR_LOCK_PREFIX + productCode);
                        continue;
                    } else {
                        logger.info("222");
                        redisTemplate.opsForValue().set(redisKey, remainAmount-amount.doubleValue());
//                        logger.info("2222");
                        try {
                            update(config.getId(), 0, 0, new BigDecimal(remainAmount - amount.doubleValue()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error(productCode + "kzjr haskey更新数据库异常2" + e.getMessage());
                        }
//                        logger.info("22222");
                        redisService.releaseLock(Constants.KZJR_LOCK_PREFIX + productCode);
//                        logger.info("22"+JSON.toJSONString(config));
                        return config;

                    }
                } else {
                    double totalA = config.getTotalAmount().doubleValue();
                    if ((totalA - amount.doubleValue()) < 0) {
                        logger.info("333");
//                    update(config.getId(),1,1,config.getTotalAmount());
//                    if(totalA<500){
//                        update(config.getId(),0,0,config.getTotalAmount());
//                    }
                        redisService.releaseLock(Constants.KZJR_LOCK_PREFIX + productCode);
                        continue;
                    } else {
                        logger.info("444");
                        redisTemplate.opsForValue().set(redisKey, totalA - amount.doubleValue());
                        try {
                            update(config.getId(), 0, 0, new BigDecimal(totalA - amount.doubleValue()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error(productCode + "kzjr nokey更新数据库异常4" + e.getMessage());
                        }
                        redisService.releaseLock(Constants.KZJR_LOCK_PREFIX + productCode);
                        return config;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                try {
                    redisService.releaseLock(Constants.KZJR_LOCK_PREFIX + productCode);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }finally {
                try {
                    redisService.releaseLock(Constants.KZJR_LOCK_PREFIX + productCode);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }


        }
        logger.info("耗时:"+(System.currentTimeMillis()-start));
        return null;
    }

//    @Override
//    public List<KzjrProductConfigInfo> findByCondition(Condition condition) {
//        return null;
//    }
}
