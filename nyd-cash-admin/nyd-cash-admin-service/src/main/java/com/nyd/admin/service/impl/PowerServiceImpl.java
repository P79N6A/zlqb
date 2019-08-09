package com.nyd.admin.service.impl;

import com.nyd.admin.dao.PowerDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.PowerMapper;
import com.nyd.admin.entity.power.Power;
import com.nyd.admin.model.power.UserPowerInfo;
import com.nyd.admin.model.power.dto.PowerDto;
import com.nyd.admin.model.power.vo.RolePowerRelVo;
import com.nyd.admin.service.CrudService;
import com.nyd.admin.service.PowerService;
import com.nyd.admin.service.RoleService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.PowerUtil;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Peng
 * @create 2018-01-03 15:07
 **/
@Service
public class PowerServiceImpl extends CrudService<PowerMapper,Power> implements PowerService {
    private static Logger logger = LoggerFactory.getLogger(PowerServiceImpl.class);

    @Autowired
    PowerMapper powerMapper;
    @Autowired
    PowerDao powerDao;
    @Autowired
    RoleService roleService;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData savePower(PowerDto dto) {
        ResponseData responseData = ResponseData.success();
        Power power = new Power();
        power.setPid(dto.getPid());
        power.setPowerName(dto.getPowerName());
        power.setPowerKey(dto.getPowerKey());
        power.setPowerUrl(dto.getPowerUrl());

        try {
            powerDao.savePowerMessage(power);
        } catch (Exception e) {
            logger.error("插入权限报错",e);
            return responseData.error();
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData updatePower(PowerDto dto) {
        ResponseData responseData = ResponseData.success();
        try {
            powerDao.updatePowerMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新权限报错");
            return responseData.error("更新权限报错");
        }
        return responseData;
    }


    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData deletePower(PowerDto dto) {
        ResponseData responseData = ResponseData.success();
        try {
            //刪除时先判断该权限是否还有角色在使用
            if(dto != null && dto.getDeleteFlag() == 1){
                List<RolePowerRelVo> rolePowerRelVos  = roleService.findPowersByPowerId(dto.getId().intValue());
                if(rolePowerRelVos != null && rolePowerRelVos.size() > 0){
                    return ResponseData.error("有角色还在使用该权限，请先释放角色下的权限");
                }
            }
            powerDao.updatePowerMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新权限报错");
            return responseData.error("更新权限报错");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData queryPower() {
        ResponseData responseData = ResponseData.success();
        try {
            List<UserPowerInfo> powerList = powerMapper.queryPower();
            responseData.setData(PowerUtil.getUserPowerVo(powerList));
        } catch (Exception e) {
            logger.error("查询报错");
            e.printStackTrace();
        }
        return responseData;
    }
}
