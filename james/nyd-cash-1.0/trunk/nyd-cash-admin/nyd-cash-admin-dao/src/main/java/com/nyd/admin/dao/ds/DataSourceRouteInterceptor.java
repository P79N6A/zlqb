package com.nyd.admin.dao.ds;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class DataSourceRouteInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceRouteInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        SqlCommandType commandType = ms.getSqlCommandType();
        logger.info("SqlCommandType : " + commandType);
        if (commandType == SqlCommandType.INSERT || commandType == SqlCommandType.UPDATE
                || commandType == SqlCommandType.DELETE) {
            DataSourceContextHolder.setDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE);
            DataSourceContextHolder.setDataSource(DataSourceContextHolder.DATA_SOURCE_USER);
        } else if (commandType == SqlCommandType.SELECT) {
            DataSourceContextHolder.setDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS);
            DataSourceContextHolder.setDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN);
            DataSourceContextHolder.setDataSource(DataSourceContextHolder.DATA_SOURCE_USER);
        } else {
            DataSourceContextHolder.setDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE);
            DataSourceContextHolder.setDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
