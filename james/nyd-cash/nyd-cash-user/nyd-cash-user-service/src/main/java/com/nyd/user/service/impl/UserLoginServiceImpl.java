package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.library.api.LibraryContract;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.InnerWhiteUserDao;
import com.nyd.user.dao.PasswordDao;
import com.nyd.user.dao.PhoneRegionDao;
import com.nyd.user.dao.mapper.GeetestConfigMapper;
import com.nyd.user.dao.mapper.UserSourceMapper;
import com.nyd.user.entity.*;
import com.nyd.user.model.*;
import com.nyd.user.model.enums.LoginCode;
import com.nyd.user.model.enums.PasswordCode;
import com.nyd.user.model.enums.RegisterCode;
import com.nyd.user.model.mq.RegisterToHitAccountMessage;
import com.nyd.user.model.request.HitRequest;
import com.nyd.user.service.AccountInfoService;
import com.nyd.user.service.NydHitLogicService;
import com.nyd.user.service.UserLoginService;
import com.nyd.user.service.geetest.GeeTestApi;
import com.nyd.user.service.mq.HitAccountProducer;
import com.nyd.user.service.mq.UserLoginLogProducer;
import com.nyd.user.service.run.LibraryRunnable;
import com.nyd.user.service.util.Md5Util;
import com.nyd.user.service.util.RandomUtil;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.redis.RedisService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dengw on 17/11/3.
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {
    private static Logger LOGGER = LoggerFactory.getLogger(UserLoginServiceImpl.class);
    private static final String PHONE_REGEX = "^1\\d{10}$";
    private static final String NYD_MD5_CODE = "nyd23888000587";
    private static final String ACCOUNT_CACHE_PREFIX = "nyd:account";

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private PhoneRegionDao phoneRegionDao;

    @Autowired
    private InnerWhiteUserDao innerWhiteUserDao;
    
    @Autowired
    private PasswordDao passwordDao;
    @Autowired
    private RedisService redisService;
    @Autowired(required = false)
    private ISendSmsService sendSmsService;
    @Autowired
    private Md5Util md5Util;
    @Autowired
    private UserProperties userProperties;
    @Autowired
    private UserSourceMapper userSourceMapper;
    @Autowired
    private LibraryContract libraryContract;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private HitAccountProducer hitAccountProducer;
    @Autowired
    private UserLoginLogProducer userLoginLogProducer;
    @Autowired
    private NydHitLogicService nydHitLogicService;
    @Autowired
    private GeeTestApi geeTestApi;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private GeetestConfigMapper mapper;

    /**
     * 短信发送
     *
     * @param smsRequest
     * @return
     */
    @Override
    public ResponseData sendMsgCode(SmsInfo smsRequest) {
        LOGGER.info("begin to send msgCode, smsRequest is " + smsRequest.toString());
        ResponseData test = ifGeetest(smsRequest.getSole(), smsRequest.getAppName(), smsRequest.getDeviceId());
        if (test != null) return test;
        ResponseData responseData = null;
        SmsRequest sms = new SmsRequest();
        sms.setSmsType(Integer.valueOf(smsRequest.getSmsType()));
        sms.setCellphone(smsRequest.getMobile());
        sms.setAppName(smsRequest.getAppName());
        try {
            responseData = sendSmsService.sendSingleSms(sms);
            redisTemplate.delete("geeTest:second" + smsRequest.getDeviceId());
            LOGGER.info("send msgCode success");
        } catch (Exception e) {
            LOGGER.error("send msgCode error!", e);
        }
        return responseData;
    }

    /**
     * 登录
     *
     * @param accountInfo BaseInfo.code  100 注册成功; 200 数据库操作异常 ; 300 密码错误; 400 账号不存在
     * @return BaseInfo
     */
    @Override
    public ResponseData login(AccountInfo accountInfo, String ip) {
        LOGGER.info("begin to login, accountNumber is " + JSON.toJSONString(accountInfo));
        //此处限制用户(上海 北京 深圳)注册；
//        try {
//            LOGGER.info("begin to register, 截取前面7位 is " + accountInfo.getAccountNumber().substring(0,7));
//            PhoneRegion phoneRegion = phoneRegionDao.getPhoneRegionsByMobile(accountInfo.getAccountNumber().substring(0,7));
//            LOGGER.info("login；! phoneRegion" + phoneRegion.toString());
////            if(phoneRegion.getCity().equals("上海")||phoneRegion.getCity().equals("北京")||phoneRegion.getCity().equals("深圳")){
//                //判断是否是白名单用户；
//                List<InnerWhiteUser> mobiles = innerWhiteUserDao.getObjectsByMobile(accountInfo.getAccountNumber());
//                if(mobiles==null || mobiles.size()==0){
//                    return ResponseData.error(RegisterCode.NOT_SUPPORT.getMsg());
//                }
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ResponseData test = ifGeetest(accountInfo.getSole(), accountInfo.getAppName(), accountInfo.getDeviceId());
//        if (test != null) return test;
        ResponseData responseData = ResponseData.success();
        // 验证账号是否存在
        BaseInfo baseInfo = null;
        Account accoutTemp = new Account();
        try {
            baseInfo = accountIfExist(accountInfo.getAccountNumber(), accoutTemp);
        } catch (Exception e) {
            LOGGER.error("get accountInfo from DB error! accountNumber is " + accountInfo.getAccountNumber(), e);
            return ResponseData.error(LoginCode.DB_ERROR.getMsg());
        }
        LoginLog loginLog = new LoginLog();
        loginLog.setAccountNumber(accountInfo.getAccountNumber());
        loginLog.setIp(ip);
        if (StringUtils.isNotBlank(accountInfo.getAppName())) {
            loginLog.setAppName(accountInfo.getAppName());
        }
        // 账号存在
        if (baseInfo.getExistFlag()) {
            try {
                //List<Account> accountList = accountDao.getAccountsByAccountNumber(accountInfo.getAccountNumber());
                Account account = accoutTemp;
				/*if (accountList != null && accountList.size() > 0) {
					account = accountList.get(0);
				}*/
                if (account != null) {
                    loginLog.setUserId(account.getUserId());
                    loginLog.setSource(account.getSource());
                    loginLog.setChannel(account.getChannel());
                    accountInfo.setUserId(account.getUserId());
                }
                // 账号存在则进行密码验证
                boolean verifyFlag = verifyPassword(accountInfo);
                if (verifyFlag) { // 密码正确
//                    redisTemplate.delete("geeTest:second" + accountInfo.getDeviceId());
                    String token = getMd5Token(account, accountInfo);
                    //删除登录token
                    //removeOtherToken(accountInfo.getAccountNumber());
                    // 判断redis中是否存在此用户登录 ?为何做一次判断
                    if (verifyRedisExist(accountInfo.getAccountNumber())) {
                        // 如果有登录，此时踢掉之前设备的登录
                        try {
                            redisService.remove(accountInfo.getAccountNumber(), 3);
                        } catch (Exception e) {
                            LOGGER.error("login redis remove error!");
                        }
                        baseInfo.setUserToken(token);
                        //baseInfo.setUserToken(accountInfo.getAccountNumber());
                        responseData = responseData.setData(baseInfo);
                        setRedisValue(accountInfo, token);
                        setRedisValue(accountInfo);
                        updateLastActiveTime(accountInfo.getAccountNumber());
                    } else {
                        baseInfo.setUserToken(token);
                        //baseInfo.setUserToken(accountInfo.getAccountNumber());
                        responseData = responseData.setData(baseInfo);
                        setRedisValue(accountInfo, token);
                        setRedisValue(accountInfo);
                        //setRedisValue(accountInfo);
                        updateLastActiveTime(accountInfo.getAccountNumber());
                    }
                    loginLog.setStatus("0");
                } else {
                    // 密码错误
                    responseData = ResponseData.error(LoginCode.PASSWORD_ERROR.getMsg());
                    loginLog.setStatus("1");
                }
            } catch (Exception e) {
                LOGGER.error("verifyPassword error! accountNumber is " + accountInfo.getAccountNumber(), e);
                loginLog.setStatus("1");
                responseData = ResponseData.error(LoginCode.DB_ERROR.getMsg());
            }
        } else {
            // 账号不存在
            LOGGER.info("this user is not registered , accountNumber is" + accountInfo.getAccountNumber());
            loginLog.setStatus("2");
            responseData = ResponseData.error(LoginCode.ACCOUNT_NOT_EXISTS.getMsg());
        }
        try {
            //loginLogDao.save(loginLog);
            //账号不存在不存登录日志表
            if (loginLog.getStatus() != null && !loginLog.getStatus().equals("2")) {
                userLoginLogProducer.sendUserLoginLogMsg(loginLog);
            }
        } catch (Exception e) {
            LOGGER.error("save loginlog has exception,accountNumber is " + accountInfo.getAccountNumber(), e);
        }
        return responseData;
    }

    private void removeOtherToken(String accountNumber) {
        String pfx = accountInfoService.getLoginPrefix(accountNumber);
        if (pfx != null && pfx != "" && accountNumber != null && accountNumber != "" && pfx.indexOf("login") >= 0) {
            Set<String> keys = redisTemplate.keys(pfx + "*");
            redisTemplate.delete(keys);
        }
    }

    /**
     * 获取MD5 token
     *
     * @param account
     * @param accountInfo
     * @return
     * @throws Exception
     */
    private String getMd5Token(Account account, AccountInfo accountInfo) throws Exception {
        Long time = new Date().getTime();
        String token = "";
        if (account == null) {
            return accountInfo.getDeviceId();
        }
        if (StringUtils.isEmpty(account.getUserId())) {
            if (account.getId() == null) {
                return accountInfo.getDeviceId();
            }
            token = Md5Util.getMD5(account.getId() + NYD_MD5_CODE + time);
        } else {
            token = Md5Util.getMD5(account.getUserId() + NYD_MD5_CODE + time);
        }
        return token;
    }

    /**
     * 将登录账号设备设置redis,redis值为token值
     *
     * @param accountInfo
     */
    private void setRedisValue(AccountInfo accountInfo, String token) {
        // 登录超时时间设置
        Integer expire = Integer.parseInt(userProperties.getLoginTimeout());
        if (expire == null) {
            LOGGER.warn("no set login timeout,please set expire !");
            expire = 1;
        }
        LoginInfo info = new LoginInfo();
        info.setUserId(accountInfo.getUserId());
        info.setDeviceId(accountInfo.getDeviceId());
        info.setAccountNumber(accountInfo.getAccountNumber());
        try {
            String pfx = accountInfoService.getLoginPrefix(accountInfo.getAccountNumber());
            redisService.setString(pfx + token, JSON.toJSONString(info), expire * 1800);
            //redisService.setString(accountInfo.getAccountNumber()+"NYD", token, expire * 1800);
        } catch (Exception e) {
            LOGGER.error("set redisValue error! key is " + accountInfo.getAccountNumber(), e);
        }
    }

    /**
     * 判断账户信息是否存在
     *
     * @param accountNumber
     * @param accoutTemp
     * @return
     * @throws Exception
     */
    private BaseInfo accountIfExist(String accountNumber, Account accoutTemp) throws Exception {
        LOGGER.info("begin to judge accountIfExist, accountNumber is " + accountNumber);
        BaseInfo baseInfo = new BaseInfo();
        //先查缓存是否存在
        AccountCache cache = accountInfoService.getAccountCacheFromRedis(accountNumber);
        if (cache != null) {
            baseInfo.setExistFlag(true);
            baseInfo.setUserId(cache.getUserId());
            baseInfo.setAccountNumber(cache.getAccountNumber());
            BeanUtils.copyProperties(accoutTemp, cache);
            return baseInfo;
        }
        List<Account> accountList = accountDao.getAccountsByAccountNumber(accountNumber);
        if (accountList != null && accountList.size() > 0) {
            Account account = accountList.get(0);
            BeanUtils.copyProperties(accoutTemp, account);
            baseInfo.setExistFlag(true);
            baseInfo.setUserId(account.getUserId());
            baseInfo.setAccountNumber(account.getAccountNumber());
            accountInfoService.saveAccountInRedis(account, null);
        } else {
            baseInfo.setExistFlag(false);
        }
        return baseInfo;
    }

    private ResponseData getTest(String sole, String key) {
        //即验判断
        try {
            String sole1 = (String) redisTemplate.opsForValue().get("geeTest:second" + key);

            if (StringUtils.isBlank(sole1)) {
                return ResponseData.error("极验验证失败");
            }
            if (!sole1.equals(sole)) {
                return ResponseData.error("极验验证失败");
            }
            return ResponseData.success();
        } catch (Exception e) {
            LOGGER.error("极验验证失败", e);
            return ResponseData.error("极验验证失败");
        }
    }

    /*
     *
     * Title: messageRegisterOrLogin Description:
     *
     * @param accountInfo
     *
     * @param ip
     *
     * @return
     */
    @Override
    public ResponseData messageRegisterOrLogin(AccountInfo accountInfo, String ip) {
        LOGGER.info("begin to messageRegisterOrLogin, accountInfo is " + JSON.toJSONString(accountInfo));
        //此处限制用户(上海 北京 深圳)注册；
//        try {
//            LOGGER.info("begin to register, 截取前面7位 is " + accountInfo.getAccountNumber().substring(0,7));
//            PhoneRegion phoneRegion = phoneRegionDao.getPhoneRegionsByMobile(accountInfo.getAccountNumber().substring(0,7));
//            LOGGER.info("messageRegisterOrLogin " + phoneRegion.toString());
////            if(phoneRegion.getCity().equals("上海")||phoneRegion.getCity().equals("北京")||phoneRegion.getCity().equals("深圳")){
//                //判断是否是白名单用户；
//                List<InnerWhiteUser> mobiles = innerWhiteUserDao.getObjectsByMobile(accountInfo.getAccountNumber());
//                if(mobiles==null || mobiles.size()==0){
//                    return ResponseData.error(RegisterCode.NOT_SUPPORT.getMsg());
//                }
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        ResponseData test = ifGeetest(accountInfo.getSole(), accountInfo.getAppName(), accountInfo.getDeviceId());
//        if(test != null) {
//            return test;
//        }
        ResponseData responseData = ResponseData.success();
        LOGGER.error("messageRegisterOrLogin geeTest:onePass"+redisTemplate.hasKey("geeTest:onePass" + accountInfo.getAccountNumber()));
        if (redisTemplate.hasKey("geeTest:onePass" + accountInfo.getAccountNumber())) {
            if (null == accountInfo || StringUtils.isBlank(accountInfo.getAccountNumber())) {
                LOGGER.error("参数不能为空");
                responseData = ResponseData.error("参数不能为空");
                return responseData;
            }
        }else{
            if (null == accountInfo || StringUtils.isBlank(accountInfo.getAccountNumber())
                    || StringUtils.isBlank(accountInfo.getSmsCode())) {
                LOGGER.error("参数不能为空");
                responseData = ResponseData.error("参数不能为空");
                return responseData;
            }

            // 校验验证码正确性
            /*if (!verifyMsgCode(accountInfo.getAccountNumber(), accountInfo.getSmsCode())) {
                LOGGER.info("register failed , msgCode is wrong ! mobile :{}", accountInfo.getAccountNumber());
                responseData = ResponseData.error(RegisterCode.SMSCODE_ERROR.getMsg());
                return responseData;
            }*/
        }
        // 检验手机号
        if (!accountInfo.getAccountNumber().matches(PHONE_REGEX)) {
            LOGGER.error("手机号不符合要求，手机号:{}", accountInfo.getAccountNumber());
            responseData = ResponseData.error(RegisterCode.PHONE_ERROR.getMsg());
            return responseData;
        }
        // 判断是登陆还是注册 如果手机号存在则为登录，否则为新户注册
        BaseInfo baseInfo = null;
        Account accoutTemp = new Account();
        try {
            // 从数据库查询账号信息
            baseInfo = accountIfExist(accountInfo.getAccountNumber(), accoutTemp);
        } catch (Exception e) {
            LOGGER.error("get accountInfo error! accountNumber is " + accountInfo.getAccountNumber(), e);
            return ResponseData.error(PasswordCode.DB_ERROR.getMsg());
        }
        if (baseInfo.getExistFlag()) { // 存在 直接登录
            LoginLog loginLog = new LoginLog();
            loginLog.setAccountNumber(accountInfo.getAccountNumber());
            loginLog.setIp(ip);
            loginLog.setAppName(accountInfo.getAppName());
            Account account = accoutTemp;
            try {
				/*AccountCache cache = getAccountCacheFromRedis(accountInfo.getAccountNumber());
				List<Account> accountList = accountDao.getAccountsByAccountNumber(accountInfo.getAccountNumber());
				if (accountList != null && accountList.size() > 0) {
					account = accountList.get(0);
				}*/
                if (account != null) {
                    loginLog.setUserId(account.getUserId());
                    loginLog.setSource(account.getSource());
                    accountInfo.setUserId(account.getUserId());
                }
            } catch (Exception e) {
                LOGGER.error("getAccountsByAccountNumber has exception! accountNumber is " + accountInfo.getAccountNumber(), e);
                return ResponseData.error(PasswordCode.DB_ERROR.getMsg());
            }
            String token = "";
            try {
                token = getMd5Token(account, accountInfo);
            } catch (Exception e) {
                LOGGER.error("获取token失败" + e.getMessage());
            }
            //删除登录token
            //removeOtherToken(accountInfo.getAccountNumber());
            // 判断redis中是否存在此用户登录
            if (verifyRedisExist(accountInfo.getAccountNumber())) {
                // 如果有登录，此时踢掉之前设备的登录
                try {
                    redisService.remove(accountInfo.getAccountNumber(), 3);
                } catch (Exception e) {
                    LOGGER.error("login redis remove error!");
                }
            }
            baseInfo.setUserToken(token);
            //baseInfo.setUserToken(accountInfo.getAccountNumber());
            responseData = responseData.setData(baseInfo);
            setRedisValue(accountInfo, token);
            setRedisValue(accountInfo);
            //setRedisValue(accountInfo);
            updateLastActiveTime(accountInfo.getAccountNumber());

            loginLog.setStatus("0");
            try {
                //loginLogDao.save(loginLog);
                userLoginLogProducer.sendUserLoginLogMsg(loginLog);
            } catch (Exception e) {
                LOGGER.error("save loginlog has exception,accountNumber is " + accountInfo.getAccountNumber(), e);
            }
            return responseData;
        }
        // 不存在 则注册
        String passwordToken = UUID.randomUUID().toString().replaceAll("-", "");
        baseInfo.setAccountNumber(accountInfo.getAccountNumber());
        baseInfo.setPasswordToken(passwordToken);
        responseData.setData(baseInfo);

        try {
            redisService.setString(accountInfo.getAccountNumber(), passwordToken, 120);
        } catch (Exception e) {
            LOGGER.error("redis has exception!", e);
        }
        return responseData;
    }

    private ResponseData ifGeetest(String sole, String appName, String key) {
        //查询所有配置极验的app
        List<String> strings = mapper.selectAllAppCode();
        if (strings.contains(appName)) {
            ResponseData test = getTest(sole, key);
            if (!"0".equals(test.getStatus())) {
                return test;
            }
        }
        return null;
    }

    /**
     * 免密登陆设置密码
     */
    @Override
    public ResponseData passwordSet(AccountInfo accountInfo, String ip) {
        LOGGER.info("begin to passwordSet, accountInfo is " + JSON.toJSONString(accountInfo));
        ResponseData responseData = ResponseData.success();
        if (accountInfo == null || StringUtils.isBlank(accountInfo.getAccountNumber())
                || StringUtils.isBlank(accountInfo.getPasswordToken())) {
            responseData = ResponseData.error("参数不能为空");
            return responseData;
        }
        if (StringUtils.isBlank(accountInfo.getAppName())) {
            accountInfo.setAppName("nyd");
        }
        String password = null;
        if (StringUtils.isBlank(accountInfo.getPassword())) {
            password = RandomUtil.flowPasswd(6);
        }
        // 检验手机号
        if (!accountInfo.getAccountNumber().matches(PHONE_REGEX)) {
            LOGGER.error("手机号不符合要求，手机号" + accountInfo.getAccountNumber());
            responseData = ResponseData.error(RegisterCode.PHONE_ERROR.getMsg());
            return responseData;
        }
        //判断缓存中是否存在账户信息
        if (accountInfoService.getAccountCacheFromRedis(accountInfo.getAccountNumber()) != null) {
            //账户已存在
            return ResponseData.error(RegisterCode.ACCOUNT_EXISTS.getMsg());
        }

        List<Account> accountList = null;
        try {
            accountList = accountDao.getAccountsByAccountNumber(accountInfo.getAccountNumber());
        } catch (Exception e) {
            LOGGER.error("get account error! accountNumber is" + accountInfo.getAccountNumber(), e);
            return ResponseData.error(RegisterCode.DB_ERROR.getMsg());
        }
        if (accountList != null && accountList.size() > 0) {
            // 账号已存在
            return ResponseData.error(RegisterCode.ACCOUNT_EXISTS.getMsg());
        }
        // 鉴权
        String passwordToken = redisService.getString(accountInfo.getAccountNumber());
        if (passwordToken != null && accountInfo.getPasswordToken().equals(passwordToken)) {
            // 账号未注册，生成账号ID
            Account account = new Account();
            account.setAccountNumber(accountInfo.getAccountNumber());
            account.setSource(accountInfo.getSource());
            try {
                // 保存账号信息
                accountDao.save(account);
                if (StringUtils.isNotBlank(password)) {
                    accountInfo.setPassword(password);
                } else {
                    accountInfo.setPassword(md5Util.getSecondMD5(accountInfo.getPassword()));
                }
                // 保存密码信息
                passwordDao.save(accountInfo);
                //账户信息保存redis缓存
                accountInfoService.saveAccountInRedis(account, accountInfo.getPassword());
                if (StringUtils.isBlank(accountInfo.getAppName())) {
                    accountInfo.setAppName("xxd");
                }
                // mq丢消息到library服务,进行撞库处理
                LOGGER.info("注册的用户丢进撞库线程进行处理,手机号为: " + accountInfo.getAccountNumber());
                LibraryRunnable runnable = new LibraryRunnable(libraryContract, accountInfo.getAccountNumber(),
                        accountInfo.getAppName(), accountInfo.getSource());
                threadPoolTaskExecutor.execute(runnable);

                // 添加至撞库信息中(注册成功发送mq 进行处理)
                RegisterToHitAccountMessage message = new RegisterToHitAccountMessage();
                message.setAccountNumber(accountInfo.getAccountNumber());
                message.setToken(null);
                hitAccountProducer.sendAddHitAccountMsg(message);

                // 如果appName不为空，插入一条数据到用户来源表
                if (accountInfo.getAppName() != null) {
                    try {
                        UserSource userSource = new UserSource();
                        userSource.setAccountNumber(accountInfo.getAccountNumber());
                        userSource.setSource(accountInfo.getSource());
                        userSource.setAppName(accountInfo.getAppName());
                        userSource.setOs(accountInfo.getOs());
                        // 保存账号信息
                        userSourceMapper.save(userSource);
                    } catch (Exception e) {
                        LOGGER.error("save userSource error! accountNumber is" + accountInfo.getAccountNumber(), e);
                        return ResponseData.error(RegisterCode.DB_ERROR.getMsg());
                    }
                }
                // 登录操作
                BaseInfo loginBaseInfo = new BaseInfo();
                loginBaseInfo.setAccountNumber(accountInfo.getAccountNumber());
                LoginLog loginLog = new LoginLog();
                loginLog.setAccountNumber(accountInfo.getAccountNumber());
                loginLog.setIp(ip);
                loginLog.setAppName(accountInfo.getAppName());
                loginLog.setSource(accountInfo.getSource());
                Long time = new Date().getTime();
                String token = "";
                //删除登录token
                removeOtherToken(accountInfo.getAccountNumber());
                // 判断redis中是否存在此用户登录
                if (verifyRedisExist(accountInfo.getAccountNumber())) {
                    // 如果有登录，此时踢掉之前设备的登录
                    try {
                        redisService.remove(accountInfo.getAccountNumber(), 3);
                    } catch (Exception e) {
                        LOGGER.error("login redis remove error!");
                    }
                }
                try {
                    if (StringUtils.isEmpty(account.getUserId())) {
                        token = Md5Util.getMD5(accountInfo.getAccountNumber() + NYD_MD5_CODE + time);
                    } else {
                        token = Md5Util.getMD5(account.getUserId() + NYD_MD5_CODE + time);
                    }
                } catch (Exception e) {
                    LOGGER.error("MD5加密异常：" + e.getMessage());
                }
                //baseInfo.setUserToken(token);
                //baseInfo.setUserToken(accountInfo.getAccountNumber());
                //responseData = responseData.setData(baseInfo);
                setRedisValue(accountInfo, token);
                setRedisValue(accountInfo);
                loginBaseInfo.setUserToken(token);
                responseData = responseData.setData(loginBaseInfo);
                //setRedisValue(accountInfo);
                updateLastActiveTime(accountInfo.getAccountNumber());

                loginLog.setStatus("0");
                try {
                    //loginLogDao.save(loginLog);
                    userLoginLogProducer.sendUserLoginLogMsg(loginLog);
                } catch (Exception e) {
                    LOGGER.error("save loginlog has exception,accountNumber is " + accountInfo.getAccountNumber(), e);
                }
                return responseData;
            } catch (Exception e) {
                LOGGER.error("save accountInfo error! accountNumber is" + accountInfo.getAccountNumber(), e);
                return ResponseData.error(RegisterCode.DB_ERROR.getMsg());
            }
        } else {
            responseData = ResponseData.error("请返回重新尝试！");
            return responseData;
        }
    }

    @Override
    public ResponseData geeTestPrepare(GeeTestDto dto) {
        LOGGER.info("begin geeTestPrepare ,dto is:" + dto.toString());
        //此处限制用户(上海 北京 深圳)注册；
//        try {
//            LOGGER.info("begin to register, 截取前面7位 is " + dto.getAccountNumber().substring(0,7));
//            PhoneRegion phoneRegion = phoneRegionDao.getPhoneRegionsByMobile(dto.getAccountNumber().substring(0,7));
//            LOGGER.info("messageRegisterOrLogin " + phoneRegion.toString());
////            if(phoneRegion.getCity().equals("上海")||phoneRegion.getCity().equals("北京")||phoneRegion.getCity().equals("深圳")){
//                //判断是否是白名单用户；
//                List<InnerWhiteUser> mobiles = innerWhiteUserDao.getObjectsByMobile(dto.getAccountNumber());
//                if(mobiles==null || mobiles.size()==0){
//                    return ResponseData.error(RegisterCode.NOT_SUPPORT.getMsg());
//                }
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            GeeTestRespone geeTestRespone = geeTestApi.firstVerify(dto);
            String sole = getUUID32();
            GeeTestResult result = new GeeTestResult();
            result.setSole(sole);
            result.setChallenge(geeTestRespone.getChallenge());
            result.setGt(geeTestRespone.getGt());
            result.setNew_captcha("true");
            result.setSuccess(geeTestRespone.getSuccess());
            LOGGER.info("geeTestPrepare result is:" + result.toString());
            return ResponseData.success(result);
        } catch (Exception e) {
            LOGGER.error("geeTestPrepare has exception,dto is:" + dto.toString(), e);
            return ResponseData.error("geeTestPrepare has exception");
        }
    }

    @Override
    public ResponseData geeTestSecond(GeeTestDto dto) {
        LOGGER.info("begin geeTestSecond ,dto is:" + dto.toString());
        try {
            geeTestApi.secondVerify(dto);
            String sole = getUUID32();
            GeeTestResult result = new GeeTestResult();
            result.setSole(sole);
            redisTemplate.opsForValue().set("geeTest:second" + dto.getDeviceId(), sole, 5, TimeUnit.MINUTES);
            LOGGER.info("geeTestSecond result is:" + result.toString());
            return ResponseData.success(result);
        } catch (Exception e) {
            LOGGER.error("geeTestSecond has exception,dto is:" + dto.toString(), e);
            return ResponseData.error("geeTestSecond has exception");
        }
    }

    @Override
    public ResponseData geeTestOnePass(GeeTestDto dto) {
        LOGGER.info("begin geeTestOnePass ,dto is:" + dto.toString());
        //此处限制用户(上海 北京 深圳)注册；
//        try {
//            LOGGER.info("begin to register, 截取前面7位 is " + dto.getAccountNumber().substring(0,7));
//            PhoneRegion phoneRegion = phoneRegionDao.getPhoneRegionsByMobile(dto.getAccountNumber().substring(0,7));
//            LOGGER.info("messageRegisterOrLogin " + phoneRegion.toString());
////            if(phoneRegion.getCity().equals("上海")||phoneRegion.getCity().equals("北京")||phoneRegion.getCity().equals("深圳")){
//                //判断是否是白名单用户；
//                List<InnerWhiteUser> mobiles = innerWhiteUserDao.getObjectsByMobile(dto.getAccountNumber());
//                if(mobiles==null || mobiles.size()==0){
//                    return ResponseData.error(RegisterCode.NOT_SUPPORT.getMsg());
//                }
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            int onePass = geeTestApi.onePass(dto);
            if (onePass == 1) {
                redisTemplate.opsForValue().set("geeTest:onePass" + dto.getAccountNumber(), onePass);
            }
            LOGGER.info("geeTestOnePass result is:" + onePass);
            Map<String, String> map = new HashMap<>(100);
            map.put("pass", String.valueOf(onePass));
            return ResponseData.success(map);
        } catch (Exception e) {
            LOGGER.error("geeTestOnePass has exception,dto is:" + dto.toString(), e);
            return ResponseData.error("geeTestOnePass has exception");
        }
    }


    private String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


    /**
     * 账户信息缓存至redis
     *
     * @param account
     * @param password
     */
    private void saveAccountInRedis(Account account, String password) {
        AccountCache cache = new AccountCache();
        try {
            BeanUtils.copyProperties(cache, account);
            cache.setPassword(password);
            redisService.setString(ACCOUNT_CACHE_PREFIX + ":" + account.getAccountNumber(), JSON.toJSONString(cache), 10 * 24 * 60 * 60);
        } catch (Exception e) {
            LOGGER.error("插入redis缓存失败accountNumber：" + account.getAccountNumber());
        }
    }

    /**
     * 获取用户账户redis缓存信息
     *
     * @param accountNumber
     * @return
     */
    private AccountCache getAccountCacheFromRedis(String accountNumber) {
        AccountCache cache = new AccountCache();
        String temp = redisService.getString(ACCOUNT_CACHE_PREFIX + ":" + accountNumber);
        if (StringUtils.isEmpty(temp)) {
            return null;
        } else {
            cache = JSONObject.parseObject(temp, AccountCache.class);
        }
        return cache;
    }

    /**
     * 注销
     *
     * @param accountNumber
     * @return boolean
     */
    @Override
    public ResponseData logout(String accountNumber) {
        LOGGER.info("begin to logout, accountNumber is " + accountNumber);
        ResponseData responseData = ResponseData.success();
        redisService.remove(accountNumber, 3);
        removeOtherToken(accountNumber);
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setLastActiveTime(new Date());
        try {
            accountDao.updateAccountByAccountNumber(account);
        } catch (Exception e) {
            LOGGER.error("update account lastActiveTime error! accountNumber is" + accountNumber, e);
            responseData = ResponseData.error("数据库操作异常");
        }
        return responseData;
    }

    /**
     * 注册
     *
     * @param accountInfo
     * @return String 100 注册成功; 200 数据库操作异常 ; 300 短信验证码错误; 400 账号已存在
     */
    @Override
    public ResponseData register(AccountInfo accountInfo) {
        LOGGER.info("begin to register, accountNumber is " + accountInfo.getAccountNumber());
        //此处限制用户(上海 北京 深圳)注册;
//        try {
//            LOGGER.info("begin to register, 截取前面7位 is " + accountInfo.getAccountNumber().substring(0,7));
//            PhoneRegion phoneRegion = phoneRegionDao.getPhoneRegionsByMobile(accountInfo.getAccountNumber().substring(0,7));
////            if(phoneRegion.getCity().equals("上海")||phoneRegion.getCity().equals("北京")||phoneRegion.getCity().equals("深圳")){
//                //判断是否是白名单用户；
//                List<InnerWhiteUser> mobiles = innerWhiteUserDao.getObjectsByMobile(accountInfo.getAccountNumber());
//                if(mobiles==null || mobiles.size()==0){
//                    return ResponseData.error(RegisterCode.NOT_SUPPORT.getMsg());
//                }
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        LOGGER.info("begin to register, accountNumber is " + accountInfo.getAccountNumber());
        ResponseData responseData = ResponseData.success();
        // 短信验证
        if (!verifyMsgCode(accountInfo.getAccountNumber(), accountInfo.getSmsCode())) {
            LOGGER.info("register failed , msgCode is wrong ! mobile" + accountInfo.getAccountNumber());
            return ResponseData.error(RegisterCode.SMSCODE_ERROR.getMsg());
        }
        if (StringUtils.isBlank(accountInfo.getAppName())) {
            accountInfo.setAppName("xxd");
        }
/*        //取消百度信息流引流页取消撞库规则
        if (accountInfo.getSource() != null && !accountInfo.getSource().startsWith("nyd_baidu_")) {
            // mq丢消息到library服务,进行撞库处理
            LOGGER.info("注册的用户丢进撞库线程进行处理,手机号为: " + accountInfo.getAccountNumber());
            LibraryRunnable runnable = new LibraryRunnable(libraryContract, accountInfo.getAccountNumber(),
                    accountInfo.getAppName(), accountInfo.getSource());
            threadPoolTaskExecutor.execute(runnable);
        }
        //撞库规则重新调整
        HitRequest request = new HitRequest();
        request.setAppName(accountInfo.getAppName());
        request.setMobile(accountInfo.getAccountNumber());
        request.setMobileType("1");
        request.setSource(accountInfo.getSource());
        nydHitLogicService.HitByMobileAndRule(request);*/
        List<Account> accountList = null;
        try {
            accountList = accountDao.getAccountsByAccountNumber(accountInfo.getAccountNumber());
        } catch (Exception e) {
            LOGGER.error("get account error! accountNumber is" + accountInfo.getAccountNumber(), e);
            return ResponseData.error(RegisterCode.DB_ERROR.getMsg());
        }
        if (accountList != null && accountList.size() > 0) {
            sendSms(accountInfo);
            // 账号已存在
            return ResponseData.error(RegisterCode.ACCOUNT_EXISTS.getMsg());
        } else {
            // 账号未注册，生成账号ID
            Account account = new Account();
            account.setAccountNumber(accountInfo.getAccountNumber());
            account.setSource(accountInfo.getSource());
            try {
                // 保存账号信息
                accountDao.save(account);
                String pwd = accountInfo.getPassword();
                if (StringUtils.isBlank(pwd)) {
                    pwd = RandomUtil.flowPasswd(6);
                    accountInfo.setPassword(md5Util.getSecondMD5(md5Util.getMD5(pwd)));
                } else {
                    accountInfo.setPassword(md5Util.getSecondMD5(accountInfo.getPassword()));
                }
                // 保存密码信息
                passwordDao.save(accountInfo);
                //账户信息保存redis缓存
                accountInfoService.saveAccountInRedis(account, accountInfo.getPassword());
            } catch (Exception e) {
                LOGGER.error("save accountInfo error! accountNumber is" + accountInfo.getAccountNumber(), e);
                return ResponseData.error(RegisterCode.DB_ERROR.getMsg());
            }
            // 如果appName不为空，插入一条数据到用户来源表
            if (accountInfo.getAppName() != null) {
                try {
                    UserSource userSource = new UserSource();
                    userSource.setAccountNumber(accountInfo.getAccountNumber());
                    userSource.setSource(accountInfo.getSource());
                    userSource.setAppName(accountInfo.getAppName());
                    userSource.setOs(accountInfo.getOs());
                    // 保存账号信息
                    userSourceMapper.save(userSource);
                } catch (Exception e) {
                    LOGGER.error("save userSource error! accountNumber is" + accountInfo.getAccountNumber(), e);
                    return ResponseData.error(RegisterCode.DB_ERROR.getMsg());
                }
            }
            sendSms(accountInfo);
        }
        return responseData;
    }

    /**
     * 查询
     *
     * @param accountInfo
     */
    @Override
    public ResponseData query(AccountInfo accountInfo) {
        LOGGER.info("begin to register, accountNumber is " + accountInfo.getAccountNumber());
        ResponseData responseData = ResponseData.success();
        List<Account> accountList = null;
        try {
            accountList = accountDao.getAccountsByAccountNumber(accountInfo.getAccountNumber());
            responseData.setData(accountList);
        } catch (Exception e) {
            LOGGER.error("get account error! accountNumber is" + accountInfo.getAccountNumber(), e);
            return ResponseData.error(RegisterCode.DB_ERROR.getMsg());
        }

        return responseData;
    }

    /**
     * 引流没短信注册
     *
     * @param accountInfo
     * @return String 100 注册成功; 200 数据库操作异常 ; 300 短信验证码错误; 400 账号已存在
     */
    @Override
    public ResponseData channelRegister(AccountInfo accountInfo) {
        LOGGER.info("begin to register, accountNumber is " + accountInfo.getAccountNumber());
        ResponseData responseData = ResponseData.success();
        List<Account> accountList = null;
        try {
            accountList = accountDao.getAccountsByAccountNumber(accountInfo.getAccountNumber());
        } catch (Exception e) {
            LOGGER.error("get account error! accountNumber is" + accountInfo.getAccountNumber(), e);
            return ResponseData.error(RegisterCode.DB_ERROR.getMsg());
        }
        if (StringUtils.isBlank(accountInfo.getAppName())) {
            accountInfo.setAppName("nyd");
        }
        //取消百度信息流引流页取消撞库规则
        if (accountInfo.getSource() != null && !accountInfo.getSource().startsWith("nyd_baidu_")) {
            // mq丢消息到library服务,进行撞库处理
            LOGGER.info("注册的用户丢进撞库线程进行处理,手机号为: " + accountInfo.getAccountNumber());
            LibraryRunnable runnable = new LibraryRunnable(libraryContract, accountInfo.getAccountNumber(),
                    accountInfo.getAppName(), accountInfo.getSource());
            threadPoolTaskExecutor.execute(runnable);
        }
        //撞库规则重新调整
        HitRequest request = new HitRequest();
        request.setAppName(accountInfo.getAppName());
        request.setMobile(accountInfo.getAccountNumber());
        request.setMobileType("1");
        request.setSource(accountInfo.getSource());
        nydHitLogicService.HitByMobileAndRule(request);
        if (accountList != null && accountList.size() > 0) {
            // 账号已存在
            return ResponseData.error(RegisterCode.ACCOUNT_EXISTS.getMsg());
        } else {
            // 账号未注册，生成账号ID
            Account account = new Account();
            account.setAccountNumber(accountInfo.getAccountNumber());
            account.setSource(accountInfo.getSource());
            try {
                // 保存账号信息
                accountDao.save(account);
                accountInfo.setPassword(md5Util.getSecondMD5(accountInfo.getPassword()));
                // 保存密码信息
                passwordDao.save(accountInfo);
                //账户信息保存redis缓存
                accountInfoService.saveAccountInRedis(account, accountInfo.getPassword());
            } catch (Exception e) {
                LOGGER.error("save accountInfo error! accountNumber is" + accountInfo.getAccountNumber(), e);
                return ResponseData.error(RegisterCode.DB_ERROR.getMsg());
            }
            // 如果appName不为空，插入一条数据到用户来源表
            if (accountInfo.getAppName() != null) {
                try {
                    UserSource userSource = new UserSource();
                    userSource.setAccountNumber(accountInfo.getAccountNumber());
                    userSource.setSource(accountInfo.getSource());
                    userSource.setAppName(accountInfo.getAppName());
                    userSource.setOs(accountInfo.getOs());
                    // 保存账号信息
                    userSourceMapper.save(userSource);
                } catch (Exception e) {
                    LOGGER.error("save userSource error! accountNumber is" + accountInfo.getAccountNumber(), e);
                    return ResponseData.error(RegisterCode.DB_ERROR.getMsg());
                }
            }
        }
        return responseData;
    }

    /**
     * 修改密码
     *
     * @param accountInfo
     * @return Integer 100 修改成功; 200 数据库操作异常 ; 400 账号不存在; 500 原有密码错误
     */
    @Override
    public ResponseData modifyPassword(AccountInfo accountInfo) {
        LOGGER.info("begin to modify password, accountNumber is " + accountInfo.getAccountNumber());
        ResponseData responseData = ResponseData.success();
        // 验证原始密码
        boolean verifyFlag = false;
        try {
            verifyFlag = verifyPassword(accountInfo);
        } catch (Exception e) {
            LOGGER.error("verifyPassword error! accountNumber is " + accountInfo.getAccountNumber(), e);
            // 数据库异常
            return ResponseData.error(PasswordCode.DB_ERROR.getMsg());
        }
        if (verifyFlag) {
            try {
                accountInfo.setPassword(md5Util.getSecondMD5(accountInfo.getNewPassword()));
                passwordDao.update(accountInfo);
                accountInfoService.removeAccountCacheFromRedis(accountInfo.getAccountNumber());//删除缓存中的数据
            } catch (Exception e) {
                LOGGER.info("update password failed ! mobile" + accountInfo.getAccountNumber());
                return ResponseData.error(PasswordCode.DB_ERROR.getMsg());
            }
        } else {
            // 密码错误
            return ResponseData.error(PasswordCode.PASSWORD_ERROR.getMsg());
        }

        return responseData;
    }

    /**
     * 忘记密码
     *
     * @param accountInfo
     * @return Integer 100 修改成功; 200 数据库操作异常 ; 300 短信验证码错误; 400 账号不存在
     */
    @Override
    public ResponseData forgetPassword(AccountInfo accountInfo) {
        LOGGER.info("begin to forget password, accountNumber is " + JSON.toJSONString(accountInfo));
//        ResponseData test = getTest(accountInfo);
//        if (!"0".equals(test.getStatus())){
//            return test;
//        }
        ResponseData responseData = ResponseData.success();
        // 忘记密码,进行短信校验
        if (!verifyMsgCode(accountInfo.getAccountNumber(), accountInfo.getSmsCode())) {
            LOGGER.info("modify password failed , msgCode is wrong ! mobile" + accountInfo.getAccountNumber());
            return ResponseData.error(PasswordCode.SMSCODE_ERROR.getMsg());
        } else {
            boolean existFlag = false;
            try {
                // 从数据库查询账号信息
                existFlag = accountIfExist(accountInfo.getAccountNumber()).getExistFlag();
            } catch (Exception e) {
                LOGGER.error("get accountInfo error! accountNumber is " + accountInfo.getAccountNumber(), e);
                return ResponseData.error(PasswordCode.DB_ERROR.getMsg());
            }
            // 验证是否有该账号
            if (existFlag) {
                try {
                    accountInfo.setPassword(md5Util.getSecondMD5(accountInfo.getPassword()));
                    passwordDao.update(accountInfo);
                    accountInfoService.removeAccountCacheFromRedis(accountInfo.getAccountNumber());
                } catch (Exception e) {
                    LOGGER.info("update password failed ! mobile" + accountInfo.getAccountNumber());
                    return ResponseData.error(PasswordCode.DB_ERROR.getMsg());
                }
            } else {
                // 账号不存在
                return ResponseData.error(PasswordCode.ACCOUNT_NOT_EXISTS.getMsg());
            }
        }

        return responseData;
    }

    /**
     * 判断是否登录超时
     *
     * @param accountNumber,deviceId
     * @return boolean false超时，true未超时
     */
    @Override
    public boolean judgeTimeout(String accountNumber, String deviceId) {
        if (StringUtils.isBlank(accountNumber) || StringUtils.isBlank(deviceId)) {
            return false;
        } else {
            try {
                String deviceIdInRedis = redisService.getString(accountNumber);
                if (StringUtils.isBlank(deviceIdInRedis)) {
                    return false;
                } else {
                    if (deviceId.equals(deviceIdInRedis)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("redis get error! key is " + accountNumber);
            }
        }
        return false;
    }

    /**
     * 校验短信验证码
     *
     * @param mobile
     * @param msgCode
     * @return boolean
     */
    public boolean verifyMsgCode(String mobile, String msgCode) {
        LOGGER.info("begin to verify msgCode, mobile is " + mobile);
        boolean verifyFlag = false;
        SmsRequest sms = new SmsRequest();
        sms.setSmsType(1);
        sms.setCellphone(mobile);
        try {
            ResponseData<String> responseData = sendSmsService.getVerifyCode(sms);
            String code = responseData.getData();
            if ("0".equals(responseData.getStatus())) {
                if (msgCode.equals(code)) {
                    verifyFlag = true;
                }
            }
            LOGGER.info("verify msgCode result is " + verifyFlag);
        } catch (Exception e) {
            LOGGER.error("verifyMsgCode error ! mobile is " + mobile);
        }
        return verifyFlag;
    }

    /**
     * 校验密码
     *
     * @param accountInfo
     * @return boolean
     */
    private boolean verifyPassword(AccountInfo accountInfo) throws Exception {
        LOGGER.info("begin to verify password, accountNumber is " + accountInfo.getAccountNumber());
        boolean verifyFlag = false;
        String password = null;
        //先查询缓存密码信息
        AccountCache cache = accountInfoService.getAccountCacheFromRedis(accountInfo.getAccountNumber());
        if (cache != null && !StringUtils.isEmpty(cache.getPassword())) {
            password = cache.getPassword();
            if (md5Util.getSecondMD5(accountInfo.getPassword()).equals(password)) {
                verifyFlag = true;
            }
            return verifyFlag;
        }
        List<Password> passwordList = passwordDao.getPasswordsByAccountNumber(accountInfo.getAccountNumber());
        if (passwordList != null && passwordList.size() > 0) {
            password = passwordList.get(0).getPassword();
        }
        if (md5Util.getSecondMD5(accountInfo.getPassword()).equals(password)) {
            if (cache != null) {
                cache.setPassword(password);
                accountInfoService.saveAccountCacheInRedis(cache);
            }
            verifyFlag = true;
        }
        LOGGER.info("verify password result is " + verifyFlag);
        return verifyFlag;
//        return true;
    }

    /**
     * 账号是否存在
     *
     * @param accountNumber
     * @return String
     */
    private BaseInfo accountIfExist(String accountNumber) throws Exception {
        LOGGER.info("begin to judge accountIfExist, accountNumber is " + accountNumber);
        BaseInfo baseInfo = new BaseInfo();
        //先查缓存是否存在
        AccountCache cache = accountInfoService.getAccountCacheFromRedis(accountNumber);
        if (cache != null) {
            baseInfo.setExistFlag(true);
            baseInfo.setUserId(cache.getUserId());
            baseInfo.setAccountNumber(cache.getAccountNumber());
            return baseInfo;
        }
        List<Account> accountList = accountDao.getAccountsByAccountNumber(accountNumber);
        if (accountList != null && accountList.size() > 0) {
            Account account = accountList.get(0);
            baseInfo.setExistFlag(true);
            baseInfo.setUserId(account.getUserId());
            baseInfo.setAccountNumber(account.getAccountNumber());
        } else {
            baseInfo.setExistFlag(false);
        }
        return baseInfo;
    }

    /**
     * 查看redis中是否存在此用户登录记录
     *
     * @return
     */
    private boolean verifyRedisExist(String accountNumber) {
        boolean flag = false;
        try {
            String deviceId = redisService.getString(accountNumber);
            if (deviceId != null && !"".equals(deviceId)) {
                flag = true;
            }
        } catch (Exception e) {
            LOGGER.error("verify redisExist error! key is " + accountNumber, e);
        }
        return flag;
    }

    /**
     * 判断redis中存在的设备号是否与登录的设备号一致
     *
     * @return
     */
    private boolean deviceIdIfEqual(AccountInfo accountInfo) {
        boolean flag = false;
        try {
            String deviceIdInRedis = redisService.getString(accountInfo.getAccountNumber());
            if (deviceIdInRedis != null && deviceIdInRedis.equals(accountInfo.getDeviceId())) {
                flag = true;
            }
        } catch (Exception e) {
            LOGGER.error("compare deviceId error! key is " + accountInfo.getAccountNumber(), e);
        }
        return flag;
    }

    /**
     * 将登录账号设备设置redis
     *
     * @param accountInfo
     */
    private void setRedisValue(AccountInfo accountInfo) {
        // 登录超时时间设置
        Integer expire = Integer.parseInt(userProperties.getLoginTimeout());
        if (expire == null) {
            LOGGER.warn("no set login timeout,please set expire !");
            expire = 1;
        }
        try {
            redisService.setString(accountInfo.getAccountNumber(), accountInfo.getDeviceId(), expire * 1800);
        } catch (Exception e) {
            LOGGER.error("set redisValue error! key is " + accountInfo.getAccountNumber(), e);
        }
    }

    /**
     * 更新最后活跃时间
     */
    private void updateLastActiveTime(String accountNumber) {
        LOGGER.info("begin to update lastActiveTime, accountNumber is " + accountNumber);
        Date date = new Date();
        //更新缓存用户最后登录时间
        AccountCache cache = accountInfoService.getAccountCacheFromRedis(accountNumber);
        if (cache != null) {
            cache.setLastActiveTime(date);
            accountInfoService.saveAccountInRedis(cache);
        }
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setLastActiveTime(date);
        try {
            accountDao.updateAccountByAccountNumber(account);
        } catch (Exception e) {
            LOGGER.error("update account lastActiveTime error! accountNumber is" + accountNumber, e);
        }
    }

    /**
     * 保存客户账户缓存
     *
     * @param cache
     */
    private void saveAccountInRedis(AccountCache cache) {
        try {
            redisService.setString(ACCOUNT_CACHE_PREFIX + ":" + cache.getAccountNumber(), JSON.toJSONString(cache), 10 * 24 * 60 * 60);
        } catch (Exception e) {
            LOGGER.error("插入redis缓存失败accountNumber：" + cache.getAccountNumber());
        }
    }

    /**
     * 发送IOS版授信教程短信
     */
    private void sendSms(AccountInfo accountInfo) {
        if (accountInfo == null) {
            return;
        }
        if (StringUtils.isNotBlank(accountInfo.getOs()) && "ios".equals(accountInfo.getOs())) {
            SmsRequest smsRequest = new SmsRequest();
            smsRequest.setAppName(accountInfo.getAppName());
            smsRequest.setSmsType(26);
            smsRequest.setCellphone(accountInfo.getAccountNumber());
            sendSmsService.sendSingleSms(smsRequest);
        }
    }

}
