package com.nyd.admin.service.batch;

import org.springframework.batch.item.database.JdbcCursorItemReader;

/**
 * Cong Yuxiang
 * 2017/11/27
 **/
public class NewJdbcCursorItemReader<T> extends JdbcCursorItemReader<T> {
    private String tableName;

    public NewJdbcCursorItemReader() {
        super();
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        String newSql = super.getSql().replace("?",tableName);
        super.setSql(newSql);
    }
}
