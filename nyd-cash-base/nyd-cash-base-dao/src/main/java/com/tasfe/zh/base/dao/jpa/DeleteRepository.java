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
public interface DeleteRepository<T, ID extends Serializable> extends
        JpaRepository<T, ID>,
        JpaSpecificationExecutor<T> {

	/*public Integer deleteById(String id);

	public Integer deleteByFieldEq(String Field, Object value);

	public Integer deleteByFieldLike(String Field, String value);

	public Integer deleteByFieldIn(String Field, Iterable<?> values);*/


}
