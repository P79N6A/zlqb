package com.nyd.order.dao;

import com.nyd.order.model.WithholdTaskConfig;

import java.util.Date;
import java.util.List;

/**
 * @author liuqiu
 */
public interface WithholdTaskConfigDao {

    /**
     * 查询代扣任务开始时间
     */
    List<WithholdTaskConfig> select() throws Exception;
    /**
     * 根据配置编码查询代扣任务开始时间
     */
    List<WithholdTaskConfig> selectByCode(String code) throws Exception;

    /**
     * 更新代扣任务开始时间
     * @param startTime
     */
    void update(Date startTime) throws Exception;
    /**
     * 更新代扣任务开始时间
     * @param startTime
     */
    void updateByCode(Date startTime,String code) throws Exception;
}
