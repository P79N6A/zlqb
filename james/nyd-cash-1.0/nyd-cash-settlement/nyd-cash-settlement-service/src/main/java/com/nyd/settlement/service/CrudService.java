package com.nyd.settlement.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.settlement.dao.CrudDao;
import com.nyd.settlement.model.paging.Paging;
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
     * @param queryMap 查询条件
     * @return 实体对象列表
     */
    public List<T> queryList(Map<String, Object> queryMap) {
        return dao.queryList(queryMap);
    }

    /**
     * 查询分页数据
     *
     * @param page   分页对象
     * @param entity 实体对象
     * @return 分页数据
     */
    public PageInfo<T> findPage(Paging page, T entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.getOrderBy());
        List<T> list = dao.findList(entity);
        return new PageInfo<>(list);
    }

    /**
     * 查询分页数据
     *
     * @param page     分页对象
     * @param queryMap 查询条件
     * @return 分页数据
     */
    public PageInfo<T> queryPage(Paging page, Map<String, Object> queryMap) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.getOrderBy());
        List<T> list = dao.queryList(queryMap);
        return new PageInfo<>(list);
    }

}
