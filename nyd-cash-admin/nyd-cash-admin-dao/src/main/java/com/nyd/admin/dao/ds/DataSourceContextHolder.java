package com.nyd.admin.dao.ds;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceContextHolder {

    public static final String DATA_SOURCE_FINANCE = "financeDataSource";
    public static final String DATA_SOURCE_ZEUS = "zeusDataSource";
    public static final String DATA_SOURCE_ADMIN = "adminDataSource";
    public static final String DATA_SOURCE_USER = "userDataSource";
    private static final Logger logger = LoggerFactory.getLogger(DataSourceContextHolder.class);
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
        logger.info("Switch DataSource -> " + dataSource);
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
}
