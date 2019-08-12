package com.tasfe.zh.base.dao.jpa;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Lait on 2017/8/1.
 * 扩展JpaRepository，添加对update的支持
 */
@NoRepositoryBean
public interface FunctionRepository<T, ID extends Serializable> extends
        JpaRepository<T, ID>,
        JpaSpecificationExecutor<T> {
    /**
     * 统计加和运算
     *
     * @param spec       查询条件
     * @param fieldName  字段名称
     * @param resultType 返回值类型
     * @return
     */
    public <S extends Number> S sum(String fieldName, Class<S> resultType, Specification<T> spec);


}
