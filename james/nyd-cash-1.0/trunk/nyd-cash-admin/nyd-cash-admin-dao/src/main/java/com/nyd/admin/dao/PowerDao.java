package com.nyd.admin.dao;

import com.nyd.admin.entity.power.Power;
import com.nyd.admin.model.power.dto.PowerDto;

/**
 * @author Peng
 * @create 2018-01-03 14:28
 **/
public interface PowerDao {
    void savePowerMessage(Power power) throws Exception;

    void updatePowerMessage(PowerDto dto) throws Exception;

    void queryPowerMessage() throws Exception;
}
