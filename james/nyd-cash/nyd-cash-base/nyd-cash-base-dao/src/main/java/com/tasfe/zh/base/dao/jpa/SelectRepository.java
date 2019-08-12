package com.tasfe.zh.base.dao.jpa;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Lait on 2017/8/1.
 * 扩展JpaRepository，添加对update的支持
 */
@NoRepositoryBean
public interface SelectRepository<T, ID extends Serializable> extends
        JpaRepository<T, ID>,
        JpaSpecificationExecutor<T> {

    /**
     * 查询多个字段
     *
     * @param fields 查询字段数组
     * @param spec   查询条件
     * @return 返回list，每个元素为一个map对象，存储字段名称和字段值的映射
     */
    public List<Map<String, Object>> queryMultiFields(String[] fields, Specification<T> spec);

    /**
     * 查询单个字段
     *
     * @param field
     * @param spec
     * @return 返回待查询字段的list对象
     */
    public List<String> querySingleFields(String field, Specification<T> spec);

	/*
    public T findOneById(String id);

	public T findOneByField(String field, Object value);

	public List<T> findAllByFieldEq(String field, Object value);

	public List<T> findAllByFieldLike(String field, String value);

	public List<T> findAllByFieldIn(String Field, Iterable<?> values);

	public Object findOneByHql(String hql);

	@SuppressWarnings("rawtypes")
	public List findAllByHql(String hql);

	public Object findOneBySql(String sql);

	@SuppressWarnings("rawtypes")
	public List findAllBySql(String sql);

	public List findAllByHql(String sql, Integer max);

	public List findAllByHql(String sql, Integer max, Integer startPosition);
	*/
}
