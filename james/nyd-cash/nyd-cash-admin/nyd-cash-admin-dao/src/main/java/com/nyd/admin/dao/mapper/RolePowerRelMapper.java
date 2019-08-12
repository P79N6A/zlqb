package com.nyd.admin.dao.mapper;

import com.nyd.admin.model.power.vo.RolePowerRelVo;

import java.util.List;

/**
 * Created by hwei on 2018/1/5.
 */
public interface RolePowerRelMapper {

    void insertRolePowerList(List<RolePowerRelVo> list);
}
