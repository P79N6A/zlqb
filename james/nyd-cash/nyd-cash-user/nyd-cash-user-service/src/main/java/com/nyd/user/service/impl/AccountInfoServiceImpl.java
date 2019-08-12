package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.user.api.AccountInfoApi;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.mapper.AccountMapper;
import com.nyd.user.entity.Account;
import com.nyd.user.model.AccountCache;
import com.nyd.user.service.AccountInfoService;
import com.tasfe.framework.redis.RedisService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("accountInfoApi")
public class AccountInfoServiceImpl implements AccountInfoService, AccountInfoApi {
    private static Logger LOGGER = LoggerFactory.getLogger(AccountInfoServiceImpl.class);
	private static final String ACCOUNT_CACHE_PREFIX = "nyd:account";
	private static final String LOGIN_TOKEN_PREFIX = "login:";

    @Autowired
    private AccountDao accountDao;
    
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private AccountMapper accountMapper;


    @Override
    public List<Account> findByAccountNumber(String accountNumber) throws Exception {
        return accountDao.getAccountsByAccountNumber(accountNumber);
    }
    @Override
    public List<Account> findByUserId(String userId) throws Exception {
    	return accountDao.getAccountsByUserId(userId);
    }

    @Override
    public void updateAccount(Account account) throws Exception {
        accountDao.updateAccountByAccountNumber(account);
    }
    @Override
    public void updateAccountByUserId(Account account) throws Exception {
    	accountDao.updateAccountByUserId(account);
    }

    @Override
    public void saveYmtAccount(Account account) throws Exception {
        accountDao.save(account);
    }

	@Override
	public AccountCache getAccountCacheFromRedis(String accountNumber) {
		AccountCache cache = new AccountCache();
		String temp = redisService.getString(ACCOUNT_CACHE_PREFIX + ":" + accountNumber);
		if(StringUtils.isEmpty(temp)) {
			return null;
		}else {
			cache = JSONObject.parseObject(temp, AccountCache.class);
		}
		return cache;
	}
	@Override
	public void removeAccountCacheFromRedis(String accountNumber) {
		redisService.remove(ACCOUNT_CACHE_PREFIX + ":" + accountNumber,2);
	}

	@Override
	public void saveAccountCacheInRedis(AccountCache cache) {
		try {
			redisService.setString(ACCOUNT_CACHE_PREFIX + ":" + cache.getAccountNumber(), JSON.toJSONString(cache), 10*24*60*60);
		} catch (Exception e) {
			LOGGER.error("插入redis缓存失败accountNumber：" + cache.getAccountNumber());
		}
	}

	@Override
	public void saveAccountInRedis(Account account, String password) {
		AccountCache cache = new AccountCache();
		try {
			BeanUtils.copyProperties(cache, account);
			cache.setPassword(password);
			redisService.setString(ACCOUNT_CACHE_PREFIX + ":" + account.getAccountNumber(), JSON.toJSONString(cache), 10*24*60*60);
		} catch (Exception e) {
			LOGGER.error("插入redis缓存失败accountNumber：" + account.getAccountNumber());
		}
	}

	@Override
	public void saveAccountInRedis(AccountCache cache) {
		try {
			redisService.setString(ACCOUNT_CACHE_PREFIX + ":" + cache.getAccountNumber(), JSON.toJSONString(cache), 10*24*60*60);
		} catch (Exception e) {
			LOGGER.error("插入redis缓存失败accountNumber：" + cache.getAccountNumber());
		}
	}

	@Override
	public List<AccountCache> getAccountList(Map<String, Object> params) {
		return accountMapper.getAccountList(params);
	}

	/*
	 * 客户账户信息存入redis
	 * (non-Javadoc)
	 * @see com.nyd.user.service.AccountInfoService#convertAccountInRedis(java.util.Map)
	 */
	@Override
	public void convertAccountInRedis(Map<String, Object> params) {
		LOGGER.info("同步账户信息到redis跑批开始：" + JSON.toJSONString(params));
		if(params.get("minId") != null) {
			boolean flag = true;
			Long minId = (Long)params.get("minId");
			Long maxId = (Long)params.get("maxId");
			List<AccountCache> list = null;
			Map<String, Object> req = new HashMap<String, Object>();
			while(flag) {
				req.put("minId", minId);
				if(maxId != null) {
					req.put("maxId", maxId);
				}
				try {
					list = accountMapper.getAccountList(req);
				}catch(Exception e) {
					LOGGER.error("查询账户列表异常：" + e.getMessage());
					break;
				}
				if(list != null && list.size() > 0 ) {
					for(AccountCache cache : list) {
						saveAccountInRedis(cache);
					}
					if(maxId == null) {
						minId = list.get(list.size()-1).getId();
						list.clear();
						continue;
					}
					if(list.get(list.size()-1).getId() < maxId) {
						minId = list.get(list.size()-1).getId();
						list.clear();
						continue;
					}else {
						flag = false;
						continue;
					}
				}else {
					flag = false;
					continue;
				}
			}
		}else {
			if(params.get("updateTime") != null) {
				List<AccountCache> list = null;
				try {
					list = accountMapper.getAccountList(params);
				}catch(Exception e) {
					LOGGER.error("查询账户列表异常：" + e.getMessage());
				}
				for(AccountCache cache : list) {
					saveAccountInRedis(cache);
				}
			}
		}
		LOGGER.info("同步账户信息到redis跑批结束！" );
	}

	@Override
	public String getLoginPrefix(String accountNum) {
		String pfx = LOGIN_TOKEN_PREFIX + accountNum + ":";
		return pfx;
	}

}
