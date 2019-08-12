package com.zhiwang.zfm.service.impl.redis;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zhiwang.zfm.common.util.DateUtils;
import com.zhiwang.zfm.common.util.RandomUtil;
import com.zhiwang.zfm.service.api.redis.RedisService;


/**
 * 封装redis 缓存服务器服务接口
 * 
 */
@Service
public class RedisServiceImpl implements RedisService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceImpl.class);

	
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static String redisCode = "utf-8";

    /**
     * @param key
     */
	public long del(final String... keys) {
        return (long)redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    result = connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(final byte[] key, final byte[] value, final long liveTime) {
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key, String value, long liveTime) {
        this.set(key.getBytes(), value.getBytes(), liveTime);
    }

    /**
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        this.set(key, value, 0L);
    }

    /**
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        this.set(key, value, 0L);
    }

    /**
     * @param key
     * @return
     */
    public String get(final String key) {
        Object obj = redisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				 try {
	                	if(key == null){
	                		return null;
	                	}
	                	byte[] value = connection.get(key.getBytes());
	                    return value == null ? null : new String(connection.get(key.getBytes()), redisCode);
	                } catch (UnsupportedEncodingException e) {
	                    e.printStackTrace();
	                }
	                return null;
			}
        });
        
        if(obj == null)
        	return null;
        
        return (String) obj;
    }
 

    /**
     * @param pattern
     * @return
     */
    public Set keys(String pattern) {
        return redisTemplate.keys(pattern);

    }

    /**
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        Object obj = redisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.exists(key.getBytes());
			}
        });
        if(obj == null) 
        	return false;
        
        return (boolean) obj;
    }

    /**
     * @return
     */
    public String flushDB() {
        Object obj = redisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
        });
        if(obj == null)
        	return "";
        
        return (String) obj;
    }

    /**
     * @return
     */
    public long dbSize() {
        Object obj = redisTemplate.execute(new RedisCallback() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
        if(obj == null) 
        	return 0l;
        
        return (long) obj;
    }

    /**
     * @return
     */
    public String ping() {
        Object obj = redisTemplate.execute(new RedisCallback() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                return connection.ping();
            }
        });
        if(obj == null) 
        	return null;
        
        return (String) obj;
    }
	
	@Override
	public synchronized String getSeriNo() {
		// 默认6位随机值 
		// key : seqNo  value : 
		String seqNo = RandomUtil.randomString(4);
		try {
			
			String todayTime = DateUtils.getCurrentTime(DateUtils.STYLE_3);
			String value = get("order-serino");
			// 为空时从01开始计算 
			if(value == null) {
				seqNo = "00000001";
			}else {
				// 时间戳 
				String timeStamp = value.substring(0,8);
				// 流水号 
				seqNo = value.substring(8,value.length());
				// 如果是当天 则累+即可
				if(timeStamp.equals(todayTime)) {
					seqNo = arithmeticNumber(seqNo);
				}else {
					seqNo = "00000001"; 
				}
			}
			set("order-serino",todayTime + seqNo,86400);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  seqNo;
	}
	/**
	 * 累加运算
	 * @param batchNum
	 * @return
	 */
	private static String arithmeticNumber(String batchNum) {
		if("99999999".equals(batchNum)) 
			return "00000001";
		
		int begin = Integer.parseInt(batchNum) + 1;
		String result = begin + "";
		String resultNumber = "";
		if(result.length() < 8) {
			int j = 8 - result.length();
			for(int i = 0;i < j ;i ++){
				resultNumber += "0";
			}
			resultNumber += begin;
		}else{
			resultNumber = result;
		}
		return resultNumber;
	}

	
	/**
	 * 生成8位订单编号 
	 */
	@Override
	public synchronized String getOrderNumber() {
		String seqNo = "";
		String startValue ="";
		try {
			
			String value = get("order-applay-number");
			// 为空时从01开始计算 
			if(value == null) {
				seqNo = "00000001";
			}else {
				// 流水号 
				if("99999999".equals(value)) {
					seqNo = "00000001";
				}else{
					int begin = Integer.parseInt(value) + 1;
					String result = begin + "";
					if(result.length() < 8) {
						int j = 8 - result.length();
						for(int i = 0;i < j ;i ++){
							seqNo += "0";
						}
						seqNo += begin;
					}else{
						seqNo = result;
					}
				}
			}
			set("order-applay-number",startValue+seqNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return startValue+seqNo;
	}
	
	
	
}
