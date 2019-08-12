package com.nyd.capital.service.cache.model;

import lombok.Data;

/**
 * 对缓存对象的封装类
 * Cong Yuxiang
 * 2017/11/21
 */
public @Data class CacheWrapper extends BaseTO {



    /** 被缓存的内容 */
    private String            obj;

    /** 过期时间,单位:秒.0表示不过期 */
    private int               expireTime       = 0;

    /** 创建时间 */
    private long              createTime;

    /** 最后一次访问该缓存时间 */
    private long              lastAccessTime;

    public CacheWrapper(String obj) {
        this(obj, 0);
    }

    public CacheWrapper(String obj, int expireTime) {
        this.obj = obj;
        this.expireTime = expireTime;
        this.createTime = System.currentTimeMillis();
        this.lastAccessTime = System.currentTimeMillis();
    }

    /**
     * 判断该缓存是否已过期
     */
    public boolean isExpire() {
        if (expireTime > 0) {
            return createTime + expireTime * 1000 < System.currentTimeMillis();
        }
        return false;
    }



}
