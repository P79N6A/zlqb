package com.nyd.capital.service;

import com.nyd.capital.dao.CrudDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


public abstract class CrudService<D extends CrudDao<T>, T> {

    /**
     * 持久层对象
     */
    @Autowired
    private D dao;

    public D getDao() {
        return dao;
    }

    /**
     * 获取单条数据
     *
     * @param id 主键
     * @return 数据实体
     */
    public T get(Long id) {
        return dao.get(id);
    }

    /**
     * 获取单条数据
     *
     * @param entity 实体对象
     * @return 实体对象
     */
    public T get(T entity) {
        return dao.get(entity);
    }

    /**
     * 查询列表数据
     *
     * @param entity 实体对象
     * @return 实体对象列表
     */
    public List<T> findList(T entity) {
        return dao.findList(entity);
    }
    /**
     * 查询列表数据
     *
     */
    public List<T> findAllList() {
        return dao.findAllList();
    }

    /**
     * 查询列表数据
     *
     * @param queryMap 查询条件
     * @return 实体对象列表
     */
    public List<T> queryList(Map<String, Object> queryMap) {
        return dao.queryList(queryMap);
    }



}
