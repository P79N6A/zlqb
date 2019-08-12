package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.RoleDao;
import com.nyd.admin.entity.power.Role;
import com.nyd.admin.entity.power.RolePowerRel;
import com.nyd.admin.model.power.dto.RoleDto;
import com.nyd.admin.model.power.vo.RolePowerRelVo;
import com.nyd.admin.model.power.vo.RoleVo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hwei on 2018/1/4.
 */
@Repository
public class RoleDaoImpl implements RoleDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void saveRole(RoleDto roleDto) throws Exception {
        Role role = new Role();
        BeanUtils.copyProperties(roleDto,role);
        crudTemplate.save(role);
    }

    @Override
    public void updateRole(RoleDto roleDto) throws Exception {
        RoleVo roleVo = new RoleVo();
        BeanUtils.copyProperties(roleDto, roleVo);
        Criteria criteria = Criteria.from(Role.class)
                .whioutId()
                .where()
                .and("id",Operator.EQ,roleDto.getId())
                .endWhere();
        roleVo.setId(null);
        crudTemplate.update(roleVo,criteria);
    }

    @Override
    public List<RoleVo> findRoles(RoleDto roleDto) throws Exception {
        RoleVo roleVo = new RoleVo();
        if (roleDto!=null) {
            roleVo.setRoleName(roleDto.getRoleName());
            if (roleDto.getId()!=null) {
                roleVo.setId(Long.valueOf(roleDto.getId()));
            }
        }
        Criteria criteria = Criteria.from(Role.class)
                .whioutId()
                .where()
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(roleVo,criteria);
    }

    @Override
    public List<RolePowerRelVo> findPowersByRole(RoleDto roleDto) throws Exception {
        RolePowerRelVo rolePowerRelVo = new RolePowerRelVo();
        if (roleDto!=null) {
            rolePowerRelVo.setRoleId(roleDto.getId());
        }
        Criteria criteria = Criteria.from(RolePowerRel.class)
                .whioutId()
                .where()
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(rolePowerRelVo,criteria);
    }

    @Override
    public List<RolePowerRelVo> findPowersByPowerId(Integer powerId) throws Exception {
        RolePowerRelVo rolePowerRelVo = new RolePowerRelVo();
        rolePowerRelVo.setPowerId(powerId);
        Criteria criteria = Criteria.from(RolePowerRel.class)
                .whioutId()
                .where()
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(rolePowerRelVo,criteria);
    }

    public void setCrudTemplate(CrudTemplate crudTemplate) {
        this.crudTemplate = crudTemplate;
    }

    @Override
    public void deletePowersByRoleId(Integer roleId) throws Exception {
        RolePowerRel rolePowerRel = new RolePowerRel();
        Criteria criteria = Criteria.from(RolePowerRel.class)
                .whioutId()
                .where()
                .and("role_id",Operator.EQ,roleId)
                .endWhere();
        crudTemplate.delete(rolePowerRel,criteria);
    }
}
