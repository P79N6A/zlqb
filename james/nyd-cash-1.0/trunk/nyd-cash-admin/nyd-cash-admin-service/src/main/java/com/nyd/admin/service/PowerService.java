package com.nyd.admin.service;

import com.nyd.admin.model.power.dto.PowerDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author Peng
 * @create 2018-01-03 15:01
 **/
public interface PowerService {
    ResponseData savePower(PowerDto dto);

    ResponseData updatePower(PowerDto dto);

    ResponseData queryPower();

    ResponseData deletePower(PowerDto dto);
}
