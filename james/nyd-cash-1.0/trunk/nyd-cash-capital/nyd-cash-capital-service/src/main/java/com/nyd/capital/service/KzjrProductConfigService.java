package com.nyd.capital.service;

import com.nyd.capital.entity.KzjrProductConfig;
import com.nyd.capital.model.kzjr.KzjrProductConfigInfo;
import com.tasfe.framework.support.service.CrudService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/14
 **/
public interface KzjrProductConfigService extends CrudService<KzjrProductConfigInfo,KzjrProductConfig,Long> {
    void saveInfo(KzjrProductConfigInfo info);

    List<KzjrProductConfig> query(Integer duration);

    void update(Long id,Integer status,Integer fullStatus,BigDecimal remainAmount);

    KzjrProductConfig queryByPriority(Integer duration, BigDecimal amount);
}
