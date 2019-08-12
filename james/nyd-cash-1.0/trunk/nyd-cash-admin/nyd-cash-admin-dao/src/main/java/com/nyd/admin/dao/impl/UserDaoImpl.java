package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.UserDao;
import com.nyd.admin.entity.power.User;
import com.nyd.admin.entity.power.UserRoleRel;
import com.nyd.admin.model.power.dto.UserDto;
import com.nyd.admin.model.power.vo.UserRoleRelVo;
import com.nyd.admin.model.power.vo.UserVo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Peng
 * @create 2017-12-19 15:22
 **/

@Repository
public class UserDaoImpl implements UserDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Override
    public List<UserVo> getUserInfo(UserVo vo) throws Exception {
        Criteria criteria = Criteria.from(User.class)
                .whioutId()
                .where()
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(vo,criteria);
    }


    @Override
    public void save(UserVo vo) throws Exception {
        User userInfo = new User();
        BeanUtils.copyProperties(vo, userInfo);
        crudTemplate.save(userInfo);
    }

    @Override
    public void update(UserVo vo) throws Exception {
        Criteria criteria = Criteria.from(User.class)
                .whioutId()
                .where().and("account_no", Operator.EQ,vo.getAccountNo())
                .endWhere();
        vo.setAccountNo(null);
        crudTemplate.update(vo,criteria);
    }


    @Override
    public void deleteRolesByUserId(Integer userId) throws Exception {
        UserRoleRel userRoleRel = new UserRoleRel();
        Criteria criteria = Criteria.from(UserRoleRel.class)
                .whioutId()
                .where()
                .and("user_id",Operator.EQ,userId)
                .endWhere();
        crudTemplate.delete(userRoleRel,criteria);
    }


    @Override
    public List<UserRoleRelVo> findRolesByUser(UserDto userDto) throws Exception {
        UserRoleRelVo userRoleRelVo = new UserRoleRelVo();
        if (userDto!=null) {
            userRoleRelVo.setUserId(userDto.getId());
        }
        Criteria criteria = Criteria.from(UserRoleRel.class)
                .whioutId()
                .where()
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(userRoleRelVo,criteria);
    }

    @Override
    public List<UserRoleRelVo> findRolesByRoleId(Integer roleId) throws Exception {
        UserRoleRelVo userRoleRelVo = new UserRoleRelVo();
        if (roleId!=null) {
            userRoleRelVo.setRoleId(roleId);
        }
        Criteria criteria = Criteria.from(UserRoleRel.class)
                .whioutId()
                .where()
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(userRoleRelVo,criteria);
    }

    /**
     * 更新催收人员信息
     * @param userVo
     * @return
     */
    @Override
    public void updateCollectionUser(UserVo userVo) throws Exception {
        crudTemplate.update(userVo, Criteria.from(User.class));
    }

    @Override
    public List<UserVo> getUserInfoByUserName(UserVo voquery) throws Exception {
        Criteria criteria = Criteria.from(User.class)
                .where().and("user_name", Operator.EQ,voquery.getUserName())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        UserVo userVo = new UserVo();
        return crudTemplate.find(userVo,criteria);
    }

}
