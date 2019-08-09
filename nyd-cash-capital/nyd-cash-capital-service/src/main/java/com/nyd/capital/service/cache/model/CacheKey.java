package com.nyd.capital.service.cache.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 表示缓存的Key
 * Cong Yuxiang
 * 2017/11/21
 */
public @Data class CacheKey extends BaseTO {


    /** 命名空间 */
    private String            nameSpace;

    private String            key;

    public CacheKey(String key) {
        this(null, key);
    }

    public CacheKey(String nameSpace, String key) {
        this.nameSpace = nameSpace;
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(nameSpace)) {
            sb.append(nameSpace).append(".");
        }
        sb.append(key);
        this.key = sb.toString();
    }


}
