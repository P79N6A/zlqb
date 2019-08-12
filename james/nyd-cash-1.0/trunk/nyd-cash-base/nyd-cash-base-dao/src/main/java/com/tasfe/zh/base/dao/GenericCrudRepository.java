package com.tasfe.zh.base.dao;

import com.tasfe.zh.base.dao.jpa.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by Lait on 2017/8/1.
 * 扩展JpaRepository，添加对update的支持
 */
@NoRepositoryBean
public interface GenericCrudRepository<T, ID extends Serializable> extends
        JpaRepository<T, ID>,
        JpaSpecificationExecutor<T>,
        InsertRepository<T, ID>,
        SelectRepository<T, ID>,
        UpdateRepository<T, ID>,
        DeleteRepository<T, ID>,
        FunctionRepository<T, ID> {

    /**
     * 获取当前dao对应的实体管理器
     *
     * @return
     */
    EntityManager getEntityManager();

    /**
     * 获取进行操作的领域类
     */
    Class<T> getDomainClass();


}
