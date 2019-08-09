package com.tasfe.zh.base.dao.test;

import com.tasfe.zh.base.dao.GenericCrudRepository;
import com.tasfe.zh.base.dao.annotation.MybatisQuery;
import com.tasfe.zh.base.dao.mybatis.plugins.page.MybatisPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by liupeng on 2016-6-15.
 */
public interface TmpUserDao extends GenericCrudRepository<TmpUser, Long> {

    @MybatisQuery
    List<TmpUser> findAll(@Param("param") Map<String, Object> searchParam);

    @MybatisQuery
    MybatisPage<TmpUser> findAll(@Param("param") Map<String, Object> searchParam, Pageable pageable);

}
