package com.tasfe.zh.base.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.zh.base.dao.GenericCrudRepository;
import com.tasfe.zh.base.dao.jpa.impls.Query;
import com.tasfe.zh.base.model.PageResults;
import com.tasfe.zh.base.service.CrudService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by Lait on 2017/7/26.
 */
public abstract class CrudServiceImpl<M, E, PK extends Serializable> implements CrudService<M, E, PK> {

    @Autowired(required = false)
    protected CrudTemplate crudTemplate;
    private Class<E> entityClass;
    private Class<M> modelClass;

    public CrudServiceImpl() {
        modelClass = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }


    //@Autowired
    //private GenericDao<T,ID> baseDao;
    //private BaseDao<T> baseDao;
    /*@Autowired
    protected EntityManager entityManager;*/
    //@Autowired(required = true)
    //private GenericCrudRepository<T,ID> genericCrudRepository;
   /* @Override
    public GenericCrudRepository getGenericCrudRepository() {
        //return genericCrudRepository;
        return null;
    }*/

    /**
     * 保存对象
     *
     * @param model 需要添加的对象
     */
    @Override
    public void save(E model) throws Exception {
        crudTemplate.save(model);
    }

    /**
     * 批量保存对象
     *
     * @param modelList 需要增加的对象的集合
     *                  失败会抛异常
     */
    @Override
    public void save(List<E> modelList) throws Exception {
        crudTemplate.save(modelList);
    }

    /**
     * 删除对象
     *
     * @param model 需要删除的对象
     *              失败会抛异常
     */
    @Override
    public void delete(M model, Criteria criteria) throws Exception {
        crudTemplate.delete(model, criteria);
    }

    /**
     * 批量删除对象
     *
     * @param entitys 需要删除的对象的集合
     *                失败会抛异常
     */
    @Override
    public void delete(List<M> entitys, Criteria criteria) throws Exception {
        for (M entity : entitys) {
            crudTemplate.delete(entity, criteria);
        }
    }

    /**
     * 按照id删除对象
     *
     * @param id 需要删除的对象的id
     *           失败抛出异常
     */
    @Override
    public void delete(PK... id) throws Exception {
        crudTemplate.delete(modelClass, id);
    }


    @Override
    public void update(M entity, Criteria criteria) throws Exception {
        crudTemplate.update(entity, criteria);
    }

    @Override
    public void update(List<M> entitys, Criteria criteria) throws Exception {
        for (M entity : entitys) {
            crudTemplate.update(entity, criteria);
        }
    }

    /**
     * 更新或保存对象
     *
     * @param model 需要更新的对象
     *              失败会抛出异常
     */
    @Override
    public void saveOrUpdate(E model) throws Exception {
        crudTemplate.save(model);
    }

    /**
     * 批量更新或保存对象
     *
     * @param modelList 需要更新或保存的对象
     *                  失败会抛出异常
     */
    @Override
    public void saveOrUpdate(final List<E> modelList) throws Exception {
        crudTemplate.save(modelList);
    }

    /**
     * 通过主键, 查询对象
     *
     * @param id 主键(Serializable)
     * @return model
     */
    @Override
    public M get(PK id) throws Exception {
        E e = crudTemplate.get(entityClass, id);
        M m = modelClass.newInstance();
        BeanUtils.copyProperties(e, m);
        return m;
    }

    @Override
    public List<M> gets(PK... id) throws Exception {
        return crudTemplate.list(modelClass, id);
    }

    @Override
    public List<M> find(Criteria criteria) throws Exception {
        M model = modelClass.newInstance();
        return crudTemplate.find(model, criteria);
    }

    @Override
    public List<M> find(M model, Criteria criteria) throws Exception {
        return crudTemplate.find(model, criteria);
    }

    @Override
    public Page<M> paging(Criteria criteria) throws Exception {
        M model = modelClass.newInstance();
        return crudTemplate.paging(model, criteria);
    }

    @Override
    public Page<M> paging(M model, Criteria criteria) throws Exception {
        return crudTemplate.paging(model, criteria);
    }


    /**
     * 分页查询
     *
     * @param currentPageNumber 页码
     * @param pageSize          每页数量
     * @return 查询结果
     */
    public List<M> list(final Integer currentPageNumber, final Integer pageSize) throws Exception {
        return null;
    }

    /**
     * 按条件分页
     *
     * @param currentPageNumber 页码
     * @param pageSize          每页数量
     * @param query             封装的查询条件
     * @return 查询结果
     */
    public PageResults<M> list(Integer currentPageNumber, Integer pageSize, Query query) throws Exception {
        return null;
        //return baseDao.getListByPageAndQuery(currentPageNumber, pageSize, query);
    }

    /**
     * 获得数量 利用Count(*)实现
     *
     * @return 数量
     */
    public long count() throws Exception {
        //return crudTemplate.count();
        //return baseDao.getCount(modelClass);
        return crudTemplate.count(null, null);
    }

    /**
     * 获得符合对应条件的数量 利用Count(*)实现
     *
     * @param query 查询条件
     * @return 数量
     */
    public int count(final Query query) throws Exception {
        //return genericCrudRepository.coun
        //return baseDao.getCountByQuery(query);
        return 0;
    }

    /**
     * 执行Sql语句
     *
     * @param sql    sql
     * @param values 不定参数数组
     * @return 受影响的行数
     */
    public int executeSql(final String sql, final Object... values) throws Exception {
        return 0;
        //return baseDao.executeSql(sql, values);
    }

    /**
     * 通过jpql查询
     *
     * @param jpql   jpql语句
     * @param values 参数列表
     * @return 受影响的行数
     */
    public Object queryByJpql(final String jpql, final Object... values) {
        return null;
        //return baseDao.queryByJpql(jpql, values);
    }

    /**
     * 获得符合对应条件的数量 利用Count(*)实现
     *
     * @param jpql jpql查询条件
     * @return 数量
     */
    public int getCountByJpql(final String jpql, final Object... values) {

        return 0;
        //return baseDao.getCountByJpql(jpql, values);
    }


    /**
     * 通过Jpql分页查询
     *
     * @param currentPageNumber 当前页
     * @param pageSize          每页数量
     * @param jpql              jpql语句
     * @param values            jpql参数
     * @return 查询结果
     */
    public PageResults<Object> getListByPageAndJpql(Integer currentPageNumber, Integer pageSize, final String jpql, Object... values) {
        return null;
        //return baseDao.getListByPageAndJpql(currentPageNumber, pageSize, jpql, values);
    }

    /**
     * 执行jpql语句
     *
     * @param jpql   jpql语句
     * @param values 参数列表
     * @return 受影响的行数
     */
    public int executeJpql(final String jpql, final Object... values) {
        return 0;
        //return baseDao.executeJpql(jpql, values);
    }

    /**
     * refresh 刷新实体状态
     *
     * @param entity 实体
     */
    public void refresh(E entity) throws Exception {
        //crudTemplate.refresh(model);
    }


}
