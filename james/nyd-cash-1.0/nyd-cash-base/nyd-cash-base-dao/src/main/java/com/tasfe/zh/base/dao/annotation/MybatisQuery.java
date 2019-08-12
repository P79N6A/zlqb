package com.tasfe.zh.base.dao.annotation;

import org.springframework.data.annotation.QueryAnnotation;

import java.lang.annotation.*;

/**
 * Created by Lait on 2017/8/8.
 * 对mybatis方法的标注
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@QueryAnnotation
@Documented
public @interface MybatisQuery {
}
