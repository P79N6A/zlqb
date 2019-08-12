package com.tasfe.sis.auth.service.impls;

import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.zh.base.service.CacheService;
import com.tasfe.zh.base.service.CrudService;
import com.tasfe.zh.base.service.impls.CrudServiceImpl;
import com.nyd.user.entity.UserSession;
import com.tasfe.sis.auth.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by zhou on 17/6/26.
 */

@Service
public class SessionServiceImpl extends CrudServiceImpl<UserSession, UserSession, Long> implements CrudService<UserSession, UserSession, Long>, SessionService {

    private static Logger LOGGER = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CrudTemplate crudTemplate;


/*
    @Override
    public String userLogin(User user) throws Exception {
        //生成code
        StringBuffer sb = new StringBuffer();
        sb.append(System.currentTimeMillis());
        sb.append(user.getName());
        sb.append(user.getPwd());
        sb.append(System.currentTimeMillis());
        byte[] base64 = Base64.encodeBase64(sb.toString().getBytes());
        String code = DigestUtils.md5(base64).toString();
        //老会话信息失效
        UserSession root = new UserSession();
        List<UserSession> temptList = crudTemplate.find(root, Criteria.from(UserSession.class).where().and("status", Operator.EQ, 1).and("user_id", Operator.EQ, user.getId()).endWhere());
        for (UserSession temptSession:temptList) {
            temptSession.setStatus(0);
        }
        if(temptList.size()>0) {
            crudTemplate.update(temptList, Criteria.from(UserSession.class).fields("status"));
        }
        //保存新的code
        UserSession userSession = new UserSession();
        userSession.setCode(code);
        userSession.setStatus(HljnsConstants.STATUS_ONE);
        userSession.setUserId(user.getId().longValue());
        try {
            crudTemplate.save(userSession);
            String key = HljnsConstants.PREFIX_REDIS_CODE + code;
            cacheService.set(key, user.getId().toString());
            int seconds = new Integer(HljnsConstants.SESSION_TIMEOUT);//Integer.valueOf(CacheManager.getActiveMapValue("session_expiry_sec"));
            cacheService.expire(key, seconds);
            //更新redis的userId:code
            cacheService.set(String.valueOf(user.getId()), code);
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer validateSessionCode(String sessionCode) throws Exception {

        int sessionExpirySec = new Integer(HljnsConstants.SESSION_TIMEOUT);//CacheManager.getActiveMapValue("session_expiry_sec");
        String key = HljnsConstants.PREFIX_REDIS_CODE + sessionCode;
        if (cacheService.exists(key) && cacheService.get(key) != null) {
            String userId = cacheService.get(key);

            if (sessionCode.equals(cacheService.get(userId))) {
                cacheService.expire(key, sessionExpirySec);
                return Integer.valueOf(userId);
            } else {
                return null;
            }
        } else {
            UserSession example = new UserSession();
            example.setStatus(HljnsConstants.STATUS_ONE);
            example.setCode(sessionCode);
            List<UserSession> sessionList = crudTemplate.find(example,Criteria.from(UserSession.class).where().and("status",Operator.EQ).and("code",Operator.EQ).endWhere());
            if((sessionList == null)||(sessionList.size() == 0)) {
                throw new Exception();
            }
            UserSession session = sessionList.get(0);
            Long nowTime = System.currentTimeMillis();
            Long interval = Long.valueOf(sessionExpirySec);
            if (nowTime - session.getUpdateTime().getTime() < interval*1000) {
                UserSession tempSession = new UserSession();
                tempSession.setId(session.getId());
                tempSession.setUpdateTime(new Date());
                crudTemplate.update(tempSession,Criteria.from(UserSession.class).where().and("id",Operator.EQ).endWhere());
                return session.getUserId().intValue();
            } else {
                //老会话信息失效
                UserSession tempSession = new UserSession();
                tempSession.setId(session.getId());
                tempSession.setStatus(HljnsConstants.STATUS_ZERO);
//                super.getGenericCrudRepository().update(tempSession);
                crudTemplate.update(tempSession,Criteria.from(UserSession.class).where().and("id",Operator.EQ).endWhere());
                LOGGER.debug(">>>>>>用户授权码过期!请重新登录!");
                throw new Exception();
            }
        }
    }*/

}
