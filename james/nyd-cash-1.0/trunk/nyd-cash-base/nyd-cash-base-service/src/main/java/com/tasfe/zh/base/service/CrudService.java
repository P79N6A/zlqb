package com.tasfe.zh.base.service;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.zh.base.dao.jpa.impls.Query;
import com.tasfe.zh.base.model.PageResults;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lait on 2017/7/28.
 */
public interface CrudService<M, E, PK extends Serializable> {

    //GenericCrudRepository getGenericCrudRepository();

    /************************* jpa 实现 方便快速开发  *************************/
    /**
     * 保存对象
     *
     * @param entity 需要添加的对象
     */
    void save(E entity) throws Exception;

    /**
     * 批量保存对象
     *
     * @param entitys 需要增加的对象的集合
     *                失败会抛异常
     */
    void save(List<E> entitys) throws Exception;

    /**
     * 删除对象
     *
     * @param model 需要删除的对象
     *              失败会抛异常
     */
    void delete(M model, Criteria criteria) throws Exception;

    /**
     * 批量删除对象
     *
     * @param entitys 需要删除的对象的集合
     *                失败会抛异常
     */
    void delete(List<M> entitys, Criteria criteria) throws Exception;

    /**
     * @param id
     * @throws Exception
     */
    void delete(PK... id) throws Exception;


    void update(M model, Criteria criteria) throws Exception;

    void update(List<M> models, Criteria criteria) throws Exception;

    /**
     * 更新或保存对象
     *
     * @param entity 需要更新的对象
     *               失败会抛出异常
     */
    void saveOrUpdate(E entity) throws Exception;

    /**
     * 批量更新或保存对象
     *
     * @param entitys 需要更新或保存的对象
     *                失败会抛出异常
     */
    void saveOrUpdate(List<E> entitys) throws Exception;

    /**
     * 通过主键, 查询对象
     *
     * @param id 主键(Serializable)
     * @return model
     */
    M get(PK id) throws Exception;


    /**
     * @param id
     * @return
     * @throws Exception
     */
    List<M> gets(PK... id) throws Exception;


    /**
     * like
     *
     * @return List
     */
    List<M> find(M model, Criteria criteria) throws Exception;


    /**
     * like
     *
     * @return List
     */
    List<M> find(Criteria criteria) throws Exception;

    /**
     * 分页查询
     *
     * @param model
     * @param criteria
     * @return 查询结果
     */
    Page<M> paging(M model, Criteria criteria) throws Exception;


    Page<M> paging(Criteria criteria) throws Exception;

    /**
     * 按条件分页
     *
     * @param currentPageNumber 页码
     * @param pageSize          每页数量
     * @param query             封装的查询条件
     * @return 查询结果
     */
    PageResults<M> list(Integer currentPageNumber,
                        Integer pageSize,
                        Query query)
            throws Exception;

    /**
     * 获得数量 利用Count(*)实现
     *
     * @return 数量
     */
    long count() throws Exception;

    /**
     * 获得符合对应条件的数量 利用Count(*)实现
     *
     * @param query 查询条件
     * @return 数量
     */
    int count(Query query) throws Exception;

    /**
     * 执行Sql语句
     *
     * @param sql    sql
     * @param values 不定参数数组
     * @return 受影响的行数
     */
    int executeSql(String sql, Object... values)
            throws Exception;

    /**
     * 通过jpql查询
     *
     * @param jpql   jpql语句
     * @param values 参数列表
     * @return 受影响的行数
     */
    Object queryByJpql(String jpql, Object... values);

    /**
     * 获得符合对应条件的数量 利用Count(*)实现
     *
     * @param jpql jpql查询条件
     * @return 数量
     */
    int getCountByJpql(String jpql, Object... values);


    /**
     * 通过Jpql分页查询
     *
     * @param currentPageNumber 当前页
     * @param pageSize          每页数量
     * @param jpql              jpql语句
     * @param values            jpql参数
     * @return 查询结果
     */
    PageResults<Object> getListByPageAndJpql(Integer currentPageNumber,
                                             Integer pageSize,
                                             String jpql,
                                             Object... values);

    /**
     * 执行jpql语句
     *
     * @param jpql   jpql语句
     * @param values 参数列表
     * @return 受影响的行数
     */
    int executeJpql(String jpql, Object... values);

    /**
     * refresh 刷新实体状态
     *
     * @param model 实体
     */
    void refresh(E model) throws Exception;
}
