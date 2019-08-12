package com.nyd.capital.service.impl;

import com.nyd.capital.dao.ds.DataSourceContextHolder;
import com.nyd.capital.dao.mappers.TFailorderKzjrMapper;
import com.nyd.capital.entity.FailOrderKzjr;
import com.nyd.capital.entity.TimeRangeVo;
import com.nyd.capital.model.kzjr.FailOrderKzjrInfo;
import com.nyd.capital.service.FailOrderService;
import com.nyd.capital.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.service.impls.CrudServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import tk.mybatis.mapper.entity.Condition;

import java.util.Date;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/14
 **/
@Service
public class FailOrderServiceImpl extends CrudServiceImpl<FailOrderKzjrInfo,FailOrderKzjr,Long>  implements FailOrderService{

    Logger logger = LoggerFactory.getLogger(FailOrderServiceImpl.class);

    @Autowired
    private TFailorderKzjrMapper tFailorderKzjrMapper;




    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void saveKzjrInfo(FailOrderKzjrInfo info) {
        FailOrderKzjr failOrderKzjr = new FailOrderKzjr();
        failOrderKzjr.setDescription(info.getDescription());
        failOrderKzjr.setOrderNo(info.getOrderNo());
        failOrderKzjr.setAccountId(info.getAccountId());
        failOrderKzjr.setReason(info.getReason());
        failOrderKzjr.setDuration(info.getDuration());
        failOrderKzjr.setAmount(info.getAmount());
        failOrderKzjr.setChannel(info.getChannel());

        this.save(failOrderKzjr);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<FailOrderKzjr> queryKzjrFailByTime(Date startTime, Date endTime) {
        TimeRangeVo vo = new TimeRangeVo();
        if(startTime==null){
            logger.error("起始时间不能为空");
            throw new RuntimeException("起始时间不能为空");
        }
        vo.setStartDate(startTime);
        if(endTime==null){
            vo.setEndDate(new Date());
        }else {
            vo.setEndDate(endTime);
        }
        List<FailOrderKzjr> list = tFailorderKzjrMapper.queryByTime(vo);

        return list;
    }

   /* @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void deleteKzjrById(Long id) {
        this.delete(id);
    }*/

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void deleteKzjrByIds(List<Long> ids) {
        this.delete(ids.toArray(new Long[ids.size()]));
    }

    /*@Override
    public List<FailOrderKzjrInfo> findByCondition(Condition condition) {
        return null;
    }*/
}
