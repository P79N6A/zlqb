package com.nyd.admin.dao.mapper;

import com.nyd.admin.dao.CrudDao;
import com.nyd.admin.entity.power.Power;
import com.nyd.admin.model.power.UserPowerInfo;

import java.util.List;
import java.util.Set;

/**
 * @author Peng
 * @create 2018-01-04 10:32
 **/
public interface PowerMapper extends CrudDao<Power> {
    List<UserPowerInfo>queryPower();

    Set<Integer> getPid(List<Integer> idList);
}
