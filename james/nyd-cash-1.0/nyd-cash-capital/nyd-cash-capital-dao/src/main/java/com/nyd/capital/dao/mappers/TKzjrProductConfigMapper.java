package com.nyd.capital.dao.mappers;


import com.nyd.capital.dao.CrudDao;
import com.nyd.capital.entity.KzjrProductConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TKzjrProductConfigMapper extends CrudDao<KzjrProductConfig> {
        List<KzjrProductConfig> selectByCon(Map map);
        List<KzjrProductConfig> selectByPriority(Map map);
}