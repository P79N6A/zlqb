package com.nyd.admin.service.impl;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.UserDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.PowerMapper;
import com.nyd.admin.dao.mapper.UserMapper;
import com.nyd.admin.dao.mapper.UserRoleRelMapper;
import com.nyd.admin.entity.power.User;
import com.nyd.admin.entity.power.UserRoleRel;
import com.nyd.admin.model.LoginVo;
import com.nyd.admin.model.power.UserPowerInfo;
import com.nyd.admin.model.power.dto.UserDto;
import com.nyd.admin.model.power.dto.UserRoleDto;
import com.nyd.admin.model.power.vo.UserVo;
import com.nyd.admin.service.CrudService;
import com.nyd.admin.service.UserService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.AdminProperties;
import com.nyd.admin.service.utils.MD5Util;
import com.nyd.admin.service.utils.UserStruct;
import com.tasfe.framework.redis.RedisService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * @author Peng
 * @create 2017-12-19 15:33
 **/
@Service
public class UserInfoServiceImpl extends CrudService<UserMapper,User> implements UserService {
    private static Logger LOGGER = LoggerFactory.getLogger(TransformReportServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    PowerMapper powerMapper;

    @Autowired
    private UserRoleRelMapper userRoleRelMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AdminProperties adminProperties;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData getUserInfo(UserDto userDto) {
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(userDto,vo);
        ResponseData responseData = ResponseData.error();
        //登录超时时间设置
        Integer expire = 1;
        try {
            expire = Integer.parseInt(adminProperties.getLoginTimeout());
        } catch (NumberFormatException e) {
            LOGGER.error("get login expire error! accountNo is " + userDto.getAccountNo());
        }
        try {
            vo.setPassword(MD5Util.MD5(vo.getPassword()));
            List<UserVo> userVos = userDao.getUserInfo(vo);
                if(userVos.size()>0) {
                    String redisKey = "admin"+userDto.getAccountNo();
                    String token = redisKey+System.currentTimeMillis();
                    //进行base64编码
                    String encodeToken = Base64.getEncoder().encodeToString(token.getBytes("utf-8"));
                    redisService.remove(redisKey,3);
                    redisService.setString(redisKey,encodeToken,expire*1800);
                    responseData = ResponseData.success();
                    LoginVo loginVo = new LoginVo();
                    loginVo.setAccountId(userVos.get(0).getId());
                    loginVo.setAccountNo(userDto.getAccountNo());
                    loginVo.setToken(encodeToken);
                    responseData.setData(loginVo);
                }
            }catch (Exception e) {
            LOGGER.error("query userInfo error!",e);
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData saveUserInfo(UserDto userDto) {
        ResponseData responseData = ResponseData.success();
        //先根据accountNo查看是否有此账号
        UserVo voquery = new UserVo();
        voquery.setAccountNo(userDto.getAccountNo());
        try {
            List<UserVo> userVos = userDao.getUserInfo(voquery);
            if (userVos!=null&&userVos.size()>0) {
                responseData = ResponseData.error("此账号已存在");
                return responseData;
            }
        } catch (Exception e) {
            LOGGER.error("getUserInfo error!", e);
            responseData = ResponseData.error("服务器开小差了");
            return responseData;
        }
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(userDto, vo);
        try {
            vo.setPassword(MD5Util.MD5(vo.getPassword()));
            userDao.save(vo);
        }catch (Exception e) {
            LOGGER.error("saveUserInfo error!", e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public boolean updateUserInfo(UserDto userDto) {
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(userDto, vo);
        boolean saveFlag = false;
        try {
            if(vo.getPassword() != null && !"".equals(vo.getPassword())){
                vo.setPassword(MD5Util.MD5(vo.getPassword()));
            }
            userDao.update(vo);
            saveFlag = true;
        }catch (Exception e) {
            LOGGER.error("updateUserInfo error!", e);
        }
        return saveFlag;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public PageInfo<UserVo> getUserLs(UserDto userDto) throws ParseException {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        PageInfo<User> userPageInfo = this.findPage(userDto,user);

        return UserStruct.INSTANCE.poPage2VoPage(userPageInfo);
    }


    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public List<UserPowerInfo>  getUserPowerVoByUserId(UserDto userDto) {
        List<UserPowerInfo> userPowerInfos = new ArrayList<UserPowerInfo>();
        if("admin".equals(userDto.getAccountNo())){
            userPowerInfos  = powerMapper.queryPower();
        }else {
            userPowerInfos = userMapper.getUserPowerVoByUserId(userDto.getId());
        }

        List<Integer> powerList = new ArrayList<>();
        if(userPowerInfos != null && userPowerInfos.size() > 0){
            for(UserPowerInfo userPowerInfo : userPowerInfos) {
                powerList.add(userPowerInfo.getPowerId().intValue());
            }
        }

        //获取权限对应的父节点
        Set<Integer> pidSave = new HashSet<>();
        Set<Integer> pidSet = powerMapper.getPid(powerList);
        pidSave.addAll(pidSet);
        while (jugePid(pidSet)) {
            pidSet =  powerMapper.getPid(new ArrayList<Integer>(pidSet));
            pidSave.addAll(pidSet);
        }
        pidSave.remove(0);
        pidSave.addAll(powerList);

        powerList = new ArrayList<Integer>(pidSave);


        userPowerInfos = userMapper.getUserPowerVoByPowerId(powerList);

        return userPowerInfos ;
    }

    private boolean jugePid( Set<Integer> pidSet) {
        if (pidSet!=null&&pidSet.size()>0) {
            if (pidSet.size()==0) {
                for (Integer a:pidSet) {
                    if (a==0) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData editUserRole(UserRoleDto userRoleDto) {
        ResponseData responseData = ResponseData.success();
        //首先删除角色对应的原有权限
        try {
            userDao.deleteRolesByUserId(userRoleDto.getId());
        } catch (Exception e) {
            LOGGER.error("deleteRolesByUserId has exception",e);
            responseData = ResponseData.error("服务器开小差了");
            return responseData;
        }
        //在去保存
        List<UserRoleRel> list = new ArrayList<>();
        if (userRoleDto != null) {
            if (userRoleDto.getRoleList() != null && userRoleDto.getRoleList().size() > 0) {
                for (Integer roleId : userRoleDto.getRoleList()) {
                    UserRoleRel userRoleRel = new UserRoleRel();
                    userRoleRel.setUserId(userRoleDto.getId());
                    userRoleRel.setRoleId(roleId);
                    list.add(userRoleRel);
                }
            }
        }
        try {
            userRoleRelMapper.insertUserRoleList(list);
        } catch (Exception e) {
            LOGGER.error("insertUserRoleList has exception",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData findUserByName(String userName) {
        ResponseData responseData = ResponseData.success();
        //根据userName去查找具体对象
        UserVo voquery = new UserVo();
        voquery.setUserName(userName);

        try {
//            List<UserVo> userVos = userDao.getUserInfo(voquery);
//            List<UserVo> userVos = userDao.getUserInfoByUserName(voquery);
            List<User> userVos = userMapper.getUserInfoByUserName(voquery);
            if (userVos!=null && userVos.size()>0) {
                responseData.setData(userVos);
            }else {
                responseData = ResponseData.error("请检查输入的姓名是否有误");
            }
        } catch (Exception e) {
            LOGGER.error("getUserInfo error!", e);
            responseData = ResponseData.error("服务器开小差了");
            //return responseData;
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData findRolesByUser(UserDto userDto) {
        ResponseData responseData = ResponseData.success();
        try {
            responseData.setData(userDao.findRolesByUser(userDto));
        } catch (Exception e) {
            LOGGER.error("findRolesByUser has exception",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }
}
