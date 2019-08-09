package com.tasfe.sis.auth.service.impls;

import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.auth.api.AuthContract;
import com.tasfe.sis.auth.model.AuthInfos;
import com.tasfe.sis.auth.service.AuthService;
import com.tasfe.sis.auth.service.ResourcesService;
import com.tasfe.zh.base.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhou on 17/6/29.
 */

@Component("AuthService")
public class AuthServiceImpl implements AuthService, AuthContract {
    private static Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    //@Autowired
    //private CrudService crudService;

    @Autowired
    private CrudTemplate crudTemplate;

    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private CacheService cacheService;

    @Override
    public boolean authentication(AuthInfos authInfos) {
//        // 1.对比数据库
//        Login account = new Login();
//        BeanUtils.copyProperties(authInfos,account);
//        List<Login> accounts = crudService.getEntitys(account);
//        if(accounts.size()<1){
//            // 是否返回提示信息
//            return false;
//        }
//        // 2.如果对比成果，去获取用户的角色,组装数据结构
//        LoginRole accountRole = new LoginRole();
//        accountRole.setAid(authInfos.getAid());
//        List<LoginRole> accountRoleList = crudService.getEntitys(accountRole);
//
//        // 3.根据角色Role 去获取角色所对应的资源，组装数据结构
//        List<RoleResources> roleResourcesList = new ArrayList<>();
//        accountRoleList.stream().forEach(ar->{
//            RoleResources roleResources = new RoleResources();
//            roleResources.setRoleId(ar.getRid());
//            roleResourcesList.add(roleResources);
//        });
//
//
//        List<MenuInfos> menuInfosList = resourcesService.getMenuInfos(null/*authInfos*/);
//        String json = JsonUtil.toJson(menuInfosList);
//        // 4.存放在自定义权限session
//        cacheService.set(ResourcesCache.arck.toString(),json);
        return true;
    }




/*
    @Autowired
    private RedisClientTemplate redisClientTemplate;
    @Resource
    UsersSessionMapper usersSessionMapper;



    @Override
    public String userLogin(AuthDTO authDTO){

        //生成code
        StringBuffer sb = new StringBuffer();
        sb.append(System.currentTimeMillis());
        sb.append(authDTO.getLoginName());
        sb.append(authDTO.getPassword());

        sb.append(System.currentTimeMillis());
        byte[] base64 = Base64.encodeBase64(sb.toString().getBytes());
        String code = DigestUtils.md5(base64).toString();

        //老会话信息失效
        UsersSession record = new UsersSession();
        record.setStatus(HljnsConstants.STATUS_ZERO);
        UsersSessionExample example = new UsersSessionExample();
        example.createCriteria().andUserIdEqualTo(authDTO.getUserId().longValue()).andStatusEqualTo(HljnsConstants.STATUS_ONE);
        usersSessionMapper.updateByExampleSelective(record,example);

        //保存新的code
        UsersSession userSession = new UsersSession();
        userSession.setCode(code);
        userSession.setStatus(HljnsConstants.STATUS_ONE);
        userSession.setUserId(authDTO.getUserId().longValue());
        int result = usersSessionMapper.insertSelective(userSession);
        if (result >= 1) {
            //key=UZONE-SESSIONID-${code}

            String key = HljnsConstants.PREFIX_REDIS_CODE + code;
            redisClientTemplate.set(key, authDTO.getUserId().toString());
            int seconds = HljnsServerConfig.getSessionTimeOutSeconds();//Integer.valueOf(CacheManager.getActiveMapValue("session_expiry_sec"));
            redisClientTemplate.expire(key, seconds);
            //更新redis的userId:code
            redisClientTemplate.set(String.valueOf(authDTO.getUserId()), code);
            return code;
        } else {
            return null;
        }

    }

    @Override
    public ValidResult validUserCode(String code) throws BizException {

        if (Strings.isNullOrEmpty(code)) {
            return new ValidResult(0, ErrorCode.RESULT_AUTH_CODE.getCode().toString(), ErrorCode.RESULT_AUTH_CODE.getMessage());
        }


        Integer userCode = validateUserCode(code);
        if (userCode == null) {
            return new ValidResult(0, ErrorCode.RESULT_TOKEN_EXPIRE.getCode().toString(), ErrorCode.RESULT_TOKEN_EXPIRE.getMessage());
        }
        return new ValidResult(userCode);
    }



    private Integer validateUserCode(String code) throws BizException {

        int sessionExpirySec = HljnsServerConfig.getSessionTimeOutSeconds();//CacheManager.getActiveMapValue("session_expiry_sec");
        String key = HljnsConstants.PREFIX_REDIS_CODE + code;

        if (redisClientTemplate.exists(key) && redisClientTemplate.get(key) != null) {
            String userId = redisClientTemplate.get(key);

            if (code.equals(redisClientTemplate.get(userId))) {
                redisClientTemplate.expire(key, sessionExpirySec);
                return Integer.valueOf(userId);
            } else {
                return null;
            }
        } else {
            UsersSessionExample example = new UsersSessionExample();
            example.createCriteria().andStatusEqualTo(HljnsConstants.STATUS_ONE).andCodeEqualTo(code);
            List<UsersSession> sessionList = usersSessionMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(sessionList)) {
                throw new BizException(ErrorCode.RESULT_TOKEN_EXPIRE);
            }

            UsersSession session = sessionList.get(0);
            Long nowTime = System.currentTimeMillis();
            Long interval = Long.valueOf(sessionExpirySec);
            if (nowTime - session.getUpdateTime().getTime() < interval*1000) {
                UsersSession tempSession = new UsersSession();
                tempSession.setId(session.getId());
                tempSession.setUpdateTime(new Date());
                usersSessionMapper.updateByPrimaryKeySelective(tempSession);
                return session.getUserId().intValue();
            } else {
                //老会话信息失效
                UsersSession tempSession = new UsersSession();
                tempSession.setId(session.getId());
                tempSession.setStatus(HljnsConstants.STATUS_ZERO);
                usersSessionMapper.updateByPrimaryKeySelective(tempSession);
                logger.debug(">>>>>>用户授权码过期!请重新登录!");
                throw new BizException(ErrorCode.RESULT_TOKEN_EXPIRE);
            }
        }
    }*/
}
