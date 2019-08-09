package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.RoleDao;
import com.nyd.admin.dao.UserDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.PowerMapper;
import com.nyd.admin.dao.mapper.RoleMapper;
import com.nyd.admin.dao.mapper.RolePowerRelMapper;
import com.nyd.admin.entity.power.Role;
import com.nyd.admin.model.power.dto.RoleDto;
import com.nyd.admin.model.power.dto.RolePowerDto;
import com.nyd.admin.model.power.vo.RolePowerRelVo;
import com.nyd.admin.model.power.vo.RoleVo;
import com.nyd.admin.model.power.vo.UserRoleRelVo;
import com.nyd.admin.service.CrudService;
import com.nyd.admin.service.RoleService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.RoleStruct;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by hwei on 2018/1/4.
 */
@Service
public class RoleServiceImpl extends CrudService<RoleMapper, Role>  implements RoleService{
    private static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    RoleDao roleDao;

    @Autowired
    RolePowerRelMapper rolePowerRelMapper;

    @Autowired
    UserDao userDao;

    @Autowired
    PowerMapper powerMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData saveRole(RoleDto roleDto)  {
        ResponseData responseData = ResponseData.success();
        //根据角色名查询是否存在此角色
        RoleDto rd = new RoleDto();
        rd.setRoleName(roleDto.getRoleName());
        try {
            List<RoleVo> roleVos = roleDao.findRoles(rd);
            if (roleVos!=null&&roleVos.size()>0) {
                responseData = ResponseData.error("此角色名已存在");
                return responseData;
            }
        } catch (Exception e) {
            logger.error("findRoles has exception",e);
            responseData = ResponseData.error("服务器开小差了");
            return responseData;
        }
        try {
            roleDao.saveRole(roleDto);
        } catch (Exception e) {
            logger.error("saveRole has exception",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData updateRole(RoleDto roleDto) {
        ResponseData responseData = ResponseData.success();
        if (roleDto==null||roleDto.getId()==null) {
            responseData = ResponseData.error("参数不能为空");
            return responseData;
        }
        try {
            //如果只修改角色名称，可以直接修改
            if (roleDto.getDeleteFlag()==null) {
                roleDao.updateRole(roleDto);
            } else {   //如果要逻辑删除角色，更新时判断是否被用户引用
                List<UserRoleRelVo> list = userDao.findRolesByRoleId(roleDto.getId());
                if (list!=null&&list.size()>0) {
                    return ResponseData.error("有用户还在使用该角色，请先释放用户下的角色");
                } else {
                    roleDao.updateRole(roleDto);
                }
            }
        } catch (Exception e) {
            logger.error("updateRole has exception",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData findRoles(RoleDto roleDto) {
        ResponseData responseData = ResponseData.success();
        try {
           responseData.setData(roleDao.findRoles(roleDto));
        } catch (Exception e) {
            logger.error("findRoles has exception",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData findPowersByRole(RoleDto roleDto) {
        ResponseData responseData = ResponseData.success();
        try {
            responseData.setData(roleDao.findPowersByRole(roleDto));
        } catch (Exception e) {
            logger.error("findPowersByRole has exception",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData editRolePower(RolePowerDto rolePowerDto) {
        logger.info("editRolePower:"+JSONObject.toJSONString(rolePowerDto));
        ResponseData responseData = ResponseData.success();
        //首先删除角色对应的原有权限
        try {
            roleDao.deletePowersByRoleId(rolePowerDto.getId());
        } catch (Exception e) {
            logger.error("deletePowersByRoleId has exception",e);
            responseData = ResponseData.error("服务器开小差了");
            return responseData;
        }
        List<RolePowerRelVo> list = new ArrayList<>();
        if (rolePowerDto!=null) {
            if (rolePowerDto.getPowerList()!=null&&rolePowerDto.getPowerList().size()>0) {
                for (Integer powerId:rolePowerDto.getPowerList()) {
                    RolePowerRelVo rolePowerRelVo = new RolePowerRelVo();
                    rolePowerRelVo.setRoleId(rolePowerDto.getId());
                    rolePowerRelVo.setPowerId(powerId);
                    list.add(rolePowerRelVo);
                }
            }
        }
        try {
            rolePowerRelMapper.insertRolePowerList(list);
        } catch (Exception e) {
            logger.error("inserRoletPowerList has exception",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public PageInfo<RoleVo> getRoleLs(RoleDto roleDto) throws ParseException {
        Role role = new Role();
        BeanUtils.copyProperties(roleDto, role);
        PageInfo<Role> rolePageInfo = this.findPage(roleDto,role);

        return RoleStruct.INSTANCE.poPage2VoPage(rolePageInfo);
    }

    @Override
    public List<RolePowerRelVo> findPowersByPowerId(Integer powerId) {
        List<RolePowerRelVo> rolePowerRelVos = new ArrayList<>();
        try {
            rolePowerRelVos =  roleDao.findPowersByPowerId(powerId);
        } catch (Exception e) {
            logger.error("findPowersByPowerId has exception",e);
        }
        return rolePowerRelVos;
    }

}
