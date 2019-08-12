package com.zhiwang.zfm.service.api.redis;

import java.util.Set;



/** 
 * redis操作类接口
 * @author chenbo-QQ381756915
 * @date 创建时间：2018年4月20日 下午3:14:56 
 * @version 1.0.0 
 * @parameter  
 * @throws 
 * @return  
 */
public interface RedisService {
	 /**
     * 通过key删除
     * 
     * @param key
     */
    public abstract long del(String... keys);

    /**
     * 添加key value 并且设置存活时间(byte)
     * 
     * @param key
     * @param value
     * @param liveTime
     */
    public abstract void set(byte[] key, byte[] value, long liveTime);

    /**
     * 添加key value 并且设置存活时间
     * 
     * @param key
     * @param value
     * @param liveTime
     *            单位秒
     */
    public abstract void set(String key, String value, long liveTime);

    /**
     * 添加key value
     * 
     * @param key
     * @param value
     */
    public abstract void set(String key, String value);

    /**
     * 添加key value (字节)(序列化)
     * 
     * @param key
     * @param value
     */
    public abstract void set(byte[] key, byte[] value);

    /**
     * 获取redis value (String)
     * 
     * @param key
     * @return
     */
    public abstract String get(String key);

    /**
     * 通过正则匹配keys
     * 
     * @param pattern
     * @return
     */
    public abstract Set keys(String pattern);

    /**
     * 检查key是否已经存在
     * 
     * @param key
     * @return
     */
    public abstract boolean exists(String key);

    /**
     * 清空redis 所有数据
     * 
     * @return
     */
    public abstract String flushDB();

    /**
     * 查看redis里有多少数据
     */
    public abstract long dbSize();

    /**
     * 检查是否连接成功
     * 
     * @return
     */
    public abstract String ping();
    
    //  
    public String getSeriNo();
    
    /**
     * 
    *  生成8位订单编号  累加
    * @return
    * @return 返回类型
    * @throws Exception 异常
     */
    public String getOrderNumber();
    
    
}
