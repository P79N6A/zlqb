package com.tasfe.zh.base.dao.jpa.specification;

import org.springframework.data.jpa.domain.Specification;

/**
 * Created by Lait on 2017/8/8.
 * 设置地否可以缓存。主要针对非ID的查询方式的缓存
 */
public interface CacheableSpecification<T> extends Specification<T> {
    /**
     * 是否可以缓存
     *
     * @return
     */
    public boolean isCacheable();
}
