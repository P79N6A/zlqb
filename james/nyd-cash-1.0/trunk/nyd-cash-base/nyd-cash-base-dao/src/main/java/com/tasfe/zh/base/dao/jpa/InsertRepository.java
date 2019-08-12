package com.tasfe.zh.base.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by Lait on 2017/8/1.
 * 扩展JpaRepository，添加对update的支持
 */
@NoRepositoryBean
public interface InsertRepository<T, ID extends Serializable> extends
        JpaRepository<T, ID>,
        JpaSpecificationExecutor<T> {
}
