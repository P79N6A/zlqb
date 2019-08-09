package com.nyd.capital.service;

import com.nyd.capital.entity.FailOrderKzjr;
import com.nyd.capital.model.kzjr.FailOrderKzjrInfo;
import com.tasfe.framework.support.service.CrudService;

import java.util.Date;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/14
 **/
public interface FailOrderService extends CrudService<FailOrderKzjrInfo,FailOrderKzjr,Long> {
    void saveKzjrInfo(FailOrderKzjrInfo info);
    List<FailOrderKzjr> queryKzjrFailByTime(Date startTime, Date endTime);
//    void deleteKzjrById(Long id);
    void deleteKzjrByIds(List<Long> ids);
}
